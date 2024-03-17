package com.eventer.admin.web.v1;

import com.eventer.admin.contracts.auth.AuthenticationResponse;
import com.eventer.admin.mapper.AdminMapper;
import com.eventer.admin.service.AdminService;
import com.eventer.admin.service.domain.Admin;
import com.github.igorlukic015.resulter.Result;
import com.eventer.admin.web.dto.auth.LoginDTO;
import com.eventer.admin.web.dto.auth.RegisterDTO;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController implements ResultUnwrapper {
    private final AdminService adminService;

    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@RequestBody LoginDTO loginDTO) {
        Result<AuthenticationResponse> result =
                this.adminService.authenticate(AdminMapper.toRequest(loginDTO));
        return this.okOrError(result, AdminMapper::toDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        Result<Admin> result = this.adminService.register(AdminMapper.toRequest(registerDTO));
        return this.okOrError(result, AdminMapper::toDTO);
    }
}
