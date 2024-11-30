package zerobase.reservation.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import zerobase.reservation.domain.Store;

import javax.transaction.Transactional;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    @Transactional
    void deleteById(Long id);

    Page<Store> findByMemberId(Long memberId, Pageable pageable);


    Optional<Store> findByName(String name);

    default boolean existsByName(String name) {
        return findByName(name).isPresent();
    }
}
