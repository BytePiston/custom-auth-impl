package com.cactus.resourceserver.service;

import com.cactus.resourceserver.entity.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {

	Optional<User> fetchUserByEmail(String email);

	List<User> fetchAllUsers();

}
