package com.example.loanit2;

/*Dianne Scott*/

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

public class RemoveLoanFragment extends DialogFragment {

    private EditText mLoanNumber;
    private RemoveLoanFragmentListener listener;

    public RemoveLoanFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_remove_loan, null);

        builder.setView(view)
                // Add action buttons
                .setTitle("Remove Loan")
                .setPositiveButton(R.string.remove_loan, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //get loan number from edit text (parses as a string but will cast to int)
                        String  temp = mLoanNumber.getText().toString();
                        //temp to hold parsed loan number
                        int loanNumber = 0;
                        //null check
                        if(!"".equals(temp)){
                            //parse string from edit text to loanNumber int
                            loanNumber = Integer.parseInt(temp);
                        }
                        //send loan number back to main activity
                        listener.removeLoan(loanNumber);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RemoveLoanFragment.this.getDialog().cancel();
                    }
                });
        mLoanNumber = view.findViewById(R.id.loan_number);
        return builder.create();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RemoveLoanFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement RemoveLoanFragmentListener");
        }
    }

    //interface method to implement in main activity
    public interface RemoveLoanFragmentListener {
        void removeLoan(int loanNumber);
    }

}