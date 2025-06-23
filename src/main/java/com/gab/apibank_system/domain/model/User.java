package com.gab.apibank_system.domain.model;

import com.gab.apibank_system.domain.enums.AccountStatus;
import com.gab.apibank_system.domain.enums.UserRole;
import com.gab.apibank_system.domain.request.UserRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Table(name = "users")
@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String email;
    private String password;
    private String cellphone;
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    public User(String firstName, String lastName, String email, String password, String cellphone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.cellphone = cellphone;
        this.role = UserRole.USER;
        this.status = AccountStatus.ACTIVE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return getFirstName() + " " + getLastName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == AccountStatus.ACTIVE;
    }

    public static User fromDto(UserRequest registerDTO, String encryptedPass) {
        User user = new User();
        user.firstName = registerDTO.firstName();
        user.lastName = registerDTO.lastName();
        user.email = registerDTO.email();
        user.password = encryptedPass;
        user.cellphone = registerDTO.cellphone();
        user.status = AccountStatus.ACTIVE;
        user.role = UserRole.USER;
        return user;
    }
}
