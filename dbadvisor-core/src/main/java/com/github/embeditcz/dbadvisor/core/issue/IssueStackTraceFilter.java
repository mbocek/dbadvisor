package com.github.embeditcz.dbadvisor.core.issue;

public interface IssueStackTraceFilter {

    String[] filterStackTrace(String[] stackTrace);

}
