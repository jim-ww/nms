package com.notehub.notehub.controllers;

import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.notehub.notehub.dto.AuthResponseDTO;
import com.notehub.notehub.dto.LoginDTO;
import com.notehub.notehub.dto.RegisterDTO;
import com.notehub.notehub.services.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/login")
    public AuthResponseDTO loginUser(@RequestBody @Valid LoginDTO loginDTO, BindingResult br) {

        if (br.hasErrors())
            throw new BadCredentialsException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return authenticationService.loginUser(loginDTO);
    }

    @PostMapping("/register")
    public AuthResponseDTO registerUser(@RequestBody @Valid RegisterDTO registerDTO, BindingResult br) {

        if (br.hasErrors())
            throw new BadCredentialsException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        return authenticationService.registerUser(registerDTO);
    }

}
