package domain;

import com.soon.domain.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SortTypeTest {

    @Test
    @DisplayName("SortType value equals 테스트")
    void enumValueEqualsString() {
        String str1 = SortType.ACCURACY.getValue();
        String str2 = SortType.RECENCY.getValue();
        assertThat(str1).isEqualTo("accuracy");
        assertThat(str2).isEqualTo("recency");
    }
}
