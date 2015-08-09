package com.example.dmitrij.myplacetogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dmitrij.myplacetogo.java_classes.Reciver;
import com.example.dmitrij.myplacetogo.json_objects.Client;
import com.example.dmitrij.myplacetogo.json_objects.Photo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class Registeration extends ActionBarActivity {
    private String name, pass, email, address, fileName;
    private Client client;
    private final int PICK_IMAGE = 1;
    private final String logTag="Error";
    private final int RESULT_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        client = new Client();

        Button submit = (Button)findViewById(R.id.registrationSubmit);
        submit.setOnClickListener(createSubmitListener());
    }

    public View.OnClickListener createSelectImageListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
            startActivityForResult(chooserIntent, PICK_IMAGE);
        }
    };
    }

    public View.OnClickListener createSubmitListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                if(validateData()){
                    sendClient();
                    setResult(RESULT_CODE,new Intent().putExtra(Client.class.getCanonicalName(),client));
                }
                else Toast.makeText(Registeration.this,"Sorry your registration fails, try again later",Toast.LENGTH_LONG);
            } catch (IOException e) {
                Log.d(logTag,"Registration fails, client was not sended to server");
                e.printStackTrace();
            }
        }
    };
    }

    private boolean validateData() {
        {
            EditText passV = (EditText) findViewById(R.id.registrationPass);
            EditText passCV = (EditText) findViewById(R.id.registrationPassConfirm);
            pass = passV.getText().toString();
            String passConfig = passCV.getText().toString();
            if (pass != passConfig) {
                validationFails("Your passwords are not the same");
                return false;
            }
            if (pass.length() < 4 || pass.length() > 15) {
                validationFails("Wrong password");
                return false;
            }
            EditText nameV = (EditText) findViewById(R.id.registrationName);
            name = nameV.getText().toString();
            if (name == "" || name.length() < 2 || name.length() > 45) {
                validationFails("Wrong name");
                return false;
            }
            EditText emailV = (EditText) findViewById(R.id.registrationEmail);
            email = emailV.getText().toString();
            if (!(email.endsWith(".com") || email.endsWith(".ru") || email.endsWith(".ua") || email.contains("@"))) {
                validationFails("wrong email");
                return false;
            }

            EditText addressV = (EditText) findViewById(R.id.registrationAddress);
            address = addressV.getText().toString();

            Button image = (Button) findViewById(R.id.registrationPhoto);
            image.setOnClickListener(createSelectImageListener());
            client.name(name).pass(pass).address(address).email(email);
        }
        return true;
    }

    public void validationFails(String text){
        Log.d("registration fails",text);
        Toast.makeText(this, text, Toast.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("myLogs", "requestCode = " + requestCode + ", resultCode = " + resultCode);
        // если пришло ОК

        if (resultCode == RESULT_OK && requestCode==PICK_IMAGE) {

            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection)(new URL(getString(R.string.url_user_photo_uppload)).openConnection());
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);

            try(InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                BufferedOutputStream bout = new BufferedOutputStream(connection.getOutputStream());
                BufferedInputStream bin = new BufferedInputStream(inputStream);) {
                int offset=0;
                int readed;
                byte[]buffer=new byte[512];
                while((readed=bin.read(buffer,offset,buffer.length))!=-1){
                    bout.write(buffer,offset,buffer.length);
                    offset+=readed;
                }
                Reciver reciver = new Reciver();
                client.clientPhotos[0]=new Photo().setBody(buffer).setUrl(Long.parseLong(reciver.reciveString(connection.getInputStream())));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.d(logTag,"File have bean not receved from the gallery");
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(logTag, "Cannot send user photo, erorr during tranzmition");
            }

        }
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


}
