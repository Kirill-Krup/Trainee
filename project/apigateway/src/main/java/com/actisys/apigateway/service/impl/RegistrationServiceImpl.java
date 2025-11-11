package com.actisys.apigateway.service.impl;

import com.actisys.apigateway.client.AuthServiceClient;
import com.actisys.apigateway.client.UserServiceClient;
import com.actisys.apigateway.dto.JwtResponse;
import com.actisys.apigateway.dto.RegistrationRequest;
import com.actisys.apigateway.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

  private final UserServiceClient userServiceClient;
  private final AuthServiceClient authServiceClient;

  public Mono<JwtResponse> register(RegistrationRequest request) {
    return userServiceClient.createUser(request.getUserDTO())
        .flatMap(createdUser ->
            authServiceClient.register(request.getAuthRequest())
                .onErrorResume(authError ->
                    rollBackUser(createdUser.getId())
                        .then(Mono.error(authError))
                )
        );
  }

  private Mono<Void> rollBackUser(Long userId) {
    return userServiceClient.deleteUser(userId)
        .onErrorResume(rollbackError -> {
          log.error("Rollback failed with userId = " + userId + ": " + rollbackError.getMessage());
          return Mono.empty();
        });
  }
}
