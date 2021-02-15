package com.aleksandrchuyko.springbooterbroad.repository;


import com.aleksandrchuyko.springbooterbroad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
    User findUserById(Long id);
}
