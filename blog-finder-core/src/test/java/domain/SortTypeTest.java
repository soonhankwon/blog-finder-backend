package domain;

import com.soon.domain.SortType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SortTypeTest {

    @Test
    @DisplayName("SortType value equals 테스트")
    void enumValueEqualsString() {
        String str = SortType.ACCURACY.getValue();
        Assertions.assertThat(str).isEqualTo("accuracy");
    }
}
