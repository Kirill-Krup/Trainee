package com.actisys.userservice.mapper;

import com.actisys.userservice.dto.UserDTO;
import com.actisys.userservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", uses = CardInfoMapper.class)
public interface UserMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "cards", source = "cards")
  UserDTO toDTO(User user);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "cards", source = "cards")
  User toEntity(UserDTO userDTO);
}