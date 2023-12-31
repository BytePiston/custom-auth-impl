package com.cactus.springsecurity.client.event.listener;

import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.event.RegistrationSuccessEvent;
import com.cactus.springsecurity.client.service.IUserService;
import com.cactus.springsecurity.client.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationSuccessEventListener implements ApplicationListener<RegistrationSuccessEvent> {

    private final IUserService userService;

    @Autowired
    public RegistrationSuccessEventListener(IUserService userService){
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(RegistrationSuccessEvent event) {
        //TODO: Generate "Validation Token" for the user with link.

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.persistVerificationToken(token, user);
        //TODO: Send Email with to user "Validation Link".
        String verificationUrl = event.getApplicationUrl() + Constants.VERIFY_REGISTRATION_ENDPOINT + token;
        log.info("Follow the link to Verify your Account!! : "+verificationUrl);
    }
}
