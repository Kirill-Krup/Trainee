package com.example.demo.Service;

import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  public User createUser(User user) {
    return userRepository.save(user);
  }

  public Optional<User> getUserById(Long id){
    return userRepository.findById(id);
  }

  public User getUserByEmail(String email){
    return userRepository.findUserByEmailJPQL(email);
  }

  public List<User> getUsersByIds(List<Long> ids){
    return userRepository.findUsersByIdIn(ids);
  }

  @Transactional
  public User updateUser(Long id, User updated) {
    return userRepository.findById(id)
        .map(user -> {
          user.setName(updated.getName());
          user.setSurname(updated.getSurname());
          user.setBirthDate(updated.getBirthDate());
          user.setEmail(updated.getEmail());
          return userRepository.save(user);
        })
        .orElseThrow(() -> new RuntimeException("User "+ id +" not found "));
  }

  @Transactional
  public void deleteUser(Long id){
    userRepository.deleteById(id);
  }
}
