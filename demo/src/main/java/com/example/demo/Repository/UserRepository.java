package com.example.demo.Repository;

import com.example.demo.Model.User;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.email = :email")
  User findUserByEmailJPQL(@Param("email") String email);

  @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
  User findUserByEmailNative(@Param("email") String email);

  User findUserById(Long id);

  List<User> findUsersByIdIn(Collection<Long> ids);
}
