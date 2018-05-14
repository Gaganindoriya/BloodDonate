package com.gaganindoriya.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gaganindoriya.blooddonate.model.User;

import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    User user=null;

    private Spinner spinnertBloodGroup;
    private Spinner spinnertState;
    EditText etname,etphone,etcity,etemail,etpass;
//    EditText etname, etemail, etpassword, etdob, etphone;
//    RadioButton etgenderMale, etgenderFemale;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        iniViews();
        btnRegister.setOnClickListener(this);
    }

    //intitializing edittexts
    private void iniViews() {
        //etname,etemail,etpassword,etdob,etgender,etaddress,etcity,etphone,etbloodGroup;

        etname = findViewById(R.id.name);
        etphone=findViewById(R.id.phone);
        spinnertBloodGroup=findViewById(R.id.spinnerBloodGroup);
        spinnertState=findViewById(R.id.spinnerState);
        etcity=findViewById(R.id.city);
        etemail=findViewById(R.id.email);
        etpass=findViewById(R.id.password);
        btnRegister=findViewById(R.id.registerbtn);

        initBloodGroupSpinner();
        initState();


    }

    //    this will execute when button is clicked


    //this will take use to the next activity and show searhc results there


    public void initBloodGroupSpinner() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Blood_Group, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnertBloodGroup.setAdapter(adapter);
        spinnertBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    public void initState() {

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.india_states, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnertState.setAdapter(adapter);
        spinnertState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                return true;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if( validate()){
            String name=etname.getText().toString();
            String phone=etphone.getText().toString();
            String blood_group=spinnertBloodGroup.getSelectedItem().toString().trim();
            String state=spinnertState.getSelectedItem().toString().trim();
            String city=etcity.getText().toString();
            String email=etemail.getText().toString();
            String pass=etpass.getText().toString();


            //creting user object
             user=new User();
            user.setName(name);
            user.setPhone(phone);
            user.setBloodgroup(blood_group);
            user.setState(state);
            user.setCity(city);
            user.setEmail(email);
            user.setPassword(pass);
            Toast.makeText(this, "All data correct ready to send", Toast.LENGTH_SHORT).show();
            register(user);


        }


    }
    private boolean validate() {
        String name=etname.getText().toString();
        String phone=etphone.getText().toString();
        String blood_group=spinnertBloodGroup.getSelectedItem().toString().trim();
        String state=spinnertBloodGroup.getSelectedItem().toString().trim();
        String city=etcity.getText().toString();
        String email=etemail.getText().toString();
        String pass=etpass.getText().toString();

        if (name.length() == 0|| phone.length()<10||city.length()==0||email.length()==0) {

            Toast.makeText(getApplicationContext(), "pls fill the empty fields", Toast.LENGTH_SHORT).show();
            if(pass.length()<6){
                Toast.makeText(getApplicationContext(), "password should be atleast 6 digit", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;

        }
        return true;

    }
    //for registration of user
    //Adding an Donor

    private void register(final User user){

        //class for doing asymc task
        class RegisterUser extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RegisterActivity.this,"Adding...","Wait...",false,false);

            }



            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_NAME, user.getName());
                params.put(Config.KEY_PHONE,user.getPhone());
                params.put(Config.KEY_BLOOD_GROUP,user.getBloodgroup());
                params.put(Config.KEY_STATE,user.getState());
                params.put(Config.KEY_CITY,user.getCity());
                params.put(Config.KEY_EMAIL,user.getEmail());
                params.put(Config.KEY_PASS,user.getPassword());




                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_ADD, params);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Toast.makeText(RegisterActivity.this, s , Toast.LENGTH_LONG).show();
                Intent intent=new Intent(RegisterActivity.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();


            }
        }

        //calling async task executing async task

        RegisterUser ae = new RegisterUser();
        ae.execute();
    }

    //for sending data to the database





}


