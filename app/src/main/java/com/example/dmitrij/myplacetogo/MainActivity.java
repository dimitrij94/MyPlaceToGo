package com.example.dmitrij.myplacetogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dmitrij.myplacetogo.java_classes.Reciver;
import com.example.dmitrij.myplacetogo.java_classes.Sender;
import com.example.dmitrij.myplacetogo.json_objects.Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {


    private final String encode = "UTF-8";
    private final int REGISTER = 2;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        {
            Button regist =(Button) findViewById(R.id.register);
            regist.setOnClickListener(createRegisterListeneer());
        }
        {
            Button submit = (Button) findViewById(R.id.enterButton);
            submit.setOnClickListener(createAuthorizationListener());
        }
    }

    public View.OnClickListener createAuthorizationListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.enterButton) {
                    EditText userEmail = (EditText)findViewById(R.id.userEmailField);
                    EditText userPassword = (EditText)findViewById(R.id.userEmailField);
                    Sender sender = new Sender();
                    HttpURLConnection connection=null;
                    try {
                            connection = (HttpURLConnection) new URL(getString(R.string.url_user_authorization)+
                                    "user_name"+
                                    userEmail.getText().toString()+
                                    "user_pass"+
                                    userPassword.getText().toString()).openConnection();

                            connection.setRequestMethod("GET");
                            connection.setDoInput(true);
                            connection.setUseCaches(true);

                        /*String toservlet = URLEncoder.encode("user_name",encode) + "="
                                + URLEncoder.encode(userEmail.getText().toString(), encode) + "&"
                                + URLEncoder.encode("user_pass", encode) + "="
                                + URLEncoder.encode(userPassword.getText().toString(), encode);
                        sender.send(connection.getOutputStream(), toservlet);*/

                        InputStream in = connection.getInputStream();
                        if (in != null) {
                            Reciver<Client> clientReciver = new Reciver<Client>();
                            Client client = clientReciver.recive(in);
                            authorize(client);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finally {
                        connection.disconnect();
                    }
                }
            }
        };
    }

    private void authorize(Client client){
        Intent next = new Intent(this, user_proffile.class);
        next.putExtra(Client.class.getCanonicalName(), client);
        startActivity(next);
    }

    public View.OnClickListener createRegisterListeneer(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, Registeration.class),REGISTER);
            }
        };
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RESULT_OK && requestCode==REGISTER){
            client = data.getParcelableExtra(Client.class.getCanonicalName());
            authorize(client);
        }
    }

}
