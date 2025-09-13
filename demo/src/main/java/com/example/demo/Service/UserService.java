package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Mapper.UserMapperImpl;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.beans.Transient;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }


  public UserDTO createUser(UserDTO userDTO) {
    User entity = userMapper.toEntity(userDTO);
    User savedEntity = userRepository.save(entity);
    return userMapper.toDTO(savedEntity);
  }

  public Optional<UserDTO> getUserById(Long id) {
    return userRepository.findById(id).map(userMapper::toDTO);
  }

  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findUserByEmailJPQL(email);
    return userMapper.toDTO(user);
  }

  public List<UserDTO> getUsersByIds(List<Long> ids) {
    List<User> users = userRepository.findUsersByIdIn(ids);
    return users.stream().map(userMapper::toDTO).collect(Collectors.toList());
  }

  @Transactional
  public UserDTO updateUser(Long id, UserDTO updated) {
    User updatedEntity = userRepository.findById(id).map(user -> {
      user.setName(updated.getName());
      user.setSurname(updated.getSurname());
      user.setBirthDate(updated.getBirthDate());
      user.setEmail(updated.getEmail());
      return user;
    }).orElseThrow(() -> new UserNotFoundException(id));
    User savedEntity = userRepository.save(updatedEntity);
    return userMapper.toDTO(savedEntity);
  }

  @Transactional
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }
}
