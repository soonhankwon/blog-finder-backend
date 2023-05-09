package com.soon.repsoitory;

import com.soon.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findKeywordByWord(String keyword);
    boolean existsKeywordByWord(String keyword);
    List<Keyword> findTop10ByOrderByCountDesc();
}
