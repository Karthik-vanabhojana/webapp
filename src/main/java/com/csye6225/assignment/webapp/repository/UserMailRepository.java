package com.csye6225.assignment.webapp.repository;

import com.csye6225.assignment.webapp.entity.User;
import com.csye6225.assignment.webapp.entity.UserEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserMailRepository extends JpaRepository<UserEmail, UUID> {
    Optional<UserEmail> findByUser(User user);
    Optional<UserEmail> findByToken(String token);

}
