package com.example.dmitrij.myplacetogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.dmitrij.myplacetogo.json_objects.Client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Registeration extends ActionBarActivity {
    String name, pass, email, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        Client client;
        {
            Button image = (Button) findViewById(R.id.registrationPhoto);

            EditText passV = (EditText) findViewById(R.id.registrationPass);
            EditText passCV = (EditText) findViewById(R.id.registrationPassConfirm);
            pass = passV.getText().toString();
            String passConfig = passCV.getText().toString();
            if (pass != passConfig)
                validationFails("Your passwords are not the same");
            if (pass.length() < 4 || pass.length() > 15)
                validationFails("Wrong password");

            EditText nameV = (EditText) findViewById(R.id.registrationName);
            name = nameV.getText().toString();
            if (name == "" || name.length() < 2 || name.length() > 45)
                validationFails("Wrong name");

            EditText emailV = (EditText) findViewById(R.id.registrationEmail);
            email = emailV.getText().toString();
            if (!(email.endsWith(".com") || email.endsWith(".ru") || email.endsWith(".ua") || email.contains("@")))
                validationFails("wrong email");

            EditText addressV = (EditText) findViewById(R.id.registrationAddress);
            address = addressV.getText().toString();

    }
        Button submit = (Button)findViewById(R.id.registrationSubmit);
        submit.setOnClickListener(createSubmitListener());
    }

    public void validationFails(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG);
        startActivity(new Intent(this,Registeration.class));
    }
    public Context getContext(){
        return this;
    }

    public View.OnClickListener createSelectPhotoListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        };
    }

    public void sendClient() throws IOException {
        HttpURLConnection connection=null;
        OutputStream out = null;
        try {
            connection = (HttpURLConnection)(new URL("127.0.0.1\\MyPlaceToGo\\registration").openConnection());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("content-type","application\\json");
            String urlParams = URLEncoder.encode("user_name",name)+URLEncoder.encode("user_pass",pass)+URLEncoder.encode("user_address",address);
            out = connection.getOutputStream();
            out.write(urlParams.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(out != null)
            out.close();
            connection.disconnect();
        }
    }
    public View.OnClickListener createSubmitListener(){
       return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendClient();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
