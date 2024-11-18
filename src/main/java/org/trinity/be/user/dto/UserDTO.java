package org.trinity.be.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userNo;
    private String accountNo;
    private String id;
    private String password;
    private String name;
    private Date createdAt;
    private Date updatedAt;

}
