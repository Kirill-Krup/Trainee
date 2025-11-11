package com.actisys.apigateway.client;

import com.actisys.apigateway.dto.AuthRequest;
import com.actisys.apigateway.dto.JwtResponse;
import reactor.core.publisher.Mono;

public interface AuthServiceClient {

  Mono<JwtResponse> register(AuthRequest authRequest);
}
