package org.example.fourchak.domain.store.repository;

import org.example.fourchak.domain.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    default Store findStoreByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 가게입니다."));
    }


}
