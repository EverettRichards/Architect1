package edu.sdccd.cisc191.server.concurrency;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinnhubTask {
    private String ticker;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
