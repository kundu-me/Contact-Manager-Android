package com.utdallas.nxkundu.mycontacts;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.utdallas.nxkundu.bean.Contact;
import com.utdallas.nxkundu.data.FileReadWrite;

/**
 * Created by nxkundu on 3/29/18.
 */

public class MyContactDetailsActivity  extends AppCompatActivity implements View.OnClickListener {

    EditText objEditTextFName = null;
    EditText objEditTextLName = null;
    EditText objEditTextPhone = null;
    EditText objEditTextEmail = null;

    Button objButtonSave = null;
    Button objButtonEdit = null;
    Button objButtonDelete = null;

    String operation = null;
    Contact objContact = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contact_details);

        objEditTextFName = (EditText) findViewById(R.id.editTextFName);
        objEditTextLName = (EditText) findViewById(R.id.editTextLName);
        objEditTextPhone = (EditText) findViewById(R.id.editTextPhone);
        objEditTextEmail = (EditText) findViewById(R.id.editTextEmail);

        objButtonSave = (Button) findViewById(R.id.buttonSave);
        objButtonEdit = (Button) findViewById(R.id.buttonEdit);
        objButtonDelete = (Button) findViewById(R.id.buttonDelete);

        Intent intent = getIntent();
        if (intent != null) {

            operation = (String) intent.getSerializableExtra("OPERATION");
        }

        if(("ADD").equalsIgnoreCase(operation)) {

            objButtonSave.setVisibility(View.VISIBLE);
            objButtonEdit.setVisibility(View.GONE);
            objButtonDelete.setVisibility(View.GONE);
        }
        else if(("EDIT").equalsIgnoreCase(operation)) {

            objButtonSave.setVisibility(View.GONE);
            objButtonEdit.setVisibility(View.VISIBLE);
            objButtonDelete.setVisibility(View.VISIBLE);

            objEditTextFName.setEnabled(false);
            objEditTextLName.setEnabled(false);
            objEditTextPhone.setEnabled(false);
            objEditTextEmail.setEnabled(false);

            int position = (int) intent.getSerializableExtra("POSITION");
            objContact = (Contact) intent.getSerializableExtra("CONTACT");

            objEditTextFName.setText(objContact.getFName());
            objEditTextLName.setText(objContact.getLName());
            objEditTextPhone.setText(objContact.getPhone());
            objEditTextEmail.setText(objContact.getEmail());
        }
    }

    @Override
    public void onBackPressed() {

        if(objButtonSave.getVisibility() == View.VISIBLE) {

            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to go back?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            MyContactDetailsActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        else {

            MyContactDetailsActivity.this.finish();
        }
    }

    @Override
    public void onClick(View view) {

        String strMessage = "";

        switch (view.getId()) {

            case R.id.buttonEdit:

                objButtonSave.setVisibility(View.VISIBLE);
                objButtonEdit.setVisibility(View.GONE);
                objButtonDelete.setVisibility(View.VISIBLE);

                objEditTextFName.setEnabled(true);
                objEditTextLName.setEnabled(true);
                objEditTextPhone.setEnabled(true);
                objEditTextEmail.setEnabled(true);

                break;

            case R.id.buttonSave:

                String strFName = objEditTextFName.getText().toString();
                String strLName = objEditTextLName.getText().toString();
                String strPhone = objEditTextPhone.getText().toString();
                String strEmail = objEditTextEmail.getText().toString();

                if (strFName.isEmpty()) {

                    strMessage = "Please enter the first name";
                    Toast.makeText(MyContactDetailsActivity.this, strMessage, Toast.LENGTH_SHORT).show();
                    break;
                }

                Contact objNewContact = new Contact();
                objNewContact.setFName(strFName);
                objNewContact.setLName(strLName);
                objNewContact.setPhone(strPhone);
                objNewContact.setEmail(strEmail);

                boolean isSuccess = false;
                if(("ADD").equalsIgnoreCase(operation)) {

                    isSuccess = FileReadWrite.getInstance(MyContactDetailsActivity.this).addNewContact(objNewContact);
                }
                else if(("EDIT").equalsIgnoreCase(operation)) {

                    isSuccess = FileReadWrite.getInstance(MyContactDetailsActivity.this).editContact(objContact, objNewContact);
                }

                if(!isSuccess) {

                    strMessage = "Contact Already Exists";
                    Toast.makeText(MyContactDetailsActivity.this, strMessage, Toast.LENGTH_SHORT).show();
                    break;
                }

                strMessage = "Saved Successfully";
                Toast.makeText(MyContactDetailsActivity.this, strMessage, Toast.LENGTH_SHORT).show();

                MyContactDetailsActivity.this.finish();

                break;

            case R.id.buttonDelete:

                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                boolean isSuccessDel = FileReadWrite.getInstance(MyContactDetailsActivity.this).deleteContact(objContact);
                                String strMessageDel = null;
                                if(isSuccessDel) {

                                    strMessageDel = "Contact Deleted";
                                }
                                else {

                                    strMessageDel = "Error";
                                }

                                Toast.makeText(MyContactDetailsActivity.this, strMessageDel, Toast.LENGTH_SHORT).show();
                                MyContactDetailsActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                break;

        }
    }
}
