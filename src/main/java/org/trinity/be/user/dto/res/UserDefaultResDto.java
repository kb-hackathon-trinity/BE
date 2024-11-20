package org.trinity.be.user.dto.res;

import lombok.Getter;
import lombok.ToString;
import org.trinity.be.user.domain.User;

import java.util.Date;

@Getter
@ToString
public class UserDefaultResDto {

    private Integer userNo;
    private String accountNo;
    private String id;;
    private String name;
    private Date createdAt;
    private Date updatedAt;


    public UserDefaultResDto(User user) {
        this.userNo = user.getUserNo();
        this.accountNo = user.getAccountNo();
        this.id = user.getId();
        this.name = user.getName();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
