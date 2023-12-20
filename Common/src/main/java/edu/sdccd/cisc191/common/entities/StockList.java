package edu.sdccd.cisc191.common.entities;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * StockList a program to create a list of stocks to be used to display on the webpage
 */
public class StockList {

    //Node for each item in list with pointer to next item.
    private class Node {
        Stock item;
        Node next;
    }

    private Node head;  //Head of list
    private int length;  //Length of list


//    private LinkedList<Stock> stocks; //the stocks that will make up the list of stocks

    /**
     * Constructor creates a new StockList
     * no-args constructor, just initializes a new LinkedList<Stock>
     * instance variable for stocks.
     */
    public StockList() {
        this.head = null;
        this.length = 0;
    }

    /**
     * Constructor creates a new StockList from data
     * @param stocks - a linked list of stocks to initialize the stocks
     * instance variable.
     */
    public StockList(LinkedList<Stock> stocks) {
        for (Stock stock : stocks) {
            this.add(stock);
        }
    }

    /**
     * Constructor creates a new StockList from data
     * @param stocks - an ArrayList of stocks to initialize the stocks
     * instance variable.
     */
    public StockList(ArrayList<Stock> stocks) {
        for (Stock stock : stocks) {
            this.add(stock);
        }
    }

    //Class Methods

    /**
     * getStocks
     * no args
     * returns stocks as a Java LinkedList (for use in frontend)
     */
    public LinkedList<Stock> getStocks() {
        LinkedList<Stock> stocks = new LinkedList<>();

        if(head == null) {
            return stocks;
        }
        //Traverse through linked list and add to stocks variable.
        Node runner;
        Node previous;
        runner = head.next;
        previous = head;

        //Add the head to the stocks list
        stocks.add(head.item);

        while (runner != null) {
            // Move along the list until either the runner hits the end or finds
            //where to insert the newStock.
            stocks.add(runner.item);
            previous = runner;
            runner = runner.next;
        }

        //Return stocks
        return stocks;
    }

    /**
     * add
     * @param newStock
     * Adds newStock to the linked list stocks and
     * sorts list.
     */
    public void add(Stock newStock) {

        //Create a new node and add newStock as the item.
        Node newNode = new Node();
        newNode.item = newStock;

        if (head == null) {
            //If list is empty, insert at beginning.
            head = newNode;
        } else if (head.item.compareTo(newStock) >= 0) {
            //If ticker symbol for head is greater than ticker symbol for newStock,
            //Make newStock the head and push list up.
            newNode.next = head;
            head = newNode;
        } else {
            //If the newStock ticker is larger than the head's ticker, then traverse the
            //list to place it in the correct location.
            Node runner;
            Node previous;
            runner = head.next;
            previous = head;

            while (runner != null && runner.item.compareTo(newStock) < 0) {
                // Move along the list until either the runner hits the end or finds
                //where to insert the newStock.
                previous = runner;
                runner = runner.next;
            }
            //Insert newNode after traversal.
            newNode.next = runner;
            previous.next = newNode;
        }

        //Update length
        this.length++;
    }

    /**
     * delete
     * @param stockToRemove
     * Takes the variable id and uses it to
     * find a stock in the stocks list.  If successful,
     * the stock is removed from the linked list and returns 0.
     * If unsuccessful, the method returns -1.
     */
    public boolean delete(Stock stockToRemove) {
        //If the head is null, list is empty
        if (head == null) {
            return false;
        } else if (head.item.equals(stockToRemove)) {
            //If the stock to remove is the head, remove the head.
            head = head.next;
            this.length--;
            return true;
        } else {
            Node runner;
            Node previous;
            runner = head.next;
            previous = head;

            //Traverse list.
            while (runner != null && runner.item.compareTo(stockToRemove) < 0) {
                previous = runner;
                runner = runner.next;
            }
            if (runner != null && runner.item.equals(stockToRemove)) {
                //If stockToRemove is found, remove it.
                previous.next = runner.next;
                this.length--;
                return true;
            } else {
                //stockToRemove not found in list.
                return false;
            }
        }
    }

    /**
     * delete
     * @param ticker
     * Takes the variable id and uses it to
     * find a stock in the stocks list.  If successful,
     * the stock is removed from the linked list and returns 0.
     * If unsuccessful, the method returns -1.
     */
    public boolean delete(String ticker) {
        //If the head is null, list is empty
        if (head == null) {
            return false;
        } else if (head.item.getTicker().equals(ticker)) {
            //If the stock to remove is the head, remove the head.
            head = head.next;
            this.length--;
            return true;
        } else {
            Node runner;
            Node previous;
            runner = head.next;
            previous = head;

            //Traverse list.
            while (runner != null && runner.item.getTicker().compareTo(ticker) < 0) {
                previous = runner;
                runner = runner.next;
            }
            if (runner != null && runner.item.getTicker().equals(ticker)) {
                //If stockToRemove is found, remove it.
                previous.next = runner.next;
                this.length--;
                return true;
            } else {
                //stockToRemove not found in list.
                return false;
            }
        }
    }


    /**
     * length
     * no-args
     * Returns the number of items in the stocks linked list as an integer.
     */
    public int length() {
        return this.length;
    }
}
