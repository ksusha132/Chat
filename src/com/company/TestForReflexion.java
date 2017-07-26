package com.company;


public class TestForReflexion implements TestForReflx{
    private int x = 10;
    Integer i;
    String s;

    TestForReflexion(){
        this.i = 10;
        this.s = "times";
    }

    @Override
    public Integer sum(int i) {
        return i + i;
    }
}
