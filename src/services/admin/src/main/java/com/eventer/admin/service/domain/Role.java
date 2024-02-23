package com.eventer.admin.service.domain;

import com.eventer.admin.utils.Result;
import com.eventer.admin.utils.ResultErrorMessages;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Role {
    public static final Role EVENT_MANAGER = new Role(1, "EVENT_MANAGER");
    public static final Role ADMINISTRATOR = new Role(2, "ADMINISTRATOR");
    private static final Set<Role> allRoles = Set.of(EVENT_MANAGER, ADMINISTRATOR);

    private final int id;
    private final String name;

    private Role(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Result<Role> create(String name) {
        Optional<Role> foundRole =
                allRoles.stream().filter(role -> Objects.equals(role.getName(), name)).findFirst();

        return foundRole.map(Result::success).orElseGet(() -> Result.invalid(ResultErrorMessages.invalidRole));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
