package com.actisys.apigateway.client.impl;

import com.actisys.apigateway.client.AuthServiceClient;
import com.actisys.apigateway.config.ServiceProperties;
import com.actisys.apigateway.dto.AuthRequest;
import com.actisys.apigateway.dto.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthServiceClientImpl implements AuthServiceClient {

  private final WebClient.Builder webClientBuilder;
  private final ServiceProperties serviceProperties;

  @Override
  public Mono<JwtResponse> register(AuthRequest authRequest) {
    String uri = UriComponentsBuilder
        .fromUriString(serviceProperties.getAuth().getUrl())
        .path(serviceProperties.getAuth().getEndpoints().getRegister())
        .toUriString();

    return webClientBuilder.build()
        .post()
        .uri(uri)
        .bodyValue(authRequest)
        .retrieve()
        .bodyToMono(JwtResponse.class);
  }
}
