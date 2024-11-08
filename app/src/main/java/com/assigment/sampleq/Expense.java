package com.assigment.sampleq;

public class Expense {
    private  String title;
    private  double amount;
    private  int participants;
    public  Expense(String title,double amount,int participants){
        this.title=title;
        this.amount=amount;
        this.participants=participants;
    }
    public  String getTitle(){
        return  title;
    }
    public  double getAmount(){
        return  amount;
    }
    public  int getParticipants(){
        return  participants;
    }


}
