package com.orderservice.openfeignclient;

import com.orderservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/v1/users/find-id/{id}")
    UserDTO getUserById(@PathVariable("id") String id);
}
