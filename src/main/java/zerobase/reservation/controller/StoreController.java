package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zerobase.reservation.dto.MemberOwnedStoreReservationsDto;
import zerobase.reservation.dto.Result;
import zerobase.reservation.dto.StoreDto;
import zerobase.reservation.repository.StoreRepository;
import zerobase.reservation.service.StoreService;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@Transactional
public class StoreController {
    private final StoreService storeService;
    private final StoreRepository storeRepository;

    @PostMapping("/store")
    public ResponseEntity<StoreDto> create(
            @RequestBody StoreDto storeDto
            ) {
        storeDto = storeService.join(storeDto);
        return ResponseEntity.ok(storeDto);
    }

    @GetMapping("/stores")
    public ResponseEntity<Map<String, Object>> findAll(Pageable pageable) {
        Page<StoreDto> pagedStores = storeService.findAll(pageable);

        Map<String, Object> response = getPageableStoreDto(pagedStores);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> getPageableStoreDto(Page<StoreDto> pagedStores) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", pagedStores.getContent());
        response.put("currentPage", pagedStores.getNumber());
        response.put("totalItems", pagedStores.getTotalElements());
        response.put("totalPages", pagedStores.getTotalPages());
        return response;
    }


    @GetMapping("/store/{storeId}")
    public ResponseEntity<Result> findById(@PathVariable("storeId") Long id) {
        StoreDto storeDto = storeService.findById(id);
        return ResponseEntity.ok(new Result(1, storeDto));
    }

    @GetMapping("/store/member/{memberId}")
    public ResponseEntity<Map<String, Object>> findByMember(@PathVariable("memberId") Long id, Pageable pageable) {
        Page<StoreDto> storePage = storeService.findByMember(id, pageable);

        Map<String, Object> response = getPageableStoreDto(storePage);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/members/{memberId}/owned-stores/reservations")
    public ResponseEntity<Map<String, Object>> findReservationsForMemberStores(@PathVariable("memberId") Long id, Pageable pageable) {
        Page<MemberOwnedStoreReservationsDto> reservationPage = storeService.findReservationsForMemberStores(id, pageable);

        Map<String, Object> response = getStringObjectMap(reservationPage);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> getStringObjectMap(Page<MemberOwnedStoreReservationsDto> reservationPage) {
        Map<String, Object> response = new HashMap<>();
        response.put("data", reservationPage.getContent());
        response.put("currentPage", reservationPage.getNumber());
        response.put("totalItems", reservationPage.getTotalElements());
        response.put("totalPages", reservationPage.getTotalPages());
        return response;
    }


    @PutMapping("/store/{storeId}")
    public ResponseEntity<StoreDto> update(
            @RequestBody StoreDto storeDto,
            @PathVariable("storeId") Long id
    ) {
        storeDto = storeService.updateStore(storeDto, id);
        return ResponseEntity.ok(storeDto);
    }

    @DeleteMapping("/store/{storeId}")
    public void delete(
            @PathVariable("storeId") Long id
    ) {
        storeService.deleteStore(id);
    }


}
