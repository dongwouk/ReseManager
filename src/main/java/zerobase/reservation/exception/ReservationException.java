package zerobase.reservation.exception;

import lombok.*;
import zerobase.reservation.type.ErrorCode;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public ReservationException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
