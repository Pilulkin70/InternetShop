package ua.garmash.internetshop.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.model.User;

import java.util.List;

public interface UserService extends UserDetailsService { // security
	boolean save(UserDto userDTO);
	void save(User user);
	List<UserDto> getAll();
	User findByName(String name);
	void updateProfile(UserDto userDTO);
	void delUserById(Long id);
	UserDto getById(Long id);
	UserDto getByName(String name);
}
