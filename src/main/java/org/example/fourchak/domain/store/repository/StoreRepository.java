package org.example.fourchak.domain.store.repository;

import org.example.fourchak.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreRepository extends JpaRepository<Store, Long> {

    default Store findStoreByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
    }

    // 검색 메서드 추가
    @Query("SELECT s FROM Store s WHERE s.storeName LIKE %:keyword% AND s.deletedAt IS NULL")
    Page<Store> findByStoreNameContaining(@Param("keyword") String keyword, Pageable pageable);

}
