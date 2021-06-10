package com.example.myphonecontactsassignment.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myphonecontactsassignment.Adapters.ContactListRecyclerViewAdapter;
import com.example.myphonecontactsassignment.Model.ContactDetails;
import com.example.myphonecontactsassignment.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.ListIterator;

public class ShowPhoneContactListActivity extends AppCompatActivity {

    Cursor cursor;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContactListRecyclerViewAdapter contactListAdapter;
    ArrayList<ContactDetails> contactDetailsArrayList;

    public static final int ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE = 0;
    Context context;
    Button add_contact_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_phone_contact_list);
        context = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        contactDetailsArrayList = new ArrayList<>();

        if (checkPermission()) {
            LoadContact loadContact = new LoadContact();
            loadContact.execute();
        }

        add_contact_btn = (Button) findViewById(R.id.addcontact_btn);

        add_contact_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowPhoneContactListActivity.this, AddContactsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    class LoadContact extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Get Contact list from Phone
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            HashSet<String> mobile_number_set = new HashSet<>();
            if (cursor != null) {
                Log.e("count", "" + cursor.getCount());
                if (cursor.getCount() == 0) {

                }

                while (cursor.moveToNext()) {

                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ContactDetails contactDetails = new ContactDetails();
                    contactDetails.setContactName(name);
                    contactDetails.setContactPhoneNumber(phoneNumber);

                    phoneNumber = phoneNumber.replaceAll(" ", "");
                    if ((phoneNumber.startsWith("+91") || (phoneNumber.startsWith("+1"))) && (!mobile_number_set.contains(phoneNumber))) {
                        contactDetailsArrayList.add(contactDetails);
                        mobile_number_set.add(phoneNumber);
                    }

                    ContactsSort(contactDetailsArrayList);

                }
            } else {
                Log.e("Cursor closed", "----------------");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            contactListAdapter = new ContactListRecyclerViewAdapter(context, contactDetailsArrayList);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(contactListAdapter);

        }
    }

    public void ContactsSort(ArrayList<ContactDetails> contactDetailsArrayList) {

        Collections.sort(contactDetailsArrayList, new Comparator<ContactDetails>() {

            @Override
            public int compare(ContactDetails o1, ContactDetails o2) {
                try {
                    return o1.getContactName().compareTo(o2.getContactName());
                } catch (Exception e) {
                    return 0;
                }
            }
        });

    }

    //runtime storage permission
    public boolean checkPermission() {
        int READ_CONTACTS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        int WRITE_CONTACTS = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
        if((READ_CONTACTS != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS}, ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE);
            return false;
        }

        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case  ASK_MULTIPLE_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 &&(permissions[0].equals(Manifest.permission.READ_CONTACTS)&&permissions[1].equals(Manifest.permission.WRITE_CONTACTS))) {

                    if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED ) {
                        Toast.makeText(getApplicationContext(), "Please allow  permission", Toast.LENGTH_LONG).show();
                    } else {

                        LoadContact loadContact = new LoadContact();
                        loadContact.execute();
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 ){

           /* LoadContact loadContact = new LoadContact();
            loadContact.execute();*/
               if(data !=null) {
                   ContactDetails contactDetails = new ContactDetails();
                   contactDetails.setContactName(data.getStringExtra("name"));
                   contactDetails.setContactPhoneNumber(data.getStringExtra("phonenumber"));
                   contactDetailsArrayList.add(contactDetails);
                   ContactsSort(contactDetailsArrayList);
                   contactListAdapter = new ContactListRecyclerViewAdapter(context, contactDetailsArrayList);
                   recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                   recyclerView.setAdapter(contactListAdapter);
               }

        }
    }
}