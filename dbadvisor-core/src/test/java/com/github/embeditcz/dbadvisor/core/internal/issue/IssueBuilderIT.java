package com.github.embeditcz.dbadvisor.core.internal.issue;

import com.github.embeditcz.dbadvisor.core.AbstractIT;
import com.github.embeditcz.dbadvisor.core.issue.Issue;
import com.github.embeditcz.dbadvisor.core.issue.IssueBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueBuilderIT extends AbstractIT {

    @Autowired
    private IssueBuilder issueBuilder;

    @Test
    public void shouldBuildIssue() {
        Issue issue = issueBuilder.builder()
                .type("t")
                .description("d")
                .weight(0)
                .metadata("m1", "v1")
                .build();

        assertThat(issue.getType()).isEqualTo("t");
        assertThat(issue.getDescription()).isEqualTo("d");
        assertThat(issue.getMetadata()).containsKey("m1");
        assertThat(issue.getWeight()).isEqualTo(0);
        assertThat(issue.getTimestamp()).isNotNull();
        assertThat(issue.getStackTrace()).isNotEmpty();
    }

    @Test
    public void shouldFilterStackTrace() {
        Issue issue = issueBuilder.builder().build();

        String[] stackTrace = issue.getStackTrace();

        for (String stackTraceLine : stackTrace) {
            assertThat(stackTraceLine).doesNotStartWith("com.github.embeditcz.dbadvisor");
        }
    }

}
