package com.example.issuetracker.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ApplicationConfiguration {

    @Value("${average-story-point:10}")
    private Integer averageStoryPoint;

    @Value("${spring.jackson.date-format}")
    private String dateFormat;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormat)));
    }

}
