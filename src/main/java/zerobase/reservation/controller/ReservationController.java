package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.service.ReservationService;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("/reservation")
    public ResponseEntity<ReservationDto> create(
            @RequestBody ReservationDto reservationDto
    ) {
        reservationDto = reservationService.join(reservationDto);
        return ResponseEntity.ok(reservationDto);
    }


    @GetMapping("/reservations/member/{memberId}")
    public ResponseEntity<Map<String, Object>> findAllByMemberId(
            @PathVariable("memberId") Long id,
            Pageable pageable
    ) {
        Page<ReservationDto> reservationPage = reservationService.findAllByMemberId(id, pageable);

        Map<String, Object> response = getPageableResponse(reservationPage);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/reservations/store/{storeId}")
    public ResponseEntity<Map<String, Object>> findAllByStoreId(
            @PathVariable("storeId") Long id,
            Pageable pageable
    ) {
        Page<ReservationDto> allByStoreId = reservationService.findAllByStoreId(id, pageable);
        Map<String, Object> response = getPageableResponse(allByStoreId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("reservations/without_review/member/{memberId}")
    public ResponseEntity<Map<String, Object>> findAllConfirmedReservationWithoutReview(
            @PathVariable("memberId") Long id,
            Pageable pageable
    ) {
        Page<ReservationDto> allConfirmedReservationWithoutReview = reservationService.findAllConfirmedReservationWithoutReview(id, pageable);
        Map<String, Object> response = getPageableResponse(allConfirmedReservationWithoutReview);
        return ResponseEntity.ok(response);
    }

    private Map<String, Object> getPageableResponse(Page<ReservationDto> reservationPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", reservationPage.getContent());
        response.put("currentPage", reservationPage.getNumber());
        response.put("totalItems", reservationPage.getTotalElements());
        response.put("totalPages", reservationPage.getTotalPages());
        return response;
    }

    @PutMapping("/reservation/confirm")
    public ResponseEntity<ReservationDto> confirm(
            @RequestParam Long reservationId
    ) {
        ReservationDto reservation = reservationService.confirmReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }

    @PutMapping("/reservation/cancel")
    public ResponseEntity<ReservationDto> cancel(
            @RequestParam Long reservationId
    ) {
        ReservationDto reservation = reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(reservation);
    }
}
