package com.test.wheely.wheelytest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import autobahn.WebSocketConnection;
import autobahn.WebSocketConnectionHandler;
import autobahn.WebSocketException;

/**
 * Created by Александр on 05.08.2014.
 */
public class LoginActivity extends ActionBarActivity {

    private EditText edit_username;
    private EditText edit_password;
    private Button connect;
    public static Context mContext;


    public void setUpgradeButton(int title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customBar = View.inflate(getApplicationContext(), R.layout.custom_actionbar, null);
        actionBar.setCustomView(customBar);
        TextView textTitle = (TextView) findViewById(R.id.txtTitle);
        textTitle.setText(title);


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edit_username=(EditText) findViewById(R.id.username);
        edit_password=(EditText) findViewById(R.id.password);
        connect=(Button) findViewById(R.id.connect);
        final Intent i=new Intent(this, MapsActivity.class);
        mContext=this;
        setTitle("Login");
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog=new AlertDialog.Builder(mContext);
                dialog.setTitle("Message");
                dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                if(edit_username.getText().toString().substring(0, 1).matches("a")){
                    if(edit_password.getText().toString().substring(0, 1).matches("a")){
//                        scheduleAlarm();
                        i.putExtra("username",edit_username.getText().toString());
                        i.putExtra("password",edit_password.getText().toString());
                        App.username=edit_username.getText().toString();
                        App.password=edit_password.getText().toString();

                        startActivity(i);

                          }
                    else{
                        dialog.setMessage("Incorrect password");
                        dialog.show();

                    }
                }
                else{

                    dialog.setMessage("Incorrect username");
                   dialog.show();
                }
            }
        });
    }



}



