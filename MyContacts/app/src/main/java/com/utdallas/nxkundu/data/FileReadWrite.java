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

import com.utdallas.nxkundu.bean.Contact;

/**
 * Created by nxkundu on 3/28/18.
 * Written by Nirmallya Kundu for CS6326.001,
 * assignment 4, starting March 28, 2018.
 * NetID: nxk161830
 */

public class FileReadWrite {

    private static final String CONTACT_FILE_NAME = "MyContactsFile4.txt";

    private Context context = null;
    private List<Contact> lstFetchedContact = new ArrayList<>();
    private Map<String, Contact> mapContact = new HashMap<>();
    private static FileReadWrite objFileReadWrite = null;

    /**************************************************************************
     *
     * private constructor to create a singleton Class
     * Also calls the method to create a text a file to store the Contacts
     * if the file does not exists, creates a file
     *
     **************************************************************************/
    private FileReadWrite(Context context) {
        this.context = context;
        createMyContactsFile();
    }

    /**************************************************************************
     *
     * Method to get the object of the Singleton Class
     *
     **************************************************************************/
    public static FileReadWrite getInstance(Context context) {

        if(objFileReadWrite == null) {
            objFileReadWrite = new FileReadWrite(context);
        }

        return objFileReadWrite;
    }

    /**************************************************************************
     *
     * loadContacts() - This method loads all the contacts from the
     * tab separated text file into a list
     *
     **************************************************************************/
    public List<Contact> loadContacts() {

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
                objContact.setFName(arrWords.length >= 1? arrWords[0] : "");
                objContact.setLName(arrWords.length >= 2? arrWords[1] : "");
                objContact.setPhone(arrWords.length >= 3? arrWords[2] : "");
                objContact.setEmail(arrWords.length >= 4? arrWords[3] : "");

                lstContact.add(objContact);
                mapContact.put(generateMapKey(objContact), objContact);
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

    /**************************************************************************
     *
     * addNewContact() - This method adds a new Contact
     * Update the existing list
     * Write the data (along with new and old) to the file
     *
     **************************************************************************/
    public boolean addNewContact(Contact objContact) {

        if(mapContact.get(generateMapKey(objContact)) != null) {
            return false;
        }

        lstFetchedContact.add(objContact);
        mapContact.put(generateMapKey(objContact), objContact);

        writeToFile();

        return true;
    }

    /**************************************************************************
     *
     * editContact() - This method edits a Contact
     * Update the existing list
     * Write the data (along with new and old) to the file
     *
     **************************************************************************/
    public boolean editContact(Contact objOldContact, Contact objNewContact) {

        if((objOldContact.getFName().equals(objNewContact.getFName()) == false
                || objOldContact.getLName().equals(objNewContact.getLName()) == false
                || objOldContact.getPhone().equals(objNewContact.getPhone()) == false
                || objOldContact.getEmail().equals(objNewContact.getEmail()) == false)
                && (mapContact.get(generateMapKey(objNewContact)) != null)) {
            return false;
        }

        mapContact.remove(generateMapKey(objOldContact));
        mapContact.put(generateMapKey(objNewContact), objNewContact);

        List<Contact> lstContactNew = new ArrayList<>();

        for (String key : mapContact.keySet()) {

            Contact objContact = mapContact.get(key);

            lstContactNew.add(objContact);
        }

        lstFetchedContact = new ArrayList<>(lstContactNew);
        Collections.sort(lstFetchedContact);

        writeToFile();

        return true;
    }

    /**************************************************************************
     *
     * deleteContact() - This method deletes a Contact
     * Update the existing list
     * Write the data (along with new and old) to the file
     *
     **************************************************************************/
    public boolean deleteContact(Contact objDelContact) {

        if(mapContact.get(generateMapKey(objDelContact)) == null) {
            return false;
        }

        mapContact.remove(generateMapKey(objDelContact));

        List<Contact> lstContactNew = new ArrayList<>();
        for (String key : mapContact.keySet()) {

            Contact objContact = mapContact.get(key);

            lstContactNew.add(objContact);
        }

        lstFetchedContact = new ArrayList<>(lstContactNew);
        Collections.sort(lstFetchedContact);

        writeToFile();

        return true;
    }

    /**************************************************************************
     *
     * writeToFile() - This method the entire list data
     * Write the data (along with new and old) to the file
     *
     **************************************************************************/
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

    /**************************************************************************
     *
     * generateMapKey() - This method generates the Map Key
     *
     **************************************************************************/
    private String generateMapKey(Contact objContact) {

        //return objContact.getFName() + "#" + objContact.getLName();
         return objContact.getFName() + "#" + objContact.getLName()
                 + objContact.getPhone() + "#" + objContact.getEmail();
    }

    /**************************************************************************
     *
     * createMyContactsFile() - This method Creates the Contacts file
     * if the file does not exists
     *
     **************************************************************************/
    private void createMyContactsFile()  {

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

}
