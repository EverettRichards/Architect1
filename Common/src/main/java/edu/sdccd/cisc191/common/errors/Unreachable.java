package edu.sdccd.cisc191.common.errors;

public class Unreachable extends Exception {
    public Unreachable() {
        super("This code is not supposed to execute");
    }
}
