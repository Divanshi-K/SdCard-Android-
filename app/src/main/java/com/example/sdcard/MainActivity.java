package com.example.sdcard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import android.Manifest;
import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    AppCompatEditText etst;
    AppCompatButton bnwd,bnrd,bnclear;
    Context context;
    private int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etst = findViewById(R.id.etsometext);
        bnwd = findViewById(R.id.bnwd);
        bnrd = findViewById(R.id.bnrd);
        bnclear = findViewById(R.id.bnclear);
        context = this;

        bnwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etst.getText().toString();
                try
                {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE);
                    }
                    File mydir = getExternalFilesDir("mydir");
                    File myFile = new File(mydir,"textfile.txt");
                    if(!mydir.exists())
                        mydir.mkdirs();
                    if(!myFile.exists()){
                        myFile.createNewFile();
                    }
                    else{
                        myFile.delete();
                        myFile.createNewFile();
                    }
                    FileOutputStream fout=new FileOutputStream(myFile);
                    fout.write(message.getBytes());
                    fout.close();
                    Toast.makeText(getBaseContext(), "File Created on SD CARD", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bnrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg;
                String bufr = "";
                try
                {
                    File mydir = getExternalFilesDir("mydir");
                    File myFile = new File(mydir,"textfile.txt");

                    FileInputStream fis = new FileInputStream(myFile);
                    BufferedReader bfrdr = new BufferedReader(new InputStreamReader(fis));
                    while((msg = bfrdr.readLine()) != null)
                    {
                        bufr += msg;
                    }
                    etst.setText(bufr);
                    bfrdr.close();
                    fis.close();
                    Toast.makeText(getBaseContext(), "Retrieved Data from file on SD CARD", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        bnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etst.setText("");
            }
        });
    }
    public void onRequestPermissionResult(int requestCode,String[] permission, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG, "Permission: "+permission[0]+ "was"+grantResults[0]);
        }
    }
}