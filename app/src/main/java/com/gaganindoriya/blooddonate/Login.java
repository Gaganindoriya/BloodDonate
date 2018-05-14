package com.gaganindoriya.blooddonate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gaganindoriya.blooddonate.model.User;
import com.gaganindoriya.blooddonate.utill.SessionManager;

import java.util.HashMap;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin;
    EditText etLogin,etPass;
    //session variable
    // Session Manager Class
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin=findViewById(R.id.login_btn);
        etLogin=findViewById(R.id.login_email);
        etPass=findViewById(R.id.login_pass);
        btnLogin.setOnClickListener(this);
        session = new SessionManager(getApplicationContext());
        if(session.isLoggedIn()){
            Intent intent= new Intent(Login.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public void forgotPass(View view) {
        Intent intent= new Intent(Login.this,ForgotPassword.class);
        startActivity(intent);
    }

    public void registerUser(View view) {
        Intent intent= new Intent(Login.this,RegisterActivity.class);
        startActivity(intent);
    }
    public void skip(View view) {
        Intent intent= new Intent(Login.this,MainActivity.class);
        intent.putExtra("SKIPFLAG",true);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v == btnLogin){
            doLogin();
        }
    }
    public void doLogin(){

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
            String loginID=etLogin.getText().toString();
            String loginPass=etPass.getText().toString();
            //calling perform login method for backend process
            performLogin(loginID,loginPass);
        }else{
            Toast.makeText(Login.this, "No Internet Connection ,Please Switch on Your Intenet data..", Toast.LENGTH_SHORT).show();

        }

    }

    public void performLogin(final String id, final String pass){
        //for registration of user
        //Adding an Donor


            //class for doing asymc task
            class LoginUser extends AsyncTask<Void,Void,String> {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Login.this,"Loggin in...","Please Wait...",false,false);

                }



                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();
                    params.put(Config.KEY_EMAIL,id);
                    params.put(Config.KEY_PASS,pass);




                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_GET_LOGIN, params);
                    return res;
                }
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();

                    if(s.equals(" 1")) {
                        session.createLoginSession(etLogin.getText().toString(), etPass.getText().toString());
                        MainScreen(true);
                    }else
                        {
                            Toast.makeText(Login.this, "wrong email or passwrod", Toast.LENGTH_LONG).show();
                        }


                }
            }

            //calling async task executing async task

        LoginUser loginUser = new LoginUser();
        loginUser.execute();
        }


        public void MainScreen(boolean flag){
            if(flag) {
                Intent intent = new Intent(Login.this, MainActivity.class);
//                intent.putExtra("EMAIL",etLogin.getText().toString());
//                intent.putExtra("PASS",etPass.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }

}
