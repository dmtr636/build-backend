package com.kydas.build.auth.login;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "login_attempts")
public class LoginAttempt {
    @Id
    @Column(nullable = false, unique = true)
    private String ip;

    @Column(nullable = false)
    private int attempts;

    private Instant banExpirationTime;

    @Column(nullable = false)
    private Instant lastLoginAttempt = Instant.now();

    public LoginAttempt(String ip) {
        this.ip = ip;
        this.attempts = 0;
    }

    public LoginAttempt() {
        this.attempts = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginAttempt that)) return false;
        return ip != null && ip.equals(that.getIp());
    }

    @Override
    public int hashCode() {
        return ip != null ? ip.hashCode() : 0;
    }
}
