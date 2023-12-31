package com.cactus.springsecurity.client.event;

import com.cactus.springsecurity.client.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationSuccessEvent extends ApplicationEvent {

    private User user;
    private String applicationUrl;

    public RegistrationSuccessEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
