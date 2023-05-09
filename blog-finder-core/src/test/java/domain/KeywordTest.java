package domain;

import com.soon.domain.Keyword;
import com.soon.exception.ApiException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class KeywordTest {

    @Test
    @DisplayName("키워드 공백 제거 테스트")
    void removeSpaceWord() {
        Keyword keyword = new Keyword("!@#     SoOn");
        assertThat(keyword).isEqualTo(new Keyword("soon"));
    }

    @Test
    @DisplayName("키워드 특수문자 제거 & 소문자로 변환 테스트")
    void convertLowerCase() {
        Keyword keyword = new Keyword("!@#SoOn", 1L);
        assertThat(keyword).isEqualTo(new Keyword("soon", 1L));
    }

    @Test
    @DisplayName("키워드 검색횟수 증가 테스트")
    void increaseCount() {
        Keyword keyword = new Keyword(1L,"Soon", 1L);
        keyword.increaseCount();
        assertThat(keyword).isEqualTo(new Keyword(1L,"Soon", 2L));
    }

    @Test
    @DisplayName("키워드 빈 문자열 예외처리")
    void keywordWithEmpty() {
        assertThatThrownBy(() -> {
            new Keyword("", 1L);
        }).isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("키워드 null 예외처리")
    void keywordWithNull() {
        assertThatThrownBy(() -> {
            new Keyword(null, 1L);
        }).isInstanceOf(ApiException.class);
    }

    @Test
    @DisplayName("검색 횟수 음수값 예외처리")
    void countUnderTheMinimum() {
        assertThatThrownBy(() -> {
            new Keyword("soon", -1L);
        }).isInstanceOf(ApiException.class);
    }
}
