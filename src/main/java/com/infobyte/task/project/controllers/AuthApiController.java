//package com.infobyte.task.project.controllers;
//
//import com.infobyte.task.project.dtos.LoginRequest;
//import com.infobyte.task.project.dtos.LoginResponse;
//import com.infobyte.task.project.services.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class AuthApiController {
//
//    @Autowired private AuthService authService;
//
//    @PostMapping("/api/auth/login")
//    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
//        LoginResponse response = authService.login(request);
//        return ResponseEntity.ok()
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken())
//                .body(response);
//    }
//}