package org.trinity.be.user.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.trinity.be.user.domain.type.Role;

@Getter
@ToString
@NoArgsConstructor
public class UserRole {

    private Long id;

    private Long memberID;

    private String role;


    @Builder
    public UserRole(Long id, Role role) {
        this.memberID = id;
        this.role = role.name();
    }
}
