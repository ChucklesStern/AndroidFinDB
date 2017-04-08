package com.example.root.androidfindbhw;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class getOneTransaction extends AppCompatActivity {
@BindView(R.id.singleTransaction)
    TextView oneTrans;

    @BindView(R.id.delete_btn)
    Button deleteTrans_;

    @BindView(R.id.edit_button)
            Button editButton_;

    @BindView(R.id.getAllT)
            Button getAllT_;

    String transData;
    JSONArray transArray;
    String indexString;
    String type = "findelete";
    String responseString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_one_transaction);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();

        transData = b.getString("myObject");
        oneTrans.setText(transData);
    }
    @OnClick(R.id.getAllT)
    public void getAllT_(View view){
        Intent intent = new Intent(this, getAllTransactions.class);
        startActivity(intent);
    }

@OnClick(R.id.edit_button)
    public void editOne(View view) {
    Intent intent = new Intent(this, edit_transaction.class);
    intent.putExtra("myObject", transData);
    startActivity(intent);
}
@OnClick(R.id.delete_btn)
    public void deleteOne(View view) {
    deleteTrans_.setEnabled(false);
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
    Log.d("INDEX", indexString);
    Toast.makeText(getApplicationContext(),"Deleting Item at Index:" + indexString, Toast.LENGTH_SHORT).show();

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
                                Toast.makeText(getApplicationContext(),"Delete Confirmed" + responseString, Toast.LENGTH_SHORT).show();
                            } else if (responseString.equals(responseTest)) {
                                Log.d("Response String Null!", (String) responseString);
                                //  onLoginFailed();
                            }
                        }
                    }, 100);

        }

    });
    backgroundApi.execute(type, indexString);
}

public void onLoginSuccess() {
    Intent intent = new Intent(this, getAllTransactions.class);
    startActivity(intent);
}
}
