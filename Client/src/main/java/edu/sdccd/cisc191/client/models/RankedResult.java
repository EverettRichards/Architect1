package edu.sdccd.cisc191.client.models;

import edu.sdccd.cisc191.common.entities.Stock;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankedResult {
    private int score;
    private Stock stock;
}
