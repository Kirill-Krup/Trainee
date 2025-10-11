package com.actisys.orderservice.fallback;

import com.actisys.orderservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "userservice", url = "${user.service.url}")
public interface UserServiceClient {

  @GetMapping("/api/v1/users/{userId}")
  UserDTO getUserById(@PathVariable("userId") String userId);

  @GetMapping("/api/v1/users/{email}")
  UserDTO getUserByEmail(@PathVariable("email") String email);
}
