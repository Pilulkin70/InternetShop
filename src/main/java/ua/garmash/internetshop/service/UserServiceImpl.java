package ua.garmash.internetshop.service;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.garmash.internetshop.dao.UserRepository;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.mapper.UserMapper;
import ua.garmash.internetshop.model.Role;
import ua.garmash.internetshop.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    private final UserMapper mapper = UserMapper.MAPPER;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> getAll() {
        return mapper.fromUserList(userRepository.findAll());
/*        return userRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());*/
    }

    @Override
    @Transactional
    public boolean save(UserDto userDto) {
        if (!Objects.equals(userDto.getPassword(), userDto.getMatchingPassword())) {
            throw new RuntimeException("Password is not equal");
        }
/*        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .email(userDto.getEmail())
                .role(Role.CLIENT)
                .build();
        userRepository.save(user);*/
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setRole(Role.CLIENT);
        userRepository.save(mapper.toUser(userDto));
        return true;
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findFirstByUsername(name);
    }

    @Override
    @Transactional
    public void updateProfile(UserDto dto) {
        User savedUser = userRepository.findFirstById(dto.getId());
        if (savedUser == null) {
            throw new RuntimeException("User not found by name " + dto.getUsername());
        }

        boolean changed = false;
        if (!dto.getUsername().equals(savedUser.getUsername())) {
            if (userRepository.findFirstByUsername(dto.getUsername()) != null) {
                throw new RuntimeException("This name is already in use " + dto.getUsername());
            }
            savedUser.setUsername(dto.getUsername());
            changed = true;
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            savedUser.setPassword(passwordEncoder.encode(dto.getPassword()));
            changed = true;
        }
        if (!dto.getEmail().equals(savedUser.getEmail())) {
            savedUser.setEmail(dto.getEmail());
            changed = true;
        }
        if (!dto.getPhone().equals(savedUser.getPhone())) {
            savedUser.setPhone(dto.getPhone());
            changed = true;
        }
        if (!dto.getFirstName().equals(savedUser.getFirstName())) {
            savedUser.setFirstName(dto.getFirstName());
            changed = true;
        }
        if (!dto.getLastName().equals(savedUser.getLastName())) {
            savedUser.setLastName(dto.getLastName());
            changed = true;
        }
        if (dto.getAge() != savedUser.getAge()) {
            savedUser.setAge(dto.getAge());
            changed = true;
        }
        if (!dto.getCity().equals(savedUser.getCity())) {
            savedUser.setCity(dto.getCity());
            changed = true;
        }
        if (!(dto.getRole() == null) && !dto.getRole().equals(savedUser.getRole())) {
            savedUser.setRole(dto.getRole());
            changed = true;
        }

        if (changed) {
            userRepository.save(savedUser);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with name: " + username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                roles);
    }

/*    private UserDto toDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }*/

    @Override
    @Transactional
    public void delUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        return mapper.fromUser(userRepository.findById(id).get());
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public UserDto getUserDtoByName(String name) {
        return mapper.fromUser(userRepository.findFirstByUsername(name));
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findFirstByUsername(name);
    }
}
