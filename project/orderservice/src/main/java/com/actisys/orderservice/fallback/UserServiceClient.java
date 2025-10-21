package com.actisys.orderservice.fallback;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice", url = "${user.service.url}")
public interface UserServiceClient {

  @GetMapping("/api/v1/users/get-id-by-email/{email}")
  Long getUserIdByEmail(@PathVariable("email") String email);
}
