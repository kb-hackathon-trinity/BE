package org.trinity.be.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private String accountNo;
    private Integer balance;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}
