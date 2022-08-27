package ua.garmash.internetshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.garmash.internetshop.model.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	private Long id;
	private String username;
	private String password;
	private String matchingPassword;
	private String email;
	private String firstName;
	private String lastName;
	private String city;
	private String gender;
	private Integer age;
	private Role role;
}
