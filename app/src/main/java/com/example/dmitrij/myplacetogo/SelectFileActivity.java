package com.example.dmitrij.myplacetogo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;


public class SelectFileActivity extends ActionBarActivity {
    String[]path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        File[]files= Environment.getExternalStorageDirectory().listFiles();
        String[]data=new String[files.length];

        path = new String[files.length];
        for (int i = 0; i < data.length; i++) {
            data[i]=files[i].getName();
            path[i]=files[i].getAbsolutePath();
        }

        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,R.layout.activity_select_file,data);
        ListView filesList =(ListView)findViewById(R.id.selectFileList);
        filesList.setAdapter(adapter);
        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getContext(), Registeration.class).putExtra("fileName", path[i]));
            }});
    }
    private Context getContext(){
        return this;
    }
}
