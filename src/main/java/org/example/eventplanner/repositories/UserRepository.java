package org.example.eventplanner.repositories;

import org.example.eventplanner.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
boolean existsByEmail(String email);
User findByEmail(String email);
}
