package com.actisys.userservice.service.impl;

import com.actisys.userservice.dto.UserDTO;
import com.actisys.userservice.exception.UserNotFoundException;
import com.actisys.userservice.mapper.UserMapper;
import com.actisys.userservice.model.CardInfo;
import com.actisys.userservice.model.User;
import com.actisys.userservice.repository.UserRepository;
import com.actisys.userservice.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserDTO createUser(UserDTO userDTO) {
    User entity = userMapper.toEntity(userDTO);
    if (entity.getCards() != null) {
      for (CardInfo card : entity.getCards()) {
        card.setUser(entity);
      }
    }
    User savedEntity = userRepository.save(entity);
    return userMapper.toDTO(savedEntity);
  }

  @Cacheable(value = "users", key = "#id")
  public Optional<UserDTO> getUserById(Long id) {
    return Optional.ofNullable(userRepository.findUserById(id)).map(userMapper::toDTO);
  }

  @Override
  @Cacheable(value = "users", key = "#email")
  public UserDTO getUserByEmail(String email) {
    User user = userRepository.findUserByEmailJPQL(email);
    if(user == null) {
      throw new UserNotFoundException(email);
    }
    return userMapper.toDTO(user);
  }

  @Override
  public List<UserDTO> getUsersByIds(List<Long> ids) {
    return ids.stream().map(this::getUserById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
  }

  @Override
  @Transactional
  @Caching(put = {
      @CachePut(value = "users", key = "#id"),
      @CachePut(value = "users",key = "#result.email", condition = "#result != null ")
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

  @Override
  @Transactional
  @Caching(evict = {
      @CacheEvict(value = "users", key = "#id"),
      @CacheEvict(value = "users", key = "#result.email")
  })
  public UserDTO deleteUser(Long id) {
    if(!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    UserDTO userDTO = userMapper.toDTO(user);
    userRepository.deleteById(id);
    return userDTO;
  }


}