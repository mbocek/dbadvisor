package com.github.embeditcz.dbadvisor.core.internal.issue;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.github.embeditcz.dbadvisor.core.analyzer.QueryContext;
import com.github.embeditcz.dbadvisor.core.issue.Issue;
import com.github.embeditcz.dbadvisor.core.issue.IssueBuilder;
import com.github.embeditcz.dbadvisor.core.issue.IssueMetadataProvider;
import com.github.embeditcz.dbadvisor.core.issue.IssueStackTraceFilter;
import net.ttddyy.dsproxy.QueryInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class IssueBuilderImpl implements IssueBuilder {

    private final List<IssueMetadataProvider> metadataProviders;
    private final List<IssueStackTraceFilter> stackTraceFilters;

    IssueBuilderImpl(
            @Autowired(required = false) List<IssueMetadataProvider> metadataProviders,
            @Autowired(required = false) List<IssueStackTraceFilter> stackTraceFilters) {
        this.metadataProviders = metadataProviders;
        this.stackTraceFilters = stackTraceFilters;
    }

    @Override
    public Builder builder() {
        return new BuilderImpl();
    }

    private class BuilderImpl implements Builder {

        private String type;
        private String query;
        private String description;
        private long weight;
        private String weightUnit;
        private LocalDateTime timestamp;
        private String[] stackTrace;
        private Map<String, Object> metadata = new LinkedHashMap<>();

        @Override
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        @Override
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public Builder query(QueryContext queryContext) {
            this.query = resolveQuery(queryContext);
            return this;
        }

        @Override
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public Builder weight(long weight) {
            this.weight = weight;
            return this;
        }

        @Override
        public Builder weightUnit(String weightUnit) {
            this.weightUnit = weightUnit;
            return this;
        }

        @Override
        public Builder metadata(String name, Object value) {
            metadata.put(name, value);
            return this;
        }

        @Override
        public Issue build() {
            resolveTimestamp();
            resolveStackTrace();
            resolveMetadata();
            return new Issue(type, query, description, weight, weightUnit, timestamp, stackTrace, metadata);
        }

        private String resolveQuery(QueryContext ctx) {
            String query = null;
            if (!ctx.getQueryInfoList().isEmpty()) {
                QueryInfo queryInfo = ctx.getQueryInfoList().get(0);
                query = queryInfo.getQuery();
            }
            return query;
        }

        private void resolveTimestamp() {
            if (timestamp == null) {
                timestamp = LocalDateTime.now();
            }
        }

        private void resolveMetadata() {
            if (metadataProviders != null) {
                for (IssueMetadataProvider metadataProvider : metadataProviders) {
                    metadata.putAll(metadataProvider.getIssueMetadata());
                }
            }
        }

        private void resolveStackTrace() {
            if (stackTrace == null) {
                StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
                stackTrace = Stream.of(stackTraceElements).map(StackTraceElement::toString).toArray(String[]::new);
            }
            if (stackTraceFilters != null) {
                for (IssueStackTraceFilter stackTraceFilter : stackTraceFilters) {
                    stackTrace = stackTraceFilter.filterStackTrace(stackTrace);
                }
            }
        }

    }

}
