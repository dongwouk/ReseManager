package zerobase.reservation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.reservation.domain.Member;
import zerobase.reservation.domain.Reservation;
import zerobase.reservation.domain.Store;
import zerobase.reservation.dto.MemberOwnedStoreReservationsDto;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.type.MemberStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static zerobase.reservation.dto.StoreDto.toStoreEntity;
import static zerobase.reservation.type.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;
    private final MemberRepository memberRepository;

    public StoreDto join(StoreDto storeDto) {
        Member member = memberRepository.findById(storeDto.getMemberId())
                .orElseThrow(() -> new ReservationException(MEMBER_NOT_FOUND));

        if (storeRepository.existsByName(storeDto.getName())) {
            throw new ReservationException(ALREADY_EXIST_STORE);
        }

        if (member.getMemberStatus() != MemberStatus.PARTNER) {
            throw new ReservationException(INVALID_MEMBER_STATUS_ERROR);
        }

        Store savedStore = storeRepository.save(toStoreEntity(member, storeDto));
        return storeToDto(savedStore);
    }


    @Transactional(readOnly = true)
    public StoreDto findById(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ReservationException(STORE_NOT_FOUND));
        return storeToDto(store);
    }

    @Transactional(readOnly = true)
    public Page<StoreDto> findAll(Pageable pageable) {
        Page<Store> stores = storeRepository.findAll(pageable);
        return stores.map(this::storeToDto);  // Page의 map 메소드 사용
    }


    @Transactional(readOnly = true)
    public Page<StoreDto> findByMember(Long memberId, Pageable pageable) {
        Page<Store> stores = storeRepository.findByMemberId(memberId, pageable);
        return stores.map(this::storeToDto);
    }

    public StoreDto updateStore(StoreDto storeDto, Long id) {
        Store store = storeRepository.findById(id).get();

        updateStore(storeDto, store);

        return storeToDto(store);
    }

    private void updateStore(StoreDto storeDto, Store store) {
        store.setName(storeDto.getName());
        store.setLocation(storeDto.getLocation());
        store.setDescription(storeDto.getDescription());
        store.setX(storeDto.getX());
        store.setY(store.getY());
        store.setUpdatedAt(LocalDateTime.now());
    }

    public void deleteStore(Long id) {
        storeRepository.deleteById(id);
    }

    private StoreDto storeToDto(Store store) {
        return new StoreDto().builder()
                .storeId(store.getId())
                .memberId(store.getMember().getId())
                .name(store.getName())
                .location(store.getLocation())
                .description(store.getDescription())
                .x(store.getX())
                .y(store.getY())
                .build();
    }


    public Page<MemberOwnedStoreReservationsDto> findReservationsForMemberStores(Long memberId, Pageable pageable) {
        List<MemberOwnedStoreReservationsDto> result = new ArrayList<>();

        // 주어진 페이지에 해당하는 상점들만 가져옵니다.
        Page<Store> stores = storeRepository.findByMemberId(memberId, pageable);

        for (Store store : stores) {
            List<Reservation> reservations = store.getReservationList();

            for (Reservation reservation : reservations) {
                MemberOwnedStoreReservationsDto dto = MemberOwnedStoreReservationsDto.builder()
                        .storeName(store.getName())
                        .reservationMemberName(memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found")).getUsername())
                        .createdAt(reservation.getCreatedAt())
                        .reservationAt(reservation.getTime())
                        .build();

                result.add(dto);
            }
        }

        // PageImpl을 사용하여 List를 Page로 변환
        return new PageImpl<>(result, pageable, result.size());
    }


}
