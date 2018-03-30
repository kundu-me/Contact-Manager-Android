package com.utdallas.nxkundu.mycontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.utdallas.nxkundu.bean.Contact;
import com.utdallas.nxkundu.data.FileReadWrite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyContactsActivity extends AppCompatActivity implements Serializable {

    ListView objListViewMyContacts = null;

    List<Contact> lstContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        objListViewMyContacts = (ListView) findViewById(R.id.listViewMyContacts);
    }

    @Override
    protected void onStart() {

        super.onStart();

        lstContacts = FileReadWrite.getInstance(MyContactsActivity.this).loadContacts();

        Collections.sort(lstContacts);
        ListAdapter lstAdapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, lstContacts);
        objListViewMyContacts.setAdapter(lstAdapter);

        objListViewMyContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact objContact = (Contact) objListViewMyContacts.getItemAtPosition(position);
                Intent intent = new Intent(MyContactsActivity.this, MyContactDetailsActivity.class);
                intent.putExtra("OPERATION", "EDIT");
                intent.putExtra("CONTACT", objContact);
                intent.putExtra("POSITION", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        MyContactsActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_contact) {

            Intent intent = new Intent(MyContactsActivity.this, MyContactDetailsActivity.class);
            intent.putExtra("OPERATION", "ADD");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
