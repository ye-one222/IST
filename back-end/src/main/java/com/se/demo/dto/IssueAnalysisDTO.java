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
    private Map<String, Integer> statusCounts; //key: 상태, value: 해당상태의 이슈개수

}