package zerobase.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.domain.Review;
import zerobase.reservation.domain.Store;
import zerobase.reservation.dto.ReservationDto;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.StoreRepository;

import java.time.LocalDateTime;
import java.util.List;

import static zerobase.reservation.dto.ReservationDto.toReservationEntity;
import static zerobase.reservation.type.ErrorCode.*;
import static zerobase.reservation.type.ReservationStatus.CANCELED;
import static zerobase.reservation.type.ReservationStatus.CONFIRMED;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    private static Review review;

    @Scheduled(fixedRate = 60000)
    public void checkForExpiredReservations() {
        LocalDateTime currentTime = LocalDateTime.now();

        List<Reservation> expiredReservations = reservationRepository.findByTimeBeforeAndReservationStatusIsNot(currentTime, CANCELED);
        for (Reservation reservation : expiredReservations) {
            reservation.setReservationStatus(CANCELED);
            reservationRepository.save(reservation);
        }
    }

    public ReservationDto join(ReservationDto reservationDto) {
        Member member = memberRepository.findById(reservationDto.getMemberId()).get();
        Store store = storeRepository.findById(reservationDto.getStoreId()).get();
        Review review = null;

        Reservation reservation = reservationRepository.save(toReservationEntity(member, store, review, reservationDto));
        return reservationToDto(reservation);
    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> findAllByMemberId(Long memberId, Pageable pageable) {
        Page<Reservation> reservations = reservationRepository.findAllByMemberId(memberId, pageable);
        return reservations.map(this::reservationToDto);

    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> findAllByStoreId(Long StoreId, Pageable pageable) {
        Page<Reservation> allByStoreId = reservationRepository.findAllByStoreId(StoreId, pageable);
        return allByStoreId.map(this::reservationToDto);
    }

    public ReservationDto confirmReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        LocalDateTime reservationTime = reservation.getTime();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime limitBefore = reservationTime.minusMinutes(10);

        if (currentTime.isBefore(limitBefore)) {
            throw new ReservationException(TOO_EARLY_FOR_CONFIRMATION);
        }

        if (currentTime.isAfter(reservationTime)) {
            reservation.setReservationStatus(CANCELED);
            throw new ReservationException(TOO_LATE_FOR_CONFIRMATION);
        }

        reservation.setReservationStatus(CONFIRMED);
        return reservationToDto(reservation);
    }


    public ReservationDto cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).get();
        reservation.setReservationStatus(CANCELED);
        return reservationToDto(reservation);
    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> findAllConfirmedReservationWithoutReview(Long memberId, Pageable pageable) {
        Page<Reservation> byMemberIdAndReservationStatusAndReviewIsNull = reservationRepository
                .findByMemberIdAndReservationStatusAndReviewIsNull(memberId, CONFIRMED, pageable);
        return byMemberIdAndReservationStatusAndReviewIsNull.map(this::reservationToDto);
    }

    private ReservationDto reservationToDto(Reservation reservation) {
        return new ReservationDto().builder()
                .memberId(reservation.getMember().getId())
                .storeId(reservation.getStore().getId())
                .reservationId(reservation.getId())
                .time(reservation.getTime())
                .reservationStatus(reservation.getReservationStatus())
                .build();
    }
}
