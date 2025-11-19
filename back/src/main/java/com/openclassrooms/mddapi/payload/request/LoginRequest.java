package com.openclassrooms.mddapi.payload.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
	@NotBlank
	private String emailOrUsername;

	@NotBlank
	private String password;
}