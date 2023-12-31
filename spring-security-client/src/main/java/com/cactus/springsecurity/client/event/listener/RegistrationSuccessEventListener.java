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

import static com.cactus.springsecurity.client.utils.Constants.FORWARD_SLASH;
import static com.cactus.springsecurity.client.utils.Constants.USER;

@Component
@Slf4j
public class RegistrationSuccessEventListener implements ApplicationListener<RegistrationSuccessEvent> {

	private final IUserService userService;

	@Autowired
	public RegistrationSuccessEventListener(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(RegistrationSuccessEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.persistVerificationToken(token, user);
		String verificationUrl = event.getApplicationUrl() + FORWARD_SLASH + USER + FORWARD_SLASH
				+ Constants.VERIFY_REGISTRATION_ENDPOINT + token;
		log.info("Follow the link to verify your account!! : " + verificationUrl);
		event.getVerificationUrlFuture().complete(verificationUrl);
	}

}
