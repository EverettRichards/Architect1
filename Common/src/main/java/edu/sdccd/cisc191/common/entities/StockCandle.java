package edu.sdccd.cisc191.common.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StockCandle {
    private String ticker;

    private static final String[] subKeys = {"c","h","l","o","t","v"};

    private static double[][] stockInfo;

    public void setTicker(String txt){
        ticker = txt;
    }

    public String getTicker(){
        return ticker;
    }

    public static double[][] invert2DArray(double[][] inputArray){
        int rows = inputArray.length;
        int columns = inputArray[0].length;

        double[][] newArray = new double[columns][rows];

        for (int i=0;i<rows;i++){
            for (int j=0;j<columns;j++){
                newArray[j][i] = inputArray[i][j];
            }
        }

        return newArray;
    }

    private static void refreshData(String jsonInput) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonInput);

        double[][] result = new double[subKeys.length][];

        int keyIndex = 0;

        for (String subKey : subKeys) {

            JsonNode tNode = rootNode.get(subKey);
            result[keyIndex] = new double[tNode.size()];

            for (int i = 0; i < tNode.size(); i++) {
                result[keyIndex][i] = tNode.get(i).asDouble();
            }

            keyIndex++;

        }

        stockInfo = invert2DArray(result);

        //System.out.println(stockInfo[0][0]);
    }

    /*public static void main(String[] args) throws JsonProcessingException {
        refreshData(candleExample);
    }*/

    public StockCandle(String newTicker, String jsonInput) throws JsonProcessingException {
        refreshData(jsonInput);
        setTicker(newTicker);
    }

    public double[][] getStockInfo(){
        return stockInfo;
    }
    
    public String toString(){
        String outputString = "";
        for (double[] row : stockInfo){
            outputString += String.format("At time %.2f, haha",row[4]);
            for (double item : row){

            }
        }
        return outputString;
    }
}
