package ua.garmash.internetshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.garmash.internetshop.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByUsername(String name);

    User findFirstById(Long id);
}
