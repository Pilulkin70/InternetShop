package ua.garmash.internetshop.controllers.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.garmash.internetshop.repository.UserRepository;
import ua.garmash.internetshop.dto.UserDto;
import ua.garmash.internetshop.model.User;
import ua.garmash.internetshop.service.UserServiceImpl;

import java.util.UUID;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userRepository = Mockito.mock(UserRepository.class);

        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void checkFindByName() {
        String name = "petr";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername(name);

        Mockito.when(userRepository.findFirstByUsername(Mockito.anyString())).thenReturn(expectedUser);

        User actualUser = userService.findByName(name);

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void checkFindByNameExact() {
        String name = "petr";
        User expectedUser = new User();
        expectedUser.setId(1L);
        expectedUser.setUsername(name);

        Mockito.when(userRepository.findFirstByUsername(Mockito.eq(name))).thenReturn(expectedUser);

        User actualUser = userService.findByName(name);
        User rndUser = userService.findByName(UUID.randomUUID().toString());

        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(expectedUser, actualUser);

        Assertions.assertNull(rndUser);
    }

    @Test
    void checkSaveIncorrectPassword() {
        UserDto userDto = new UserDto();
        userDto.setPassword("password");
        userDto.setMatchingPassword("another");

        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() {
                userService.save(userDto);
            }
        });
    }

    @Test
    void checkSave() {
        UserDto userDto = new UserDto();
        userDto.setUsername("user");
        userDto.setEmail("email");
        userDto.setPassword("password");
        userDto.setMatchingPassword("password");

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("password");

        boolean result = userService.save(userDto);

        Assertions.assertTrue(result);
        Mockito.verify(passwordEncoder).encode(Mockito.anyString());
        Mockito.verify(userRepository).save(Mockito.any());
    }
}
