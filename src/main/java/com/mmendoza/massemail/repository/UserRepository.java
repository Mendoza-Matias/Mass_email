package com.mmendoza.massemail.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mmendoza.massemail.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users AS u WHERE u.state = 'PENDING' LIMIT :numberOfRecord", nativeQuery = true)
    List<User> getRecords(@Param("numberOfRecord") Integer numberOfRecords);
}
