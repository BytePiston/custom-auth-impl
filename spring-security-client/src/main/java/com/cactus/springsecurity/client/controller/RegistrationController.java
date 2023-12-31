package com.cactus.springsecurity.client.controller;

import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.event.RegistrationSuccessEvent;
import com.cactus.springsecurity.client.model.UserModel;
import com.cactus.springsecurity.client.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cactus.springsecurity.client.utils.Constants.HTTP;

@RestController
@RequestMapping("api/v1/user")
public class RegistrationController {

	private final IUserService userService;

	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public RegistrationController(IUserService userService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
	}

	@PostMapping("/register")
	public HttpStatus registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
		User user = userService.registerUser(userModel);
		eventPublisher.publishEvent(new RegistrationSuccessEvent(user, getApplicationUrl(request)));
		return HttpStatus.CREATED;
	}

	private String getApplicationUrl(HttpServletRequest request) {
		return HTTP + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
