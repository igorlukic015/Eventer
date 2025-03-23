package com.eventer.user.data.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "password_reset_code")
public class PasswordResetCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_reset_code_seq")
    @SequenceGenerator(name = "password_reset_code_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "userId", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "code", nullable = false)
    private String code;

    public PasswordResetCode() {
    }

    public PasswordResetCode(Long id, Long userId, String code) {
        this.id = id;
        this.userId = userId;
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PasswordResetCode that = (PasswordResetCode) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, code);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
