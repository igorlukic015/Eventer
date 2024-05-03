package com.eventer.admin.web.v1;

import com.eventer.admin.mapper.AdminMapper;
import com.eventer.admin.service.AdminService;
import com.eventer.admin.service.domain.Admin;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController implements ResultUnwrapper {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<?> getAdmins(final Pageable pageable, @RequestParam("searchTerm")Optional<String> searchTerm) {
        Result<Page<Admin>> result = this.adminService.getEventManagers(pageable, searchTerm.orElse(""));
        return this.okOrError(result, AdminMapper::toDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Result result = this.adminService.delete(id);
        return this.okOrError(result);
    }
}
