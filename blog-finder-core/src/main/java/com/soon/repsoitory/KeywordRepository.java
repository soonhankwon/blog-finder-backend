package com.soon.repsoitory;

import com.soon.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    Keyword findKeywordByWord(String keyword);
    boolean existsKeywordByWord(String keyword);
}
