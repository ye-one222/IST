package com.se.demo.dto;

import lombok.*;

@Getter
@RequiredArgsConstructor
@Setter
public class MonthlyAnalysisDTO {
        private int month;
        //각 상태마다 카운트
        private int newCnt;
        private int assignedCnt;
        private int fixedCnt;
        private int resolvedCnt;
        private int closedCnt;
        private int reopened;

}
