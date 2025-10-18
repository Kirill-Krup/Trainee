package com.actisys.authservice.repository;

import com.actisys.authservice.model.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialsRepository extends JpaRepository<UserCredentials, String> {

}
