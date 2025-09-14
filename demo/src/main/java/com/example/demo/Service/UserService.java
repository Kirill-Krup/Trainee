package com.example.demo.Service;

import com.example.demo.DTO.UserDTO;
import com.example.demo.Exception.UserNotFoundException;
import com.example.demo.Mapper.UserMapper;
import com.example.demo.Model.User;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @CachePut(value = "users", key = "#result.email")
  public UserDTO createUser(UserDTO userDTO) {
    User entity = userMapper.toEntity(userDTO);
    User savedEntity = userRepository.save(entity);
    return userMapper.toDTO(savedEntity);
  }

  @Cacheable(value = "users", key = "#id")
  public Optional<UserDTO> getUserById(Long id) {
    return userRepository.findById(id).map(userMapper::toDTO);
  }

  @Cacheable(value = "users", key = "#email")
  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findUserByEmailJPQL(email);
    return userMapper.toDTO(user);
  }

  public List<UserDTO> getUsersByIds(List<Long> ids) {
    return ids.stream().map(this::getUserById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
  }

  @Transactional
  @Caching(put = {
      @CachePut(value = "users", key = "#id"),
      @CachePut(value = "users",key = "result.email", condition = "#result != null ")
  })
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
  @CacheEvict(value = "users", key = "#id")
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }


}
