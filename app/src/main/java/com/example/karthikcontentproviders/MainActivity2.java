package com.example.karthikcontentproviders;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    ImageView imageView;
    String contactPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        imageView=findViewById(R.id.photo);
        contactPhoto=getIntent().getStringExtra("photo");
        imageView.setImageURI(Uri.parse(contactPhoto));
    }
}