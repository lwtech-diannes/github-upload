package com.example.loanit2;

/*Dianne Scott*/

import java.util.Date;

public class Loans {

    private String itemAndDesc;
    private String borrowerName;
    private int loanNumber;


    public Loans(String borrowerName, String itemAndDesc, int loanNumber){
        this.itemAndDesc = itemAndDesc;
        this.borrowerName = borrowerName;
        this.loanNumber = loanNumber;
    }

    public String getItemAndDesc() {
        return itemAndDesc;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public int getLoanNumber(){
        return loanNumber;
    }

}
