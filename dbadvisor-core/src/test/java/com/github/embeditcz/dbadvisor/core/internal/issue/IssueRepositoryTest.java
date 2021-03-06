package com.github.embeditcz.dbadvisor.core.internal.issue;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.embeditcz.dbadvisor.core.issue.Issue;
import com.github.embeditcz.dbadvisor.core.issue.IssueRepository;
import org.junit.Test;

public class IssueRepositoryTest {

    private IssueRepository issueRepository = new IssueRepositoryImpl();

    @Test
    public void shouldSortIssues() {
        issueRepository.addIssue(issue("slow", "select 1", 333, "id1"));
        issueRepository.addIssue(issue("slow", "select 2", 222, "id2"));
        issueRepository.addIssue(issue("slow", "select 3", 111, "id3"));
        issueRepository.addIssue(issue("cost", "select 1", 333, "id4"));
        issueRepository.addIssue(issue("cost", "select 2", 222, "id5"));
        issueRepository.addIssue(issue("cost", "select 3", 111, "id6"));

        List<Issue> issues = issueRepository.getIssues();

        assertThat(issues).hasSize(6);
        assertThat(issues.get(0).getDescription()).isEqualTo("id4");
        assertThat(issues.get(1).getDescription()).isEqualTo("id5");
        assertThat(issues.get(2).getDescription()).isEqualTo("id6");
        assertThat(issues.get(3).getDescription()).isEqualTo("id1");
        assertThat(issues.get(4).getDescription()).isEqualTo("id2");
        assertThat(issues.get(5).getDescription()).isEqualTo("id3");
    }

    @Test
    public void shouldDropLessImportantIssues() {
        issueRepository.addIssue(issue("slow", "select 1", 111, "id1"));
        issueRepository.addIssue(issue("slow", "select 1", 333, "id2"));
        issueRepository.addIssue(issue("slow", "select 1", 222, "id3"));

        List<Issue> issues = issueRepository.getIssues();

        assertThat(issues).hasSize(1);
        assertThat(issues.get(0).getDescription()).isEqualTo("id2");
    }

    @Test
    public void shouldDropTooMuchIssues() {
        for (int i = 0; i < 111; i++) {
            issueRepository.addIssue(issue("slow", "select " + i, i, "id" + i));
        }

        List<Issue> issues = issueRepository.getIssues();
        assertThat(issues).hasSize(100);
        assertThat(issues.get(0).getDescription()).isEqualTo("id110");
        assertThat(issues.get(99).getDescription()).isEqualTo("id11");
    }

    private Issue issue(String type, String query, long weight, String description) {
        return new Issue(type, query, description, weight, null, null, null, null);
    }

}
