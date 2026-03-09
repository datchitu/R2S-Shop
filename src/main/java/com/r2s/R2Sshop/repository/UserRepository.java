package com.r2s.R2Sshop.repository;

import com.r2s.R2Sshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    List<User> findByNameContaining(String name);

}
