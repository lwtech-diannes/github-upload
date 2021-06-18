package com.example.loanit2;

/*Dianne Scott*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RemoveLoanFragment.RemoveLoanFragmentListener {

    private EditText mBorrowerName;
    private EditText mLoanInfo;
    private RecyclerView mRecyclerView;
    private LoanAdapter mAdapter;
    private ArrayList<Loans> mLoanList;
    private final String LOAN_LIST = "loan list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBorrowerName = findViewById(R.id.edit_text_name);
        mLoanInfo = findViewById(R.id.edit_text_loan_info);

        loadLoans();
        buildRecyclerView();
        addLoanButton();
        resetButton();
        removeLoanButton();
        closeKeyboard();

        Button buttonSave = findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoans();
            }
        });
    }

    //region Start "helper methods"

    //Builds the recyclerView, call in onCreate. It keeps the onCreate less crowded and
    //easier to read.
    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_loan_data);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new LoanAdapter(mLoanList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Closes the soft keyboard. Is very useful!
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Saves loans using shared preferences. Creates Gson object and converts the array list
    //to a Json string.
    private void saveLoans() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mLoanList);
        editor.putString(LOAN_LIST, json);
        editor.apply();
    }

    //Loads loans when the app is opened.
    private void loadLoans() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(LOAN_LIST, null);
        Type type = new TypeToken<ArrayList<Loans>>() {
        }.getType();
        mLoanList = gson.fromJson(json, type);

        //Null check if the array list is empty
        if (mLoanList == null) {
            mLoanList = new ArrayList<>();
        }
        closeKeyboard();
    }

    //endregion End "helper methods"

    //region Start "buttons"

    //Creates loan object with user input. Adds loan to the ArrayList.
    private void addLoanButton() {
        Button addLoan = findViewById(R.id.button_add);
        addLoan.setOnClickListener(new View.OnClickListener() {

            //For generating random numbers for the loans
            Random rand = new Random();

            @Override
            public void onClick(View v) {
                mLoanList.add(new Loans(mBorrowerName.getText().toString(), mLoanInfo.getText().toString(), rand.nextInt(1001)));
                mAdapter.notifyItemInserted(mLoanList.size());
                closeKeyboard();
                mBorrowerName.setText("");
                mLoanInfo.setText("");
            }
        });
    }

    //Resets the array list
    public void resetButton() {
        Button reset = findViewById(R.id.button_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mLoanList.isEmpty()) {
                    mLoanList.clear(); //clears the list
                    mAdapter.notifyDataSetChanged(); //sends changes to the adapter and reloads
                    closeKeyboard();
                }
            }
        });
    }

    //When user clicks this button it calls the openFragment method to open the dialog fragment
    private void removeLoanButton() {
        Button remove = findViewById(R.id.button_remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });
    }

    //endregion End "buttons"

    //Opens dialog fragment for user to enter loan to remove
    private void openFragment() {
        RemoveLoanFragment fragment = new RemoveLoanFragment();
        fragment.show(getSupportFragmentManager(), "remove loan fragment");

    }

    //Receives the loan number from the RemoveLoanFragment, removes the loan object that
    //contains the corresponding loan number. Need to figure out how to handle incorrect loan
    //numbers.
    @Override
    public void removeLoan(int loanNumber) {
        if (!mLoanList.isEmpty()) {
            for (int i = 0; i < mLoanList.size(); i++) {
                if (mLoanList.get(i).getLoanNumber() == loanNumber) {
                    mLoanList.remove(i);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "LOAN NUMBER: " + loanNumber + " REMOVED!", Toast.LENGTH_SHORT).show();
                    closeKeyboard();
                }
            }
        }
    }

    //region Start "menu methods"

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loan_it_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_info:
                closeKeyboard();
                showInstructions(item);
                return true;
            case R.id.menu_exit:
                closeKeyboard();
                showExit(item);
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    //Displays the alert dialog for the user to select
    public void showExit(MenuItem item) {
        //Build the AlertDialog
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MainActivity.this);
        myAlertBuilder.setTitle(R.string.Exit);
        myAlertBuilder.setMessage(R.string.Exit_message);

        //AlertDialog buttons
        myAlertBuilder.setPositiveButton(R.string.Alert_Yes, new
                DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        //Exit the app
                        finishAffinity();
                        System.exit(0);
                    }
                });
        myAlertBuilder.setNegativeButton(R.string.Alert_No, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //User decided to stay, display toast message
                        Toast.makeText(getApplicationContext(), "Thanks for staying!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Show the AlertDialog.
        myAlertBuilder.show();
    }
    
    //Displays a brief message about how to use the app. 
    public void showInstructions(MenuItem item) {
        //Build the AlertDialog
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(MainActivity.this);
        final AlertDialog.Builder builder = myAlertBuilder.setTitle(R.string.title_instructions);
        final AlertDialog.Builder setMessage = myAlertBuilder.setMessage(R.string.info_message);

        myAlertBuilder.setNegativeButton(R.string.menu_ok, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Keep track of your stuff!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //Show the AlertDialog.
        myAlertBuilder.show();
    }

    //endregion End "menu methods"
}


