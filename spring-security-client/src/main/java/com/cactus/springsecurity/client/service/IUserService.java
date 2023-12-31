package com.cactus.springsecurity.client.service;

import com.cactus.springsecurity.client.entity.User;
import com.cactus.springsecurity.client.model.UserModel;

public interface IUserService {

	User registerUser(UserModel userModel);

	void persistVerificationToken(String token, User user);

}
