package com.forkexec.pts.domain;


public class BalanceInfoPoints {
    private int tag;
    private int balance;

    public BalanceInfoPoints(int balance, int tag) {
        this.balance = balance;
        this.tag = tag;
    }

    
    public int getBalance(){
        return this.balance;
    }

    public int getTag(){
        return this.tag;
    }

    public void setBalance(int balance){
        this.balance = balance;
    }

    public void setTag(int tag){
        this.tag = tag;
    }
}
