package com.actisys.apigateway.controller;

import com.actisys.apigateway.config.ServiceProperties;
import com.actisys.apigateway.dto.JwtResponse;
import com.actisys.apigateway.dto.RegistrationRequest;
import com.actisys.apigateway.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController {

  private final WebClient.Builder webClientBuilder;

  private final ServiceProperties serviceProperties;

  @PostMapping
  public Mono<ResponseEntity<JwtResponse>> register(@RequestBody RegistrationRequest request) {
    WebClient webClient = webClientBuilder.build();
    String userServiceUrl = serviceProperties.getUserUrl();
    String authServiceUrl = serviceProperties.getAuthUrl();
    return webClient.post()
        .uri(userServiceUrl + "/api/v1/users/create")
        .bodyValue(request.getUserDTO())
        .retrieve()
        .bodyToMono(UserDTO.class)
        .flatMap(createdUser ->
            webClient.post()
                .uri(authServiceUrl + "/api/v1/auth/register")
                .bodyValue(request.getAuthRequest())
                .retrieve()
                .bodyToMono(JwtResponse.class)
                .map(jwtResponse -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(jwtResponse))
                .onErrorResume(authError ->
                    rollBackUser(webClient, userServiceUrl, createdUser.getId())
                        .then(Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(null)))
                )
        )
        .onErrorResume(userError ->
            Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null))
        );
  }

  private Mono<Void> rollBackUser(WebClient webClient, String userServiceUrl, Long userId) {
    return webClient.delete()
        .uri(userServiceUrl + "/api/v1/users/" + userId)
        .retrieve()
        .bodyToMono(Void.class)
        .onErrorResume(rollbackError -> {
          System.err.println("Rollback failed with userId = " + userId + ": " + rollbackError.getMessage());
          return Mono.empty();
        });
  }
}
