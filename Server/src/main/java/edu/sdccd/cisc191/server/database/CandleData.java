package edu.sdccd.cisc191.server.database;

import edu.sdccd.cisc191.server.database.CandleResolution;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import java.util.List;

@Entity
@Table(name="CandleTable")
public class CandleData {
    // reference https://finnhub.io/docs/api/stock-candles
    @Column(name = "ticker")
    private String ticker;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> oneMinute;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> fiveMinutes;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> fifthteenMinutes;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> thirtyMinutes;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> sixtyMinutes;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> oneDay;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> oneWeek;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(referencedColumnName = "ticker"))
    private List<CandleResolution> oneMonth;
}
