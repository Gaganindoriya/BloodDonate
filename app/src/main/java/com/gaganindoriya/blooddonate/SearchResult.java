package com.gaganindoriya.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchResult extends AppCompatActivity implements ListView.OnItemClickListener {
    private ListView listView;

    private String JSON_STRING;
    Toolbar toolbar;
    String BloodGroup;
    String State;
    LinearLayout result_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = findViewById(R.id.toolbar);
        result_layout=findViewById(R.id.result_activity);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_back);

        Intent i=getIntent();
        BloodGroup=i.getStringExtra("BLOOD_GROUP").toString();
        State=i.getStringExtra("STATE").toString();




        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        //check connectivity
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;
        if(connected){
            getJSON();
        }else{
            Toast.makeText(SearchResult.this, "No Internet Connection ,Please Switch on Your Intenet data..", Toast.LENGTH_SHORT).show();
            SearchResult.this.finish();
        }
    }

    private void showDonor(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(Config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String bloodgroup = jo.getString(Config.TAG_BLOOD_GROUP);
                String state = jo.getString(Config. KEY_STATE);
                if(bloodgroup.equals(BloodGroup)&&state.equals(State)){
                    String id = jo.getString(Config.TAG_ID);
                    String name = jo.getString(Config.KEY_NAME );
                    String contact = jo.getString(Config. KEY_PHONE);

                    String city = jo.getString(Config. KEY_CITY);

                    HashMap<String,String> employees = new HashMap<>();
                    employees.put(Config.TAG_ID,"Id="+id);
                    employees.put(Config.TAG_NAME,name);
                    employees.put(Config.TAG_PHONE,contact);
                    employees.put(Config.TAG_BLOOD_GROUP,bloodgroup);
                    employees.put(Config.TAG_STATE,state);
                    employees.put(Config.TAG_CITY,city);
                    list.add(employees);
                }
                else{
                    continue;
                }


            }
            if(list.isEmpty()){
                result_layout.setBackgroundResource(R.drawable.oops);
                Toast.makeText(this,"Sorry No User Found", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                SearchResult.this, list, R.layout.list_item,
                new String[]{Config.TAG_ID,Config.TAG_NAME,Config.TAG_PHONE,Config.TAG_BLOOD_GROUP,Config.TAG_STATE,Config.TAG_CITY},
                new int[]{R.id.id, R.id.name,R.id.contact, R.id.bloodGroup, R.id.state, R.id.city});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SearchResult.this,"Fetching Data","Wait...",false,true);
            }
            @Override
            protected String doInBackground(Void... params) {
//                HashMap<String,String> paramsSend = new HashMap<>();
//                paramsSend.put(Config.KEY_BLOOD_GROUP,BloodGroup);
//                paramsSend.put(Config.KEY_STATE,State);
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(Config.URL_GET_ALL);

                return s;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s=="") {
                    Toast.makeText(SearchResult.this, "No data found", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    JSON_STRING = s;
                    showDonor();
                }

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);

        String uphone = map.get(Config.TAG_PHONE).toString();

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+uphone));
        startActivity(intent);
    }




}
