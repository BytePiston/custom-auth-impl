package com.cactus.springsecurity.client.repository;

import com.cactus.springsecurity.client.entity.ResetPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, String> {

	Optional<ResetPasswordToken> findByToken(String token);

}
