package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.UserVoucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVoucherDTOResponse {
    private LocalDateTime expireDate;
    private Integer status;
    private LocalDateTime usedAt;
    private String userName;
    private String codeVoucher;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This function customizes the output userVoucher information, including
     * expireDate, status and usedAt as a JSON file.
     * @param userVoucher
     * @author HoangVu
     * @since 1.0
     */
    public UserVoucherDTOResponse(UserVoucher userVoucher) {
        this.expireDate = userVoucher.getExpireDate();
        this.status = userVoucher.getStatus();
        if (ObjectUtils.isEmpty(userVoucher.getUsedAt())) {
            this.usedAt = userVoucher.getUsedAt();
        }
        if (ObjectUtils.isEmpty(userVoucher.getUser().getUserName())) {
            this.userName = userVoucher.getUser().getUserName();
        }
        if (ObjectUtils.isEmpty(userVoucher.getVoucher().getCode())) {
            this.codeVoucher = userVoucher.getVoucher().getCode();
        }
    }
}
