package com.welab.k8sbackenduser.api.backend;

import com.welab.k8sbackenduser.common.dto.ApiResponseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/backend/user/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BackendUserController {


    @GetMapping(value = "/test")
    public ApiResponseDto<String> test() {
        return ApiResponseDto.createOk("예스 안녕하세요 유저입니다.");
    }
}
