import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class LocalDateTimeParseTest {
    @Test
    @DisplayName("네이버 LocalDate 를 LocalDateTime 으로 파싱 테스트")
    void parseLocalDateTime() {
        LocalDateTime localDateTime = LocalDate.parse("2023-03-24", DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
        assertThat(localDateTime.toString()).isEqualTo("2023-03-24T00:00");
    }
}
