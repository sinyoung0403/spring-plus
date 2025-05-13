package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@Auth AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getId(), userChangePasswordRequest);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> searchUser(@RequestParam String nickName) {
        return ResponseEntity.ok(userService.searchUserWithRedis(nickName));
    }

    @GetMapping("/dto/users")
    public ResponseEntity<List<UserResponse>> searchUserWithDto(@RequestParam String nickName) {
        return ResponseEntity.ok(userService.searchUserWithDto(nickName));
    }

    @GetMapping("/dsl/users")
    public ResponseEntity<List<UserResponse>> searchUserWithQueryDSL(@RequestParam String nickName) {
        return ResponseEntity.ok(userService.searchUserWithQueryDSL(nickName));
    }
}
