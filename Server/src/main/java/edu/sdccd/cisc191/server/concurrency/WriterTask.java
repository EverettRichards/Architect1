package edu.sdccd.cisc191.server.concurrency;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WriterTask {
    private String ticker;
    private String candleData;
}
