package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "card_info")
public class CardInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String number;

  private String holder;

  private Timestamp expirationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
