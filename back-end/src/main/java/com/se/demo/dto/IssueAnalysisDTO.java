package com.se.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class IssueAnalysisDTO {
    private final int year = 24;
    private List<MonthlyIssueAnalysis> monthlyIssueAnalysisList;

}

@RequiredArgsConstructor
@Getter
@Setter
class MonthlyIssueAnalysis {
    private int month;

    private int newCnt;
    private int assignedCnt;
    private int fixedCnt;
    private int resolvedCnt;
    private int closedCnt;
    private int reopened;

}