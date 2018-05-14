package com.gaganindoriya.blooddonate;
//this project is creted by gagan kumar indoriya after udacity google scholarship challenge
//on 14-may-2018

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.gaganindoriya.blooddonate.utill.SessionManager;

import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private DrawerLayout mDrawerLayout;
    private Spinner spinnertState;
    private Button buttonSearch;
    private Spinner spinnertBloodGroup;
    String email=null;
    String pass=null;
        // Session Manager Class
        SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        Intent intent=getIntent();
        //to check if user is already logged ni or not
        if(intent.getBooleanExtra("SKIPFLAG",false)){
            Toast.makeText(this, "You skip Login", Toast.LENGTH_SHORT).show();
        }else {
            session.checkLogin();
            HashMap<String, String> user = session.getUserDetails();
            email = user.get(SessionManager.KEY_EMAIL);
            // pass
            pass = user.get(SessionManager.KEY_PASS);
        }
        // get user data from session




        // email

//        if(getIntent()!=null){
//
//            email=getIntent().getStringExtra("EMAIL");
//            pass=getIntent().getStringExtra("PASS");
//        }

        //uncomment this for chnging app language
        //changeLanguage();
//        buttons and spinner initializations
        spinnertBloodGroup = (Spinner) findViewById(R.id.spinnerBloodGroup);
        spinnertState = (Spinner) findViewById(R.id.spinnerState);
        buttonSearch=(Button)findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);


        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.nav_profile: {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        if (email == null && pass == null) {

                            //Toast.makeText(MainActivity.this, "You are not logged in !", Toast.LENGTH_SHORT).show();
                            displayDialog();
                            break;
                        } else {
                            Intent intent = new Intent(MainActivity.this, MyProfile.class);
                            intent.putExtra("EMAIL", email);
                            intent.putExtra("PASS", pass);
                            startActivity(intent);
                            break;
                        }
                    }
                    case R.id.nav_logout:
                        session.logoutUser();
                        break;
                    case R.id.nav_exit:
                        Toast.makeText(MainActivity.this, "finishing menu", Toast.LENGTH_SHORT).show();
                        item.setChecked(false);
                        mDrawerLayout.closeDrawers();
                        finish();
                        break;
                    default:

                }
                return true;
            }
        });

//        initizlization of spinners

        initBloodGroupSpinner();
        initState();
    }

//    this will execute when button is clicked
    @Override
    public void onClick(View v) {
        if(v == buttonSearch){
            searchDonor();
        }

    }

    //this will take use to the next activity and show searhc results there
    public void searchDonor(){

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
            String bloodGroup = spinnertBloodGroup.getSelectedItem().toString().trim();
            String state =spinnertState.getSelectedItem().toString().trim();
            Intent intent=new Intent(this,SearchResult.class);
            intent.putExtra("BLOOD_GROUP",bloodGroup);
            intent.putExtra("STATE",state);
            startActivity(intent);
        }else{
            Toast.makeText(MainActivity.this, "No Internet Connection ,Please Switch on Your Intenet data..", Toast.LENGTH_SHORT).show();

        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.developer:
               Intent i=new Intent(MainActivity.this,Developer.class);
               startActivity(i);



        }
        return super.onOptionsItemSelected(item);
    }
    public void initBloodGroupSpinner(){

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

    public void initState(){

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
    public void displayDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Oops!");

        // Setting Dialog Message
        alertDialog.setMessage("You came here by skipping login , Do you want to Login/register now ?");

        // Setting Icon to Dialog
       // alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

//    public void changeLanguage(){
//        String languageToLoad  = "hi"; // your language
//        Locale locale = new Locale(languageToLoad);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.locale = locale;
//        getBaseContext().getResources().updateConfiguration(config,
//                getBaseContext().getResources().getDisplayMetrics());
//    }

}
