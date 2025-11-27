package com.actisys.authservice.repository;

import com.actisys.authservice.model.UserCredentials;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {

  Optional<UserCredentials> findByLogin(String login);
}
