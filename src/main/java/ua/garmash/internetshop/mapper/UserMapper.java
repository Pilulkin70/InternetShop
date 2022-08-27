package ua.garmash.internetshop.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.model.User;

import java.util.List;

@Mapper
public interface UserMapper {
	UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

	User toUser(UserDto dto);

	@InheritInverseConfiguration
	UserDto fromUser(User user);

	List<User> toUserList(List<UserDto> userDtos);

	List<UserDto> fromUserList(List<User> users);

}