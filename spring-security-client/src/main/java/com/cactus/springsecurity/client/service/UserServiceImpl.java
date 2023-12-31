package com.cactus.springsecurity.client.service;

import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.entity.VerificationToken;
import com.cactus.springsecurity.client.model.UserModel;
import com.cactus.springsecurity.client.repository.UserRepository;
import com.cactus.springsecurity.client.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final VerificationTokenRepository verificationTokenRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
			VerificationTokenRepository verificationTokenRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.verificationTokenRepository = verificationTokenRepository;
	}

	@Override
	public User registerUser(UserModel userModel) {
		User user = User.builder()
			.firstName(userModel.getFirstName())
			.lastName(userModel.getLastName())
			.email(userModel.getEmail())
			.role(userModel.getRole())
			.password(passwordEncoder.encode(userModel.getPassword()))
			.build();

		userRepository.save(user);
		return user;
	}

	@Override
	public void persistVerificationToken(String token, User user) {
		VerificationToken verificationToken = new VerificationToken(token, user);
		verificationTokenRepository.save(verificationToken);
	}

}
