package com.actisys.apigateway.service;

import com.actisys.apigateway.dto.JwtResponse;
import com.actisys.apigateway.dto.RegistrationRequest;
import reactor.core.publisher.Mono;

public interface RegistrationService {
  Mono<JwtResponse> register(RegistrationRequest request);
}
