package com.doksanbir.ratelimiter.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ForeignKey;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    private LocalDateTime expirationDate;
    private boolean isLocked;
    private LocalDateTime credentialsExpirationDate;
    private boolean isEnabled;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            foreignKey = @ForeignKey(name = "FK_USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "FK_ROLE_ID")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private Set<RateLimitConfig> rateLimitConfigs = new HashSet<>();
}
