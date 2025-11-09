package com.example.bookswap.repository;

import com.example.bookswap.model.Role;
import com.example.bookswap.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(Role.USER);
        user = userRepository.save(user);
    }

    @Test
    void testFindByEmail() {
        Optional<User> found = userRepository.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByEmailAndDeletedFalse() {
        Optional<User> found = userRepository.findByEmailAndDeletedFalse("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testExistsByEmail() {
        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@example.com")).isFalse();
    }

    @Test
    void testFindByEmailAndDeletedFalse_WhenDeleted() {
        user.setDeleted(true);
        userRepository.save(user);

        Optional<User> found = userRepository.findByEmailAndDeletedFalse("test@example.com");
        assertThat(found).isEmpty();
    }
}

