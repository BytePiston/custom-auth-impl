package com.cactus.springsecurity.client.service;

import com.cactus.springsecurity.client.entity.RegistrationToken;
import com.cactus.springsecurity.client.entity.ResetPasswordToken;
import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.exception.ResourceNotFoundException;
import com.cactus.springsecurity.client.model.ChangePasswordModel;
import com.cactus.springsecurity.client.model.UserModel;

import java.util.Optional;

public interface IUserService {

	User registerUser(UserModel userModel);

	void persistVerificationToken(String token, User user);

	String validateRegistrationToken(String token);

	Optional<RegistrationToken> fetchVerificationToken(String oldToken);

	void deleteVerificationToken(RegistrationToken registrationTokenObj);

	Optional<User> fetchUserByEmail(String email);

	void persistResetPasswordToken(String token, User user);

	String validateResetPasswordToken(String token);

	void updatePassword(String email, String newPassword) throws ResourceNotFoundException;

	Optional<ResetPasswordToken> fetchResetPasswordToken(String oldToken);

	void deleteResetPasswordToken(ResetPasswordToken passwordToken);

	boolean isValidateUserAndOldPassword(ChangePasswordModel changePasswordModel);
}
