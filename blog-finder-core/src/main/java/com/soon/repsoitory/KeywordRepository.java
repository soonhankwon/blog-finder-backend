package com.soon.repsoitory;

import com.soon.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findKeywordByWord(String keyword);
    boolean existsKeywordByWord(String keyword);
    List<Keyword> findTop10ByOrderByCountDesc();
}
