package com.example.root.androidfindbhw;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import static android.R.id.list;

public class getAllTransactions extends AppCompatActivity {
    @BindView(R.id.allTransactionsList)
    ListView allTList;

    @BindView(R.id.addOne)
            Button backToAdd;


    //Body strings are empty as GetALL requires no body arguements
    String empty1 ="";
    String empty2 ="";
    String responseString;

    JSONObject jsonObject;
    Object object;
    ArrayList<Object> transList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_get_all_transactions);
        ButterKnife.bind(this);

        String type = "fingetall";

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
                                } else if (responseString.equals(responseTest)) {
                                    Log.d("Response String Null!", (String) responseString);
                                  //  onLoginFailed();
                                }
                            }
                        }, 100);

            }

        });
        backgroundApi.execute(type, empty1, empty2);



    }


        public void convert() throws JSONException
        {
            JSONArray jsonArray = new JSONArray(responseString);

            for (int i = 0; i < jsonArray.length(); i++)
            {
               object = jsonArray.get(i);
                transList.add(object);

            }

        }

        @OnClick(R.id.addOne)
        public void toAdd(View view) {
            Intent intent = new Intent(this, Createtransaction.class);
            startActivity(intent);
        }

    public void onLoginSuccess () {

        try {
            convert();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayAdapter<Object> listAdapter = new ArrayAdapter<Object>(this, R.layout.simplerow, transList);
        getCount();
        allTList.setAdapter(listAdapter);


        allTList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getOneTransaction(position,listAdapter);


/* for deleting item locally
            Object killItem = listAdapter.getItem(position);
                listAdapter.remove(killItem);
                listAdapter.notifyDataSetChanged();
                int num = listAdapter.getCount();
                String numString = Integer.toString(num);
                Log.d("ListAdapter Count", numString);
                Toast.makeText(getApplicationContext(), numString, Toast.LENGTH_SHORT).show();
                */

            }
        });
        Toast.makeText(getBaseContext(), "Click on each Item for Detailed View. Thanks!", Toast.LENGTH_LONG).show();
    }


    public int getCount() {
        return transList.size();
    }

    public void getOneTransaction(int position, ArrayAdapter listAdapter) {
        Object killItem = listAdapter.getItem(position);
        String killItemString = killItem.toString();
        Intent intent = new Intent(this, getOneTransaction.class);
        intent.putExtra("myObject", killItemString);
        startActivity(intent);
    }


}
