package com.myapplicationdev.android.l9problemstatement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class checkRecord extends AppCompatActivity {
    String folderLocation;
TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record);
        tv = findViewById(R.id.textView3);
        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder";
        File targetFile = new File(folderLocation, "data2.txt");
        if (targetFile.exists() == true) {
            String data = "";
            try {
                FileReader reader = new FileReader(targetFile);
                BufferedReader br = new BufferedReader(reader);
                String line = br.readLine();
                while (line != null) {
                    data += line + "\n";
                    line = br.readLine();
                }
                br.close();
                reader.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to read", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            tv.setText(data);
        }

    }
}