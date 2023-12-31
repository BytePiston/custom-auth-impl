package com.cactus.springsecurity.client.event.listener;

import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.event.RegistrationSuccessEvent;
import com.cactus.springsecurity.client.event.ResetPasswordEvent;
import com.cactus.springsecurity.client.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.cactus.springsecurity.client.utils.Constants.*;

@Component
@Slf4j
public class ResetPasswordEventListener implements ApplicationListener<ResetPasswordEvent> {

	private final IUserService userService;

	@Autowired
	public ResetPasswordEventListener(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(ResetPasswordEvent event) {
		User user = event.getUser();
		String token = UUID.randomUUID().toString();
		userService.persistResetPasswordToken(token, user);
		String resetPasswordUrl = event.getApplicationUrl() + FORWARD_SLASH + USER + FORWARD_SLASH
				+ SAVE_PASSWORD_ENDPOINT + token;
		log.info("Follow the link to reset your password!! : " + resetPasswordUrl);
		event.getResetPasswordUrlFuture().complete(resetPasswordUrl);
	}

}
