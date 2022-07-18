package com.example.karthikcontentproviders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView_contact;
    ContentResolver contentResolver;
    Cursor cursor;
    ArrayList<String> arrayList=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    /*ArrayList<ContactModel> contactModelArrayList=new ArrayList<>();
    ContactModel contactModel;
    ArrayAdapter<ContactModel> arrayAdapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView_contact=findViewById(R.id.ContactList);

        listView_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                intent.putExtra("photo",arrayList.get(i));
                startActivity(intent);
            }
        });
    }

    public void load(View view) {

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},110);
        }
        else
        {
            readAllContacts();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case 110:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    readAllContacts();
                }
                else
                {
                    Toast.makeText(this, "Permission Denied For Contacts", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void readAllContacts() {
        contentResolver=getContentResolver();
        Uri uri= ContactsContract.CommonDataKinds.Phone.CONTENT_URI;//Path of the Contacts
        String[] projections={ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Photo.PHOTO_URI};
        String selection=null;//row wise search
        String[] args=null;
        String sortOrder= ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC";

        cursor=contentResolver.query(uri,projections,selection,args,sortOrder);

        if(cursor!=null&&cursor.getCount()>0)
        {
            while (cursor.moveToNext())
            {
                @SuppressLint("Range") String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String photo=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO_URI));
               /* contactModel=new ContactModel(name,number);
                contactModelArrayList.add(contactModel);*/
                Log.d("Photo",""+photo);

                arrayList.add(name+"\n"+number+"\n"+photo);//name,number,photoPath
                //or
                // arrayList.add(""+photo);//Photo

                arrayAdapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList);
                listView_contact.setAdapter(arrayAdapter);

            }

        }
        else

        {
            Toast.makeText(this, "No contacts Found in this Device", Toast.LENGTH_SHORT).show();
        }

    }
}