package gov.municipal.suda.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Slf4j
public class GenerateUtilOfWaterModule {
    public static final String generateConsumerNo() {
        StringBuilder concat=new StringBuilder();
        concat.append("PC0");
        concat.append(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        final String result=String.valueOf(concat);
        return result;
    }
}
