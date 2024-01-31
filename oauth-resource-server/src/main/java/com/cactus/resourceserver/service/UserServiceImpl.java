package com.cactus.resourceserver.service;

import com.cactus.resourceserver.entity.User;
import com.cactus.resourceserver.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<User> fetchUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<User> fetchAllUsers() {
		return userRepository.findAll();
	}

}
