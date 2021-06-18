package com.example.loanit2;

/*Dianne Scott*/

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private ArrayList<Loans> mLoanList;

    //Constructor for the LoanAdapter
    public LoanAdapter(ArrayList<Loans> list){
        mLoanList = list;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_list_item, parent, false);
        LoanViewHolder mLoanViewHolder = new LoanViewHolder(v);
        return mLoanViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LoanAdapter.LoanViewHolder holder, int position) {
        Loans currentLoan = mLoanList.get(position);
        holder.mTextViewName.setText(String.format("Name: %s", currentLoan.getBorrowerName()));
        holder.mTextViewLoanInfo.setText(String.format("Loan number: %d\nItem info: %s", currentLoan.getLoanNumber(), currentLoan.getItemAndDesc()));

    }

    //Returns the size of the ArrayList
    @Override
    public int getItemCount() {
        return mLoanList.size();
    }

    //Inner class that helps set up the LoanViewHolder
    public static class LoanViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextViewName;
        public TextView mTextViewLoanInfo;

        public LoanViewHolder(View v){
            super(v);
            mTextViewName = v.findViewById(R.id.tv_loan_list_name);
            mTextViewLoanInfo = v.findViewById(R.id.tv_loan_list_loan_info);
        }
    }
}
