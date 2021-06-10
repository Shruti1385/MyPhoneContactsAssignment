package com.example.myphonecontactsassignment.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myphonecontactsassignment.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddContactsActivity extends AppCompatActivity {
    EditText contactName_edt,phoneNumber_edt;
    Button save_btn;
    String display_name,phone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        contactName_edt = (EditText)findViewById(R.id.addcontact);
        phoneNumber_edt = (EditText) findViewById(R.id.add_phonenumber);


        save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    display_name = contactName_edt.getText().toString();
                    phone_number = phoneNumber_edt.getText().toString();
                if(isValidPhoneNumber(phone_number)&&display_name.length()!=0) {
                    ArrayList<ContentProviderOperation> contact = new ArrayList<ContentProviderOperation>();
                    contact.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build());

                    // Name
                    contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.RawContacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, display_name)
                            .build());

                    // Phone number
                    contact.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.RawContacts.Data.RAW_CONTACT_ID, 0)
                            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone_number)
                            .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                            .build());
                    try {

                        ContentProviderResult[] results = getContentResolver().applyBatch(ContactsContract.AUTHORITY, contact);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("name",display_name);
                    intent.putExtra("phonenumber",phone_number);
                    setResult(1, intent);
                    finish();
                }else{
                    Toast.makeText(AddContactsActivity.this, "Enter Valid Data ", Toast.LENGTH_SHORT).show();
                }


            }


        });


    }


    public static boolean isValidPhoneNumber(String phonenumber) {
        Pattern mobilePattern = Pattern.compile("[0-9]{10}");
        Matcher mtch = mobilePattern.matcher(phonenumber);
        return mtch.matches();
    }

}