package com.actisys.authservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "credentials")
public class UserCredentials {

  @Id
  private String login;

  @Column(name = "password_hash")
  private String hashedPassword;

  @Column(name = "refresh_token")
  private String refreshToken;
}
