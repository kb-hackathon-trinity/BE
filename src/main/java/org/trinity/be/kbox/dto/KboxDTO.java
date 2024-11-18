package org.trinity.be.kbox.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KboxDTO {
    private String kboxNo;
    private String accountNo;
    private Integer modeNo;
    private String kboxName;
    private Integer targetAmount;
    private Date expirationDate;
    private Integer amount;
    private Date transferDate;
    private Integer balance;
    private Date createdAt;
    private Date updatedAt;
}
