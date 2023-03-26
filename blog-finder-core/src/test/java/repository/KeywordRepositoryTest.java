package repository;

import com.soon.domain.Keyword;
import com.soon.dto.KeywordRankDto;
import com.soon.repsoitory.KeywordRepository;
import com.soon.service.KeywordService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("키워드 레포지토리 테스트")
public class KeywordRepositoryTest {
    @Mock
    private KeywordRepository keywordRepository;

    @InjectMocks
    private KeywordService keywordService;

    @Test
    @DisplayName("문자열 검색시 키워드 반환 테스트")
    void findKeywordByword() {
        String word = "soon";
        Long count = 10L;
        Keyword keyword = new Keyword(word, count);

        when(keywordRepository.findKeywordByWord(word)).thenReturn(keyword);

        assertEquals(keyword, keywordRepository.findKeywordByWord(word));
        verify(keywordRepository, times(1)).findKeywordByWord(word);
    }

    @Test
    @DisplayName("인기 검색어 Top10 키워드 리스트 반환 테스트")
    void findTop10ByOrderByCountDesc() {
        Keyword keyword1 = new Keyword("soon", 11L);
        Keyword keyword2 = new Keyword("han", 10L);
        List<Keyword> list = Arrays.asList(keyword1, keyword2);

        when(keywordRepository.findTop10ByOrderByCountDesc()).thenReturn(list);
        List<KeywordRankDto> dto = keywordService.getTop10KewordsAndCount();

        assertEquals(dto.get(0).getWord(), "soon");
        verify(keywordRepository, times(1)).findTop10ByOrderByCountDesc();
    }

    @Test
    @DisplayName("문자열 존재 유무 테스트")
    void existsKeywordByWord() {
        Keyword keyword1 = new Keyword("soon", 1L);

        when(keywordRepository.existsKeywordByWord(keyword1.getWord())).thenReturn(true);
        when(keywordRepository.findKeywordByWord(keyword1.getWord())).thenReturn(keyword1);
        keywordService.collectKeyword("soon");

        verify(keywordRepository, times(1)).existsKeywordByWord("soon");
    }
}
