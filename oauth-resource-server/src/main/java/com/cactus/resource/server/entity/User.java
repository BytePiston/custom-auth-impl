package com.cactus.resource.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	@Column(length = 60)
	private String password;

	private String role;

	private boolean enabled = false;

}
