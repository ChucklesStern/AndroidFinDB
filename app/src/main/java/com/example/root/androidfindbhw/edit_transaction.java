package com.example.root.androidfindbhw;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class edit_transaction extends AppCompatActivity {
@BindView(R.id.edittransView)
    TextView transView_;

    @BindView(R.id.edit_description)
    EditText editDescription_;

    @BindView(R.id.edit_amount)
    EditText editAmount_;

    @BindView(R.id.btn_edit)
    Button buttonEdit_;

    @BindView(R.id.getAllTransaction)
    Button backToAll_;

    JSONArray transArray;
    String indexString;
    String responseString;
    String type = "finupdate";

    String transData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaction);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();

        transData = b.getString("myObject");

        transView_.setText(transData);
    }

    @OnClick(R.id.btn_edit)
    public void editOne(View view) {
        validate();
        if (validate() == true) {
            buttonEdit_.setEnabled(false);
            try {
                transArray = new JSONArray(transData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int num = transArray.length();
            String numString = Integer.toString(num);
            Log.d("ListAdapter Count", numString);
            try {
                indexString = transArray.getString(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String Description_ = editDescription_.getText().toString();
            if (Description_ == "") {
                try {
                    Description_ = transArray.getString(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String preAmount = editAmount_.getText().toString();
            Log.d("INDEX", indexString);
            Toast.makeText(getApplicationContext(), "Editing Item at Index:" + indexString, Toast.LENGTH_SHORT).show();

            BackgroundApi backgroundApi = new BackgroundApi(new Createtransaction.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    Log.d("response", output);
                    responseString = output;
                    final String responseTest = "User not found";
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    // On complete call either onLoginSuccess or onLoginFailed
                                    if (!(responseString.equals(responseTest))) {
                                        onLoginSuccess();
                                        Log.d("Response from Server", (String) responseString);
                                        Toast.makeText(getApplicationContext(), "Edit Confirmed" + responseString, Toast.LENGTH_SHORT).show();
                                    } else if (responseString.equals(responseTest)) {
                                        Log.d("Response String Null!", (String) responseString);
                                        //  onLoginFailed();
                                    }
                                }
                            }, 3000);

                }

            });
            backgroundApi.execute(type, indexString, Description_, preAmount);
        } else {
            Toast.makeText(getBaseContext(), "Valid Entry Required Thanks!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(this, getOneTransaction.class);
        intent.putExtra("myObject", responseString);
        startActivity(intent);
    }

    @OnClick(R.id.getAllTransaction)
    public void toAdd(View view) {
        Intent intent = new Intent(this, getAllTransactions.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        String description = editDescription_.getText().toString();
        String amount = editAmount_.getText().toString();
      //  String email = _emailText.getText().toString();
        //String mobile = _mobileText.getText().toString();
        //String password = _passwordText.getText().toString();
        //String reEnterPassword = _reEnterPasswordText.getText().toString();

        if (description.isEmpty() || description.length() < 1) {
            editDescription_.setError("at least 1 character");
            valid = false;
        } else {
            editDescription_.setError(null);
        }

        if (amount.isEmpty()) {
            editAmount_.setError("Enter 0.00 for a free item");
            valid = false;
        } else {
            editAmount_.setError(null);
        }

        return valid;
    }
}
