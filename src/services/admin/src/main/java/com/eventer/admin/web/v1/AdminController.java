package com.eventer.admin.web.v1;

import com.eventer.admin.service.AdminService;
import com.github.cigor99.resulter.ResultUnwrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController implements ResultUnwrapper {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

}
