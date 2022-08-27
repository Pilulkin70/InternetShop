package ua.garmash.internetshop.controllers.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ua.garmash.internetshop.dao.UserRepository;
import ua.garmash.internetshop.model.Role;
import ua.garmash.internetshop.model.User;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@SqlGroup({@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:initUsers.sql")})
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void checkFindByName() {
        //have
        User user = new User();
        user.setUsername("TestUser");
        user.setPassword("password");
        user.setEmail("test-email@mail.ru");

        entityManager.persist(user);

        //execute
        User actualUser = userRepository.findFirstByUsername("TestUser");

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(user.getUsername(), actualUser.getUsername());
        Assertions.assertEquals(user.getPassword(), actualUser.getPassword());
        Assertions.assertEquals(user.getEmail(), actualUser.getEmail());

    }

    @Test
    void checkFindByNameAfterSql() {
        //execute
        User actualUser = userRepository.findFirstByUsername("admin");

        //check
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals(5, actualUser.getId());
        Assertions.assertEquals("admin", actualUser.getUsername());
        Assertions.assertEquals("adminpass", actualUser.getPassword());
        Assertions.assertEquals("admin@mail.ru", actualUser.getEmail());
        Assertions.assertEquals(Role.ADMIN, actualUser.getRole());

    }
}
