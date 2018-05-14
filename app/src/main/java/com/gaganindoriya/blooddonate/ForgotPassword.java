package com.gaganindoriya.blooddonate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gaganindoriya.blooddonate.model.User;

import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity {
    EditText et;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        et=findViewById(R.id.recovery_email);
        btn=findViewById(R.id.resetbtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doResetPass();
            }
        });
    }

    public void doResetPass(){


            //class for doing asymc task
            class Delete extends AsyncTask<Void,Void,String> {

                ProgressDialog loading;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(ForgotPassword.this,"Sending password...","Please Wait...",false,false);

                }



                @Override
                protected String doInBackground(Void... v) {
                    HashMap<String,String> params = new HashMap<>();

                    params.put(Config.KEY_EMAIL,et.getText().toString());


                    RequestHandler rh = new RequestHandler();
                    String res = rh.sendPostRequest(Config.URL_GET_RESET, params);
                    return res;
                }
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();

                    if(s.isEmpty()){
                        Toast.makeText(ForgotPassword.this, "empty result" , Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(ForgotPassword.this, s, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPassword.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }


                }
            }

            //calling async task executing async task

            Delete del = new Delete();
            del.execute();
        }
    }

