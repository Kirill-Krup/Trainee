package com.actisys.apigateway.client.impl;

import com.actisys.apigateway.client.UserServiceClient;
import com.actisys.apigateway.config.ServiceProperties;
import com.actisys.apigateway.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserServiceClientImpl implements UserServiceClient {

  private final WebClient.Builder webClientBuilder;
  private final ServiceProperties serviceProperties;

  @Override
  public Mono<UserDTO> createUser(UserDTO userDTO) {
    String uri = UriComponentsBuilder
        .fromUriString(serviceProperties.getUser().getUrl())
        .path(serviceProperties.getUser().getEndpoints().getCreate())
        .toUriString();

    return webClientBuilder.build()
        .post()
        .uri(uri)
        .bodyValue(userDTO)
        .retrieve()
        .bodyToMono(UserDTO.class);
  }

  @Override
  public Mono<Void> deleteUser(Long userId) {
    String uri = UriComponentsBuilder
        .fromUriString(serviceProperties.getUser().getUrl())
        .path(serviceProperties.getUser().getEndpoints().getDelete())
        .buildAndExpand(userId)
        .toUriString();

    return webClientBuilder.build()
        .delete()
        .uri(uri)
        .retrieve()
        .bodyToMono(Void.class);
  }
}
