package com.example.elie.driverapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elie.driverapp.Model.Entities.Driver;
import com.example.elie.driverapp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity
{

    EditText Name;
    EditText ID;
    EditText Mail;
    EditText Phone;
    EditText Password1;
    EditText Password2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FindViews();
        login();
    }

    private void FindViews()
    {
        Name=(EditText)findViewById(R.id.Name);
        Phone=(EditText) findViewById(R.id.Phone);
        ID=(EditText) findViewById(R.id.ID);
        Password1=(EditText)findViewById(R.id.Password1);
        Password2=(EditText)findViewById(R.id.Password2);
        Mail=(EditText)findViewById(R.id.Mail);

    }


    private void login(){
        if (TextUtils.isEmpty(ID.getText().toString().trim())||TextUtils.isEmpty(Name.getText().toString().trim())||
                TextUtils.isEmpty(Phone.getText().toString().trim())||TextUtils.isEmpty(Mail.getText().toString().trim())||
                TextUtils.isEmpty(Password1.getText().toString().trim()) ||TextUtils.isEmpty(Mail.getText().toString().trim()))
        {
            ID.setError("Fields can't be Empty");
            Name.setError("Fields can't be Empty");
            Phone.setError("Fields can't be Empty");
            Mail.setError("Fields can't be Empty");
            Password1.setError("Fields can't be empty");
            Password2.setError("Fields can't be empty");
        }
        else if (!emailValidator(Mail.getText().toString()))
        {
            Mail.setError("Please Enter Valid Email Address");
        }
        else
        {
            Toast.makeText(this ,"Login Successful",Toast.LENGTH_SHORT).show();
        }

        if (Password1.getText().equals(Password2.getText()) == false)
            Password2.setError("The password are not the same");
    }

    //Email Validation Using Regex
    public boolean emailValidator (String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN ="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Driver getDriver()
    {
        Driver d=new Driver();
        d.setPhone(Phone.getText().toString());
        d.setName(Name.getText().toString());
        d.setMail(Mail.getText().toString());
        d.setID(Integer.valueOf(ID.getText().toString()));

        return d;
    }


}
