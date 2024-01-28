package com.cactus.springsecurity.client.controller;

import com.cactus.springsecurity.client.entity.RegistrationToken;
import com.cactus.springsecurity.client.entity.ResetPasswordToken;
import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.event.RegistrationSuccessEvent;
import com.cactus.springsecurity.client.event.ResetPasswordEvent;
import com.cactus.springsecurity.client.exception.ResourceNotFoundException;
import com.cactus.springsecurity.client.model.ChangePasswordModel;
import com.cactus.springsecurity.client.model.ResetPasswordModel;
import com.cactus.springsecurity.client.model.UserModel;
import com.cactus.springsecurity.client.response.RegistrationTokenResponse;
import com.cactus.springsecurity.client.response.ResetPasswordResponse;
import com.cactus.springsecurity.client.response.UserRegistrationResponse;
import com.cactus.springsecurity.client.response.UserResponse;
import com.cactus.springsecurity.client.service.IUserService;
import com.cactus.springsecurity.client.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.cactus.springsecurity.client.utils.Constants.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

	private final IUserService userService;

	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	public UserController(IUserService userService, ApplicationEventPublisher eventPublisher) {
		this.userService = userService;
		this.eventPublisher = eventPublisher;
	}

	@PostMapping("/register")
	public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserModel userModel,
			final HttpServletRequest request) {

		User user = userService.registerUser(userModel);
		RegistrationSuccessEvent event = new RegistrationSuccessEvent(user, Utils.getApplicationUrl(request));
		eventPublisher.publishEvent(event);
		CompletableFuture<String> verificationUrl = event.getVerificationUrlFuture();
		UserRegistrationResponse response = UserRegistrationResponse.builder()
			.firstName(userModel.getFirstName())
			.lastName(userModel.getLastName())
			.email(userModel.getEmail())
			.role(userModel.getRole())
			.verificationUrl(verificationUrl.join())
			.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/verifyRegistration")
	public ResponseEntity<RegistrationTokenResponse> validateTokenUrl(@RequestParam("token") String token,
			HttpServletRequest request) {

		String responseString = userService.validateRegistrationToken(token);
		if (responseString.equals(SUCCESS)) {
			RegistrationTokenResponse response = RegistrationTokenResponse.builder()
				.status(SUCCESS)
				.message("Token Successfully Verified!!")
				.build();
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		else if (responseString.equals(INVALID)) {
			RegistrationTokenResponse response = RegistrationTokenResponse.builder()
				.status(INVALID)
				.message("Invalid Token, please verify URL!!")
				.build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		else {
			String url = Utils.getApplicationUrl(request) + FORWARD_SLASH + USER + FORWARD_SLASH
					+ RESEND_VERIFY_TOKEN_ENDPOINT + token;
			RegistrationTokenResponse response = RegistrationTokenResponse.builder()
				.status(EXPIRED)
				.message("Token Expired, Please follow link to generate New Token!!")
				.tokenUrl(url)
				.build();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}

	@PostMapping("/resendVerificationToken")
	public ResponseEntity<RegistrationTokenResponse> resendVerificationToken(@RequestParam("token") String oldToken,
			HttpServletRequest request) {

		Optional<RegistrationToken> verificationToken = userService.fetchVerificationToken(oldToken);
		if (verificationToken.isPresent()) {
			RegistrationToken registrationTokenObj = verificationToken.get();
			User user = registrationTokenObj.getUser();
			userService.deleteVerificationToken(registrationTokenObj);
			RegistrationSuccessEvent event = new RegistrationSuccessEvent(user, Utils.getApplicationUrl(request));
			eventPublisher.publishEvent(event);
			CompletableFuture<String> verificationUrl = event.getVerificationUrlFuture();
			RegistrationTokenResponse response = RegistrationTokenResponse.builder()
				.status(SUCCESS)
				.message("New Token Successfully Generated!!")
				.tokenUrl(verificationUrl.join())
				.build();
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		RegistrationTokenResponse response = RegistrationTokenResponse.builder()
			.status(INVALID)
			.message("Invalid Token, please verify URL!!")
			.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/resetPassword")
	public ResponseEntity<ResetPasswordResponse> resetPassword(@NotNull @RequestParam("email") String email,
			HttpServletRequest request) {
		Optional<User> userOptional = userService.fetchUserByEmail(email);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			ResetPasswordEvent event = new ResetPasswordEvent(user, Utils.getApplicationUrl(request));
			eventPublisher.publishEvent(event);
			CompletableFuture<String> resetUrlFuture = event.getResetPasswordUrlFuture();
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(SUCCESS)
				.message("New Url Successfully Generated!!")
				.tokenUrl(resetUrlFuture.join())
				.build();
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}

		ResetPasswordResponse response = ResetPasswordResponse.builder()
			.status(INVALID)
			.message("Invalid Request, please verify Email!!")
			.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@PostMapping("/updatePassword")
	public ResponseEntity<ResetPasswordResponse> updatePassword(
			@Valid @RequestBody ResetPasswordModel resetPasswordModel, @RequestParam("token") String token,
			HttpServletRequest request) throws ResourceNotFoundException {
		String responseString = userService.validateResetPasswordToken(token);
		if (responseString.equals(SUCCESS)) {
			userService.updatePassword(resetPasswordModel.getEmail(), resetPasswordModel.getNewPassword());
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(SUCCESS)
				.message("Successfully Updated Password!!")
				.build();
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		else if (responseString.equals(INVALID)) {
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(INVALID)
				.message("Invalid Token, please verify URL!!")
				.build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		else {
			String url = Utils.getApplicationUrl(request) + FORWARD_SLASH + USER + FORWARD_SLASH
					+ RESEND_RESET_PASSWORD_TOKEN_ENDPOINT + token;
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(EXPIRED)
				.message("Token Expired, Please follow link to generate New Token!!")
				.tokenUrl(url)
				.build();
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
	}

	@PostMapping("/resendResetPasswordToken")
	public ResponseEntity<ResetPasswordResponse> resendResetPasswordToken(@RequestParam("token") String oldToken,
			HttpServletRequest request) {
		Optional<ResetPasswordToken> passwordTokenOptional = userService.fetchResetPasswordToken(oldToken);
		if (passwordTokenOptional.isPresent()) {
			ResetPasswordToken passwordToken = passwordTokenOptional.get();
			User user = passwordToken.getUser();
			userService.deleteResetPasswordToken(passwordToken);
			ResetPasswordEvent event = new ResetPasswordEvent(user, Utils.getApplicationUrl(request));
			eventPublisher.publishEvent(event);
			CompletableFuture<String> resetPasswordUrl = event.getResetPasswordUrlFuture();
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(SUCCESS)
				.message("New Token Successfully Generated!!")
				.tokenUrl(resetPasswordUrl.join())
				.build();
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		else {
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(INVALID)
				.message("Invalid Token, please verify URL!!")
				.build();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<ResetPasswordResponse> changePassword(
			@Valid @RequestBody ChangePasswordModel changePasswordModel, HttpServletRequest request)
			throws ResourceNotFoundException {
		if (userService.isValidateUserAndOldPassword(changePasswordModel)) {
			userService.updatePassword(changePasswordModel.getEmail(), changePasswordModel.getNewPassword());
			ResetPasswordResponse response = ResetPasswordResponse.builder()
				.status(SUCCESS)
				.message("Password Updated Successfully!!")
				.build();
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
		}
		ResetPasswordResponse response = ResetPasswordResponse.builder()
			.status(INVALID)
			.message("Invalid Password Update Request, Please Verify!!")
			.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@GetMapping("/")
	public UserResponse fetchLoggedInUser(Principal principal) {
		Optional<User> userOptional = userService.fetchUserByEmail(principal.getName());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			return UserResponse.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.email(user.getEmail())
				.role(user.getRole())
				.build();
		}
		return null;
	}

	@GetMapping("/users")
	public List<UserResponse> fetchAllUsers() {
		List<User> userList = userService.fetchAllUsers();
		if (userList != null && !userList.isEmpty()) {
			List<UserResponse> userResponseList = new ArrayList<>();
			for (User user : userList) {
				UserResponse userResponse = UserResponse.builder()
					.firstName(user.getFirstName())
					.lastName(user.getLastName())
					.email(user.getEmail())
					.role(user.getRole())
					.build();
				userResponseList.add(userResponse);
			}
			return userResponseList;
		}
		return new ArrayList<>();
	}

}
