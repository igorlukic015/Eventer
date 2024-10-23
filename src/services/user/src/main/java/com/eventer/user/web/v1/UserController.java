package com.eventer.user.web.v1;

import com.eventer.user.mapper.UserMapper;
import com.eventer.user.service.ImageHostService;
import com.eventer.user.service.UserService;
import com.eventer.user.service.domain.User;
import com.eventer.user.web.dto.user.PasswordResetDTO;
import com.eventer.user.web.dto.user.UpdateProfileDTO;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public class UserController implements ResultUnwrapper {
    private final UserService userService;
    private final ImageHostService imageHostService;

    public UserController(UserService userService, ImageHostService imageHostService1) {
        this.userService = userService;
        this.imageHostService = imageHostService1;
    }

    @GetMapping
    public ResponseEntity<?> getUserData(@AuthenticationPrincipal UserDetails userDetails) {
        Result<User> result = this.userService.getProfileData(userDetails.getUsername());
        return this.okOrError(result, UserMapper::toDTO);
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestParam("image") List<MultipartFile> images) {
        Set<String> response = this.imageHostService.saveAllImages(images);
        Result<User> result = this.userService.updateProfileImage(response);
        return this.okOrError(result, UserMapper::toDTO);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateProfileDTO dto) {
        Result<User> result = this.userService.updateProfile(UserMapper.toRequest(dto));
        return this.okOrError(result, UserMapper::toDTO);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getUsers() {
        Result<Set<User>> result = this.userService.getAll();
        return this.okOrError(result, UserMapper::toDTOSet);
    }

    @PatchMapping("/request-reset/{email}")
    public ResponseEntity<?> requestPasswordReset(@PathVariable("email") String email) {
        Result result = this.userService.requestPasswordReset(email);
        return this.okOrError(result);
    }

    @PatchMapping("/reset")
    public ResponseEntity<?> passwordReset(@RequestBody PasswordResetDTO dto) {
        Result result = this.userService.resetPassword(UserMapper.toRequest(dto));
        return this.okOrError(result);
    }
}
