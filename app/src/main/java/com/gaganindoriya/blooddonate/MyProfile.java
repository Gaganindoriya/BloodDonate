package com.gaganindoriya.blooddonate;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gaganindoriya.blooddonate.model.User;
import com.gaganindoriya.blooddonate.utill.SessionManager;

import java.util.HashMap;

public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    User user=null;

    private Spinner spinnertBloodGroup;
    private Spinner spinnertState;
    EditText etname,etphone,etcity,etemail,etpass;
    //    EditText etname, etemail, etpassword, etdob, etphone;
//    RadioButton etgenderMale, etgenderFemale;
    Button btnupdate,btnDelete;
    Intent dataIntent;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        dataIntent=getIntent();
        iniViews();
        btnupdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        //getting pref file
        session = new SessionManager(getApplicationContext());

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
        btnupdate=findViewById(R.id.updatebtn);
        btnDelete=findViewById(R.id.deletebtn);

        etemail.setText(dataIntent.getStringExtra("EMAIL"));
        etpass.setText(dataIntent.getStringExtra("PASS"));

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
        if(v==btnupdate) {
            if (validate()) {
                String name = etname.getText().toString();
                String phone = etphone.getText().toString();
                String blood_group = spinnertBloodGroup.getSelectedItem().toString().trim();
                String state = spinnertState.getSelectedItem().toString().trim();
                String city = etcity.getText().toString();
                String email = etemail.getText().toString();
                String pass = etpass.getText().toString();


                //creting user object
                user = new User();
                user.setName(name);
                user.setPhone(phone);
                user.setBloodgroup(blood_group);
                user.setState(state);
                user.setCity(city);
                user.setEmail(email);
                user.setPassword(pass);
                Toast.makeText(this, "All data correct ready to send", Toast.LENGTH_SHORT).show();
                updateUser(user);


            }
        }
        if(v==btnDelete){
            String email = etemail.getText().toString();
            String pass = etpass.getText().toString();

            user = new User();
            user.setEmail(email);
            user.setPassword(pass);

            displayDialog(user);
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

    private void updateUser(final User user){

        //class for doing asymc task
        class RegisterUser extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MyProfile.this,"Updating...","Wait...",false,false);

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
                String res = rh.sendPostRequest(Config.URL_GET_UPDATE, params);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(!s.isEmpty()) {
                    Toast.makeText(MyProfile.this, s, Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MyProfile.this, "Error occure , not updated", Toast.LENGTH_SHORT).show();
                }



            }
        }

        //calling async task executing async task

        RegisterUser ae = new RegisterUser();
        ae.execute();
    }

    //for deletion
    private void deleteUser(final User user){

        //class for doing asymc task
        class Delete extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MyProfile.this,"Deleting...","Please Wait...",false,false);

            }



            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(Config.KEY_EMAIL,user.getEmail());
                params.put(Config.KEY_PASS,user.getPassword());

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Config.URL_GET_DELETE, params);
                return res;
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //clearing pref data
                session.logoutUser();
                Toast.makeText(MyProfile.this, s , Toast.LENGTH_LONG).show();
                Intent intent=new Intent(MyProfile.this,Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loading.dismiss();
                startActivity(intent);

                finish();


            }
        }

        //calling async task executing async task

        Delete del = new Delete();
        del.execute();
    }

    //for sending data to the database

    public void displayDialog(final User user){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyProfile.this);

        // Setting Dialog Title
        alertDialog.setTitle("Alert!");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want to delet your account?");

        // Setting Icon to Dialog
        // alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                //tot delete from database
                Toast.makeText(MyProfile.this, "Deleting..", Toast.LENGTH_SHORT).show();
                deleteUser(user);

            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



}
