package com.github.embeditcz.dbadvisor.core.issue;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;

public class IssueTest {

    private ObjectMapper objectMapper;

    @Before
    public void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void shouldBeSerializableToJson() throws JsonProcessingException {
        Issue issue = new Issue();
        issue.setType("SLOW QUERY");
        issue.setDescription("Lorem ipsum dolor sit amet, consectetuer adipiscing elit");
        issue.setQuery("select * from internet");
        issue.setWeight(123);
        issue.setTimestamp(LocalDateTime.now());
        issue.setStackTrace(Stream.of(Thread.currentThread().getStackTrace()).map(StackTraceElement::toString).toArray(String[]::new));
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("elapsedTime", 1024);
        metadata.put("hcitrace", "123456789");
        issue.setMetadata(metadata);

        String json = objectMapper.writeValueAsString(issue);

        assertThat(json).isNotEmpty();
    }

}
