package com.actisys.apigateway.client;

import com.actisys.apigateway.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserServiceClient {

  Mono<UserDTO> createUser(UserDTO userDTO);

  Mono<Void> deleteUser(Long userId);
}
