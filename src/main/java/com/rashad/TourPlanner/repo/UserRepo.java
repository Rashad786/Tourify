package com.rashad.TourPlanner.repo;

import com.rashad.TourPlanner.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u.role FROM User u WHERE u.email = :email")
    String findRoleByUsername(@Param("email") String email);

    @Query("select u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);
}
