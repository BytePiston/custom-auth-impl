package com.cactus.springsecurity.client.repository;

import com.cactus.springsecurity.client.entity.RegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<RegistrationToken, String> {

	Optional<RegistrationToken> findByToken(String token);

}
