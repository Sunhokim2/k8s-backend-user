package com.welab.k8sbackenduser.domain.event;

import com.welab.k8sbackenduser.domain.SiteUser;

import java.time.LocalDateTime;

public class SiteUserInfoEvent {
    public static final String Topic = "userinfo";

    private String action;
    private String userId;
    private String phoneNumber;
    private LocalDateTime eventTime;

    public static SiteUserInfoEvent fromEntity(String action, SiteUser siteUser) {
        SiteUserInfoEvent event = new SiteUserInfoEvent();

        event.action = action;
        event.userId = siteUser.getUserId();
        event.phoneNumber = siteUser.getPhoneNumber();
        event.eventTime = LocalDateTime.now();

        return event;
    }
}
