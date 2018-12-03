package com.example.elie.driverapp;

import android.content.ComponentName;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import  android.content.Intent;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elie.driverapp.R;
import com.example.elie.driverapp.Model.DS.FireBase_DSManager;

import com.example.elie.driverapp.Model.Entities.*;

import java.util.List;


public class MainActivity extends AppCompatActivity
{

    public static final String mypreference = "myKeyWords";
    public static final String Name = "Pseudo";
    public static final String Pass = "Password";

    //region ***** Fields *****


    public static int i=4;

    Button SignIn;
    Button Login;
    EditText Pseudo;
    EditText Password;
    SharedPreferences sharedPreferences;


    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FindViews();
        sharedPreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
        if(sharedPreferences.contains(Name))
            Pseudo.setText(sharedPreferences.getString(Name,""));
        if(sharedPreferences.contains(Pass))
            Password.setText(sharedPreferences.getString(Pass,""));

       FireBase_DSManager.notifyToDriverList(new FireBase_DSManager.NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> obj)
            {
                Toast.makeText(getBaseContext(),"Essai",Toast.LENGTH_LONG).show();

            }

            @Override
            public void OnFailure(Exception exception)
            {


            }
        });

    }

    public void Store(View view)
    {
        String name=Pseudo.getText().toString();
        String password=Password.getText().toString();

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Name,name);
        editor.putString(Pass,password);
        editor.commit();
        //Toast.makeText(getBaseContext(),"DataStoredSuccessfully",Toast.LENGTH_SHORT).show();

    }
    /*public void Fetch(View view)
    {
       sharedPreferences = getSharedPreferences(mypreference,Context.MODE_PRIVATE);
       if(sharedPreferences.contains(Name))
           Pseudo.setText(sharedPreferences.getString(Name,""));
        if(sharedPreferences.contains(Pass))
            Password.setText(sharedPreferences.getString(Pass,""));
    }*/

    private void FindViews()
    {
        SignIn = (Button)findViewById(R.id.SignIn);
        Login=(Button)findViewById(R.id.Login);
        Password=(EditText)findViewById(R.id.Password);
        Pseudo=(EditText)findViewById(R.id.Pseudo);



        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Store(v);
                Driver d= new Driver(i);
                FireBase_DSManager fireBase_dsManager = new FireBase_DSManager();
                fireBase_dsManager.addDriver(d);
                i++;
            }


        });


        SignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                ComponentName componentName = new ComponentName(MainActivity.this,RegisterActivity.class);
                Intent myintent=new Intent();
                myintent.setComponent(componentName);
                startActivity(myintent);
            }
            });

        }





}