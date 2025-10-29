package com.caerus.notificationservice.client;

// import org.springframework.cloud.openfeign.FeignClient;
import com.caerus.notificationservice.dto.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(fullName = "user-service", url = "${user.service.url}")
public interface UserServiceClient {

  @GetMapping("/api/v1/users/{id}")
  UserResponse getUserById(@PathVariable("id") Long id);
}
