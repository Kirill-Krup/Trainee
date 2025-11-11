package com.actisys.apigateway.controller;

import com.actisys.apigateway.dto.JwtResponse;
import com.actisys.apigateway.dto.RegistrationRequest;
import com.actisys.apigateway.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping
  public Mono<ResponseEntity<JwtResponse>> register(@RequestBody RegistrationRequest request) {
    return registrationService.register(request)
        .map(jwtResponse -> ResponseEntity.status(HttpStatus.CREATED).body(jwtResponse))
        .onErrorResume(error ->
            Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null))
        );
  }
}
