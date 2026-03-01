package com.orderservice.grpc;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GrpcUserClient {

    @GrpcClient("user-service")
    private UserGrpcServiceGrpc.UserGrpcServiceBlockingStub userStub;

    public UserResponse getUserById(String id) {
        GetUserRequest request = GetUserRequest.newBuilder().setUserId(id).build();
        return userStub.getUserById(request);
    }
}
