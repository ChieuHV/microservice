package com.userservice.grpc;

import com.userservice.model.User;
import com.userservice.repository.UserServiceRepository;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserGrpcServiceGrpc.UserGrpcServiceImplBase {

    private final UserServiceRepository userServiceRepository;

    @Override
    public void getUserById(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userServiceRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponse response = UserResponse.newBuilder()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
