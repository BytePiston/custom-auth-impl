package com.cactus.springsecurity.client.model;

import com.cactus.springsecurity.client.validator.ValidateEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

	@NotBlank(message = "First Name Can Not Be Blank!")
	@NotNull(message = "First Name Can Not Be Empty!")
	private String firstName;

	private String lastName;

	@NotBlank(message = "Email Address Can Not Be Blank!")
	@NotNull(message = "Email Address Can Not Be Empty!")
	@Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
	private String email;

	@NotBlank(message = "Password Can Not Be Blank!")
	@NotNull(message = "Password Can Not Be Empty!")
	private String password;

	private String matchingPassword;

	@NotBlank(message = "Role Can Not Be Blank!")
	@NotNull(message = "Role Can Not Be Empty!")
	@ValidateEnum(acceptedValues = UserRole.class, message = "Invalid User Role")
	private String role;

}
