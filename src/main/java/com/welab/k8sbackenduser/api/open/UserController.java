package com.welab.k8sbackenduser.api.open;

import com.welab.k8sbackenduser.common.dto.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/api/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {
    @GetMapping(value = "/test")
    public ApiResponseDto<String> test() {
        return ApiResponseDto.createOk("예스 안녕하세요 유저입니다.");
    }
}
