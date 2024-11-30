package zerobase.reservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.type.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findAllByMemberId(Long memberId, Pageable pageable);
    Page<Reservation> findAllByStoreId(Long memberId, Pageable pageable);

    Page<Reservation> findByMemberIdAndReservationStatusAndReviewIsNull(Long memberId, ReservationStatus reservationStatus, Pageable pageable);

    List<Reservation> findByTimeBeforeAndReservationStatusIsNot(LocalDateTime currentTime, ReservationStatus reservationStatus);
}
