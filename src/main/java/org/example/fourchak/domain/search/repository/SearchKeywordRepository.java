package org.example.fourchak.domain.search.repository;

import java.util.List;
import java.util.Optional;
import org.example.fourchak.domain.search.entity.SearchKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

    Optional<SearchKeyword> findByKeyword(String keyword);

    @Query("SELECT s from SearchKeyword s ORDER BY s.count DESC LIMIT 10")
    List<SearchKeyword> findTop10ByOrderByCountDesc();


}
