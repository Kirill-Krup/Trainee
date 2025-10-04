package com.actisys.userservice.service;

import com.actisys.userservice.dto.UserDTO;
import java.util.List;
import java.util.Optional;

public interface UserService {

  UserDTO createUser(UserDTO userDTO);

  Optional<UserDTO> getUserById(Long id);

  UserDTO getUserByEmail(String email);

  List<UserDTO> getUsersByIds(List<Long> ids);

  UserDTO updateUser(Long id, UserDTO updated);

  UserDTO deleteUser(Long id);

}