package com.example.nms.controller;

import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.nms.constants.MessageConstants;
import com.example.nms.dto.UserDTO;
import com.example.nms.entity.User;
import com.example.nms.exception.user.InvalidUserException;
import com.example.nms.exception.user.UserIdNotFoundException;
import com.example.nms.mapper.UserMapper;
import com.example.nms.service.auth.AuthService;
import com.example.nms.service.note.NoteService;
import com.example.nms.service.user.UserService;
import com.example.nms.validator.UserDTOValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final NoteService noteService;
    private final AuthService authService;
    private final UserDTOValidator userDTOValidator;

    @GetMapping
    public ResponseEntity<UserDTO> getUserProfile() {
        User user = authService.getAuthenticatedUser();

        UserDTO userDTO = userMapper.toDTO(user);
        userDTO.setNumberOfNotes(noteService.getUserNotesCount(user));

        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody @Valid UserDTO updatedUserDTO, BindingResult br) {
        userDTOValidator.validate(updatedUserDTO, br);
        if (br.hasErrors())
            throw new InvalidUserException(
                    br.getFieldErrors().stream().map(err -> err.getField() + " - " + err.getDefaultMessage())
                            .collect(Collectors.joining("; ")));

        User updatedUser = userService.updateEntityFromDTO(authService.getAuthenticatedUser(), updatedUserDTO);
        UserDTO userDTO = userMapper.toDTO(updatedUser);
        userDTO.setNumberOfNotes(noteService.getUserNotesCount(updatedUser));

        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser() {

        UUID userUUID = authService.getAuthenticatedUser().getUuid();

        if (userService.delete(userUUID))
            return ResponseEntity.ok(MessageConstants.USER_DELETED);
        throw new UserIdNotFoundException(
                String.format(MessageConstants.USER_ID_NOT_FOUND, userUUID));
    }
}
