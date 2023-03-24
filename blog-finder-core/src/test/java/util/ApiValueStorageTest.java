package util;

import com.soon.utils.ApiReqValueStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiValueStorageTest {
    @Test
    @DisplayName("Storage 페이지네이션 밸류 테스트")
    void validStorageValue() {
        ApiReqValueStorage apiReqValueStorage = new ApiReqValueStorage();
        int paginationValue = apiReqValueStorage.getKakaoPagination();
        int displayValue = apiReqValueStorage.getNaverDisplay();

        assertThat(paginationValue).isEqualTo(10);
        assertThat(displayValue).isEqualTo(10);
    }
}
