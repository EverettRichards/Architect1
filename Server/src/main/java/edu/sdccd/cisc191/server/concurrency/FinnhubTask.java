package edu.sdccd.cisc191.server.concurrency;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinnhubTask {
    private String ticker;
    private String duration; // day week month 6month year 5year
    private Instant startTime;
    private Instant endTime;
}
