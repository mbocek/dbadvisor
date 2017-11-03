package com.github.embeditcz.dbadvisor.core.issue;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data // TODO tmp, should be immutable
public class Issue {

    private String type;
    private String query;
    private String description;
    private long weight;
    private LocalDateTime timestamp;
    private StackTraceElement[] stackTrace;
    private Map<String, Object> metadata;

}
