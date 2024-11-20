package org.trinity.be.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    private Integer userNo;
    private String accountNo;
    private String id;
    private String password;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    private List<UserRole> userRoles = new ArrayList<>();

    public void addMemberRole(List<UserRole> memberRoles) {
        this.userRoles = userRoles;
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.userRoles.stream()
                .map(memberRole ->
                        new SimpleGrantedAuthority(memberRole.getRole()))
                .collect(Collectors.toList());
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        for(UserRole userRole : this.userRoles)
            roles.add(userRole.getRole());

        return roles;
    }
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
