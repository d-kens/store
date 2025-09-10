package com.omoke.store.users;

import com.omoke.store.dtos.ChangePasswordRequest;
import com.omoke.store.dtos.ErrorDto;
import com.omoke.store.dtos.RegisterUserRequest;
import com.omoke.store.dtos.UpdateUserRequest;
import com.omoke.store.exceptions.EmailAlreadyExistException;
import com.omoke.store.exceptions.PasswordMismatchException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;



@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Tag(name = "Users")
public class UsersController {
    private final UsersService usersService;

    @GetMapping()
    public List<UserDto> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort") String sort) {
        return usersService.findAll(sort);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        return usersService.findUserById(userId);
    }

    @PostMapping()
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ) {
        var userDto = usersService.createUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable(name = "userId") Long userId,
            @RequestBody UpdateUserRequest request
    ) {
        return usersService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
       usersService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest request
    ) {
        usersService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorDto> handleEmailAlreadyExistException(EmailAlreadyExistException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(exception.getMessage())
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorDto> handlePasswordMismatchException(PasswordMismatchException exception) {
        return ResponseEntity.badRequest().body(
                new ErrorDto(exception.getMessage())
        );
    }
}