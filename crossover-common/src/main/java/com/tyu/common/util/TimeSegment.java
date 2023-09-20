package com.tyu.common.util;

import lombok.Data;

@Data
public class TimeSegment implements Comparable {

    private Long start;

    private Long end;

    private Integer overlapCounter = 0;




    public Integer getOverlapCounter() {
        return overlapCounter;
    }

    @Override
    public int compareTo(Object o) {
        TimeSegment other = (TimeSegment) o;
        if (this.end < other.start) {
            return -1;
        } else if (this.start > other.end) {
            return 1;
        }
        overlapCounter++;
        return 0;
    }


}
