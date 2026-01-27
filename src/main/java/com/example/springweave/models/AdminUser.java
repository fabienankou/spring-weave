package com.example.springweave.models;

import com.example.springweave.models.enums.AdminRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "admin_users")
@Getter @Setter

public class AdminUser extends AbstractUserAccount {

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdminRole role;

    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled;

    @JsonIgnore
    @Column(name = "two_factor_secret")
    private String twoFactorSecret;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> permissions;
}