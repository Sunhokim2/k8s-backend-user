package com.welab.k8sbackenduser.service;

import com.welab.k8sbackenduser.common.exception.BadParameter;
import com.welab.k8sbackenduser.common.exception.NotFound;
import com.welab.k8sbackenduser.domain.SiteUser;
import com.welab.k8sbackenduser.domain.dto.SiteUserLoginDto;
import com.welab.k8sbackenduser.domain.dto.SiteUserRefreshDto;
import com.welab.k8sbackenduser.domain.dto.SiteUserRegisterDto;
import com.welab.k8sbackenduser.domain.event.SiteUserInfoEvent;
import com.welab.k8sbackenduser.domain.repository.SiteUserRepository;
import com.welab.k8sbackenduser.event.producer.KafkaMessageProducer;
import com.welab.k8sbackenduser.secret.hash.SecureHashUtils;
import com.welab.k8sbackenduser.secret.jwt.TokenGenerator;
import com.welab.k8sbackenduser.secret.jwt.dto.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void registerUser(SiteUserRegisterDto registerDto) {
        try {
            SiteUser siteUser = registerDto.toEntity();
            SiteUser savedUser = siteUserRepository.save(siteUser); // DB 저장

            // Kafka 메시지 전송 (DB 저장 성공 후에만)
            kafkaMessageProducer.send(SiteUserInfoEvent.Topic, registerDto);


        } catch (Exception e) {
            // 어떤 예외가 발생하는지 여기서 확인 가능
            log.error("사용자 등록 중 예외 발생: {}", e.getMessage(), e);
            throw e; // 예외를 다시 던져서 500 에러로 이어지게 함
        }
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {
        SiteUser user = siteUserRepository.findByUserId(loginDto.getUserId());
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다.");
        }
        if (!SecureHashUtils.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadParameter("비밀번호가 맞지 않습니다.");
        }
        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessToken refresh(@Valid SiteUserRefreshDto refreshDto) {
        String userId = tokenGenerator.validateJwtToken(refreshDto.getToken());
        if (userId == null) {
            throw new BadParameter("토큰이 유효하지 않습니다.");
        }

        SiteUser user = siteUserRepository.findByUserId(userId);
        if (user == null) {
            throw new NotFound("사용자를 찾을 수 없습니다.");
        }

        return tokenGenerator.generateAccessToken(userId, "WEB");
    }
}
