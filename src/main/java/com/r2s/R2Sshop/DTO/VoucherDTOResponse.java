package com.r2s.R2Sshop.DTO;

import com.r2s.R2Sshop.model.Voucher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherDTOResponse {
    private String name;
    private String code;
    private Double discount;
    private Integer quantity;
    private LocalDateTime expireDate;

    /**
     * Customize the output variant product information as a JSON file.
     * <p>
     * This method customizes the output variant product information, including
     * name, code, discount, quantity and expireDate as a JSON file.
     * @param voucher
     * @author HoangVu
     * @since 1.0
     */
    public VoucherDTOResponse(Voucher voucher) {
        this.name = voucher.getName();
        this.code = voucher.getCode();
        this.discount = voucher.getDiscount();
        this.quantity = voucher.getQuantity();
        this.expireDate = voucher.getExpireDate();
    }
}
