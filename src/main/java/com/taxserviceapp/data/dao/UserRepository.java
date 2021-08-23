package com.taxserviceapp.data.dao;

import com.taxserviceapp.data.entity.User;
import com.taxserviceapp.data.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<List<User>> findAllByUserRoleEquals(UserRole userRole);

    Integer countAllByUserRole(UserRole userRole);

}
