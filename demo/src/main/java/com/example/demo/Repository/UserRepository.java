package com.example.demo.Repository;

import com.example.demo.Model.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.email = :email")
  User findUserByEmailJPQL(@Param("email") String email);

  @Query(value = "SELECT u.* FROM users u LEFT JOIN card_info c ON u.id = c.user_id WHERE u.email = :email", nativeQuery = true)
  User findUserByEmailNative(@Param("email") String email);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id = :id")
  User findUserById(@Param("id") Long id);

  @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.cards WHERE u.id IN :ids")
  List<User> findUsersByIdIn(@Param("ids") List<Long> ids);
}
