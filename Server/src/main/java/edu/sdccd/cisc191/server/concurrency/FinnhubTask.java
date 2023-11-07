package edu.sdccd.cisc191.server.concurrency;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinnhubTask {
    private String ticker;
    private Instant startTime;
    private Instant endTime;
}
