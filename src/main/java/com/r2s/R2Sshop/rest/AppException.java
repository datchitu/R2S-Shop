package com.r2s.R2Sshop.rest;

import com.r2s.R2Sshop.constants.ResponseCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppException extends RuntimeException{
    ResponseCode responseCode;

    /**
     * Return message of enum ResponseCode.
     * <p>
     * This method returns the message from the ResponseCode enum.
     * @param
     * @return the message from the ResponseCode enum
     * @throws
     * @author HoangVu
     * @since 1.0
     */
    public String getMessage() {
        return responseCode.getMessage();
    }
}
