package edu.sdccd.cisc191.server.AlphaAdvantageAPI.alphavantage;

import com.crazzyghost.alphavantage.indicator.response.PeriodicSeriesResponse;
import com.crazzyghost.alphavantage.indicator.response.SimpleIndicatorUnit;
import com.crazzyghost.alphavantage.parser.Parser;

import java.util.List;
import java.util.Map;

public class CMOResponse extends PeriodicSeriesResponse {

    private CMOResponse(List<SimpleIndicatorUnit> indicatorUnits, MetaData metaData) {
        super(indicatorUnits, metaData);
    }

    private CMOResponse(String errorMessage) {
        super(errorMessage);
    }

    public static CMOResponse of(Map<String, Object> stringObjectMap){
        Parser<CMOResponse> parser = new CMOResponseParser();
        return parser.parse(stringObjectMap);
    }

    public static class CMOResponseParser extends PeriodicSeriesParser<CMOResponse> {

        @Override
        public CMOResponse get(List<SimpleIndicatorUnit> indicatorUnits, MetaData metaData) {
            return new CMOResponse(indicatorUnits, metaData);
        }

        @Override
        public CMOResponse get(String errorMessage) {
            return new CMOResponse(errorMessage);
        }

        @Override
        protected String getIndicatorKey() {
            return "CMO";
        }
    }
}
