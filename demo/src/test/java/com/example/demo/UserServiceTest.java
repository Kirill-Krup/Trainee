package com.example.demo;

import com.example.demo.dto.UserDTO;
import com.example.demo.exception.UserNotDeletedException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.impl.UserServiceImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private UserServiceImpl userService;

  private User user;
  private UserDTO userDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    userDTO = new UserDTO(1L, null, null, null,"test@example.com", null);
  }

  @Test
  @DisplayName("Create user")
  void testCreateUser() {
    when(userMapper.toEntity(userDTO)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);
    UserDTO result = userService.createUser(userDTO);
    assertNotNull(result);
    assertEquals("test@example.com", result.getEmail());
    verify(userMapper).toEntity(userDTO);
    verify(userRepository).save(user);
    verify(userMapper).toDTO(user);
  }

  @Test
  @DisplayName("Get user by ID")
  void testGetUserById_Found() {
    when(userRepository.findUserById(1L)).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);
    Optional<UserDTO> result = userService.getUserById(1L);
    assertTrue(result.isPresent());
    assertEquals("test@example.com", result.get().getEmail());
    verify(userRepository).findUserById(1L);
    verify(userMapper).toDTO(user);
  }

  @Test
  @DisplayName("Get user by ID")
  void testGetUserById_NotFound() {
    when(userRepository.findUserById(2L)).thenReturn(null);
    Optional<UserDTO> result = userService.getUserById(2L);
    assertFalse(result.isPresent());
    verify(userRepository).findUserById(2L);
    verify(userMapper, never()).toDTO(any());
  }

  @Test
  @DisplayName("Update user")
  void testUpdateUser_NotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> userService.updateUser(99L, userDTO));
    verify(userRepository).findById(99L);
    verify(userRepository, never()).save(any());
  }

  @Test
  @DisplayName("Get user by email")
  void testGetUserByEmail_Found() {
    when(userRepository.findUserByEmailJPQL("test@example.com")).thenReturn(user);
    when(userMapper.toDTO(user)).thenReturn(userDTO);
    UserDTO result = userService.getUserByEmail("test@example.com");
    assertNotNull(result);
    assertEquals("test@example.com", result.getEmail());
    verify(userRepository).findUserByEmailJPQL("test@example.com");
    verify(userMapper).toDTO(user);
  }

  @Test
  @DisplayName("Update user")
  void testUpdateUser_Success() {
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setName("Old Name");
    existingUser.setEmail("old@example.com");
    User updatedUser = new User();
    updatedUser.setId(1L);
    updatedUser.setName("New Name");
    updatedUser.setEmail("new@example.com");
    UserDTO updatedDTO = new UserDTO(1L, "New Name", null,null,"new@example.com",null);
    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(updatedUser);
    when(userMapper.toDTO(updatedUser)).thenReturn(updatedDTO);
    UserDTO result = userService.updateUser(1L, updatedDTO);
    assertNotNull(result);
    assertEquals("New Name", result.getName());
    assertEquals("new@example.com", result.getEmail());
    verify(userRepository).findById(1L);
    verify(userRepository).save(any(User.class));
  }

  @Test
  @DisplayName("Delete user - Not Found")
  void testDeleteUser_NotFound() {
    when(userRepository.existsById(99L)).thenReturn(false);
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(UserNotDeletedException.class, () -> userService.deleteUser(99L));

    verify(userRepository).existsById(99L);
    verify(userRepository, never()).findById(99L);
    verify(userRepository, never()).deleteById(any());
  }

  @Test
  @DisplayName("Delete user - Success")
  void testDeleteUser_Success() {
    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setEmail("test@example.com");
    UserDTO expectedDTO = new UserDTO(1L, null, null, null, "test@example.com", null);
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
    when(userMapper.toDTO(existingUser)).thenReturn(expectedDTO);
    UserDTO result = userService.deleteUser(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("test@example.com", result.getEmail());
    verify(userRepository).existsById(1L);
    verify(userRepository).findById(1L);
    verify(userRepository).deleteById(1L);
    verify(userMapper).toDTO(existingUser);
  }


  @Test
  @DisplayName("Get users by IDS")
  void testGetUsersByIds() {
    User user2 = new User();
    user2.setId(2L);
    user2.setEmail("test2@example.com");
    UserDTO userDTO2 = new UserDTO(2L, null, null, null,"test2@example.com",null);
    when(userRepository.findUserById(1L)).thenReturn(user);
    when(userRepository.findUserById(2L)).thenReturn(user2);
    when(userMapper.toDTO(user)).thenReturn(userDTO);
    when(userMapper.toDTO(user2)).thenReturn(userDTO2);
    var result = userService.getUsersByIds(List.of(1L, 2L));
    assertEquals(2, result.size());
    assertEquals("test@example.com", result.get(0).getEmail());
    assertEquals("test2@example.com", result.get(1).getEmail());
    verify(userRepository, times(2)).findUserById(anyLong());
  }
}