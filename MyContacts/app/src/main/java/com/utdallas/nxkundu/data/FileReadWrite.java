package com.utdallas.nxkundu.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.utdallas.nxkundu.bean.Contact;

/**
 * Created by nxkundu on 3/28/18.
 */

public class FileReadWrite {

    private static final String CONTACT_FILE_NAME = "MyContactsData-26.txt";

    private Context context = null;
    private List<Contact> lstFetchedContact = null;
    private Map<String, Contact> mapContact = new HashMap<>();
    private static FileReadWrite objFileReadWrite = null;

    private FileReadWrite(Context context) {
        this.context = context;
        createMyContactsFile();
    }

    public static FileReadWrite getInstance(Context context) {

        if(objFileReadWrite == null) {
            objFileReadWrite = new FileReadWrite(context);
        }

        return objFileReadWrite;
    }

    public List<Contact> loadContacts() {

        if(lstFetchedContact != null) {
            return lstFetchedContact;
        }

        List<Contact> lstContact = new ArrayList<>();
        BufferedReader objBufferedReader = null;

        try {

            FileInputStream objFileInputStream = context.openFileInput(CONTACT_FILE_NAME);
            InputStreamReader objInputStreamReader = new InputStreamReader(objFileInputStream);
            objBufferedReader = new BufferedReader(objInputStreamReader);
            String strLine = "";

            while((strLine = objBufferedReader.readLine()) != null) {
                String[] arrWords = strLine.split("\t");

                Contact objContact = new Contact();
                objContact.setFName(arrWords[0]);
                objContact.setLName(arrWords[1]);
                objContact.setPhone(arrWords[2]);
                objContact.setEmail(arrWords[3]);

                lstContact.add(objContact);
                mapContact.put(objContact.getFName(), objContact);
            }
        }
        catch(IOException e) {

            e.printStackTrace();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
        finally {
            if(objBufferedReader != null) {
                try {
                    objBufferedReader.close();
                }
                catch(Exception e) {

                    e.printStackTrace();
                }
            }
        }

        lstFetchedContact = new ArrayList<>(lstContact);
        Collections.sort(lstFetchedContact);
        return lstFetchedContact;
    }

    public void createMyContactsFile()  {

        File objFileDirs = new File(String.valueOf(context.getFilesDir()));
        File objFile = new File(objFileDirs, CONTACT_FILE_NAME);

        try{
            if(!objFile.exists()) {

                objFile.createNewFile();
            }
            else {

                lstFetchedContact = this.loadContacts();
            }

        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean addNewContact(Contact objContact) {

        if(mapContact.get(objContact.getFName()) != null) {
            return false;
        }

        lstFetchedContact.add(objContact);
        mapContact.put(objContact.getFName(), objContact);

        writeToFile();

        return true;
    }

    public boolean editContact(Contact objOldContact, Contact objNewContact) {

        if(mapContact.get(objNewContact.getFName()) != null) {
            return false;
        }

        mapContact.remove(objOldContact.getFName());
        mapContact.put(objNewContact.getFName(), objNewContact);

        List<Contact> lstContactNew = new ArrayList<>();

        for (String fName : mapContact.keySet()) {

            Contact objContact = mapContact.get(fName);

            lstContactNew.add(objContact);
        }

        lstFetchedContact = new ArrayList<>(lstContactNew);
        Collections.sort(lstFetchedContact);

        writeToFile();

        return true;
    }

    public boolean deleteContact(Contact objDelContact) {

        if(mapContact.get(objDelContact.getFName()) == null) {
            return false;
        }

        mapContact.remove(objDelContact.getFName());

        List<Contact> lstContactNew = new ArrayList<>();
        for (String fName : mapContact.keySet()) {

            Contact objContact = mapContact.get(fName);

            lstContactNew.add(objContact);
        }

        lstFetchedContact = new ArrayList<>(lstContactNew);
        Collections.sort(lstFetchedContact);

        writeToFile();

        return true;
    }

    private void writeToFile() {

        try {
            FileOutputStream objFileOutputStream = context.openFileOutput(CONTACT_FILE_NAME, Context.MODE_PRIVATE);
            BufferedWriter objBufferedWriter = new BufferedWriter(new OutputStreamWriter(objFileOutputStream));

            for (Contact contact : lstFetchedContact) {
                objBufferedWriter.write(contact.toLine());
                objBufferedWriter.newLine();
            }
            objBufferedWriter.close();
            objFileOutputStream.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
