package zerobase.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.domain.Review;
import zerobase.reservation.domain.Store;
import zerobase.reservation.dto.ReviewDto;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;
import zerobase.reservation.repository.ReservationRepository;
import zerobase.reservation.repository.ReviewRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.type.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static zerobase.reservation.dto.ReviewDto.toReviewEntity;
import static zerobase.reservation.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ReservationRepository reservationRepository;

    public ReviewDto join(ReviewDto reviewDto) {
        Member member = memberRepository.findById(reviewDto.getMemberId())
                .orElseThrow(() -> new ReservationException(MEMBER_NOT_FOUND));

        Store store = storeRepository.findById(reviewDto.getStoreId())
                .orElseThrow(() -> new ReservationException(STORE_NOT_FOUND));

        Reservation reservation = reservationRepository.findById(reviewDto.getReservationId())
                .orElseThrow(() -> new ReservationException(RESERVATION_NOT_FOUND));

        if(reservation.getReview() != null) {
            throw new ReservationException(ALREADY_EXIST_REVIEW);
        }
        if(reservation.getReservationStatus()!= ReservationStatus.CONFIRMED){
            throw new ReservationException(RESERVATION_NOT_CONFIRMED);
        }

        Review review = reviewRepository.save(toReviewEntity(member, store, reservation, reviewDto));

        reservation.setReview(review);

        return convertToDto(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewDto> findAllByMemberId(Long memberId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByMemberIdAndContentIsNotNull(memberId, pageable);

        List<ReviewDto> filteredReviews = reviews.stream()
                .filter(review -> review.getStore() != null && review.getReservation() != null)
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageImpl<>(filteredReviews, pageable, reviews.getTotalElements());
    }



    @Transactional(readOnly = true)
    public Page<ReviewDto> findAllByStoreId(Long StoreId, Pageable pageable) {
        Page<Review> allByStoreIdAndContentIsNotNull = reviewRepository.findAllByStoreIdAndContentIsNotNull(StoreId, pageable);
        List<ReviewDto> collect = allByStoreIdAndContentIsNotNull.stream()
                .filter(review -> review.getStore() != null && review.getReservation() != null)
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(collect, pageable, allByStoreIdAndContentIsNotNull.getTotalElements());
    }

    public ReviewDto updateStore(ReviewDto reviewDto, Long id) {
        Review review = reviewRepository.findById(id).get();

        if (reviewDto.getContent() != null && !reviewDto.getContent().isEmpty()) {
            review.setContent(reviewDto.getContent());
        }

        review.setUpdatedAt(LocalDateTime.now());
        return convertToDto(review);
    }

    public void deleteStore(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);

        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();

            //사용자 인증 코드 필요

            // 연관된 Reservation의 외래 키를 null로 설정
            Reservation reservation = review.getReservation();
            if (reservation != null) {
                reservation.setReview(null);
                reservationRepository.save(reservation);
            }

            // Review 삭제
            reviewRepository.deleteById(id);
        }
    }

    private ReviewDto convertToDto(Review review) {
        return ReviewDto.builder()
                .memberId(review.getMember().getId())
                .storeId(review.getStore().getId())
                .reservationId(review.getReservation().getId())
                .reviewId(review.getId())
                .content(review.getContent())
                .build();
    }
}
