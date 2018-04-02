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

/**
 * Created by nxkundu on 3/28/18.
 * Written by Nirmallya Kundu for CS6326.001,
 * assignment 4, starting March 28, 2018.
 * NetID: nxk161830
 */


public class MyContactsActivity extends AppCompatActivity implements Serializable {

    ListView objListViewMyContacts = null;

    List<Contact> lstContacts = new ArrayList<>();


    /**************************************************************************
     *
     * onCreate() - This method is called when the app begins
     *
     **************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        objListViewMyContacts = (ListView) findViewById(R.id.listViewMyContacts);
    }


    /**************************************************************************
     *
     * onStart() - This method is called when the app starts
     *
     **************************************************************************/

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


    /**************************************************************************
     *
     * onBackPressed() - This method confirms the user
     * whether they really want to go back
     *
     **************************************************************************/
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

    /**************************************************************************
     *
     * onCreateOptionsMenu() - This method displays the icon
     * on the action bar to add new contact
     *
     **************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**************************************************************************
     *
     * onOptionsItemSelected() - This method displays the screen to
     * add new contact
     *
     **************************************************************************/
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
