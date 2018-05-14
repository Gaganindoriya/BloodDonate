package com.gaganindoriya.blooddonate;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class SearchDonor extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private DrawerLayout drawerLayout;
    private Spinner spinnertBloodGroup;
    private Spinner spinnertState;
    private Button buttonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);
        //Initializing views
        spinnertBloodGroup = (Spinner) findViewById(R.id.spinnerBloodGroup);
        spinnertState = (Spinner) findViewById(R.id.spinnerState);
        buttonSearch=(Button)findViewById(R.id.buttonSearch);

        buttonSearch.setOnClickListener(this);
        //initialise bloodgroup spinner

        initBloodGroupSpinner();
        initState();



        //initializing ,setting  toolbar
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
                    case R.id.nav_profile:
                        Toast.makeText(SearchDonor.this, "Profile menu", Toast.LENGTH_SHORT).show();
                        item.setChecked(false);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.nav_exit:
                        Toast.makeText(SearchDonor.this, "finishing menu", Toast.LENGTH_SHORT).show();
                        item.setChecked(false);
                        mDrawerLayout.closeDrawers();
                        finish();
                    default:

                }
                return true;
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //MENU of navigation sliding drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id){

            case R.id.developer:
                Toast.makeText(this,"developer is choosed", Toast.LENGTH_LONG).show();
                Intent intent =new Intent(this,Developer.class);
                startActivity(intent);
                break;




        }

        return super.onOptionsItemSelected(item);
    }
    //code of share button
    public void shareThisApp(){
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra("android.intent.extra.SUBJECT","My first Share");
        shareIntent.setType("text/plain");
        startActivity(shareIntent);

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

    @Override
    public void onClick(View v) {
        if(v == buttonSearch){
            searchDonor();
        }

    }

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
            Toast.makeText(SearchDonor.this, "No Internet Connection ,Please Switch on Your Intenet data..", Toast.LENGTH_SHORT).show();

        }

    }
}

