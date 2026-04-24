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
public class UserVoucherSimpleDTOResponse {
    private LocalDateTime expireDate;
    private Integer status;
    private LocalDateTime usedAt;
    private String codeVoucher;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This method customizes the output userVoucher information, including
     * expireDate, status, usedAt and codeVoucher as a JSON file.
     * @param userVoucher
     * @author HoangVu
     * @since 1.0
     */
    public UserVoucherSimpleDTOResponse(UserVoucher userVoucher) {
        this.expireDate = userVoucher.getExpireDate();
        this.status = userVoucher.getStatus();
        if (!ObjectUtils.isEmpty(userVoucher.getUsedAt())) {
            this.usedAt = userVoucher.getUsedAt();
        }
        if (!ObjectUtils.isEmpty(userVoucher.getVoucher().getCode())) {
            this.codeVoucher = userVoucher.getVoucher().getCode();
        }
    }
}
