package com.utdallas.nxkundu.bean;

import java.io.Serializable;

/**
 * Created by nxkundu on 3/28/18.
 * Written by Nirmallya Kundu for CS6326.001,
 * assignment 4, starting March 28, 2018.
 * NetID: nxk161830
 */

public class Contact implements Serializable, Comparable<Contact>{

    private String fName;
    private String lName;
    private String phone;
    private String email;

    public Contact() {

        this.fName = "";
        this.lName = "";
        this.phone = "";
        this.email = "";

    }

    /**************************************************************************
     *
     * Getter and Setter for the Contact Class object
     *
     **************************************************************************/
    public String getFName() {

        return fName;
    }

    public void setFName(String fName) {

        this.fName = fName == null? "" : fName;
    }

    public String getLName() {

        return this.lName;
    }

    public void setLName(String lName) {

        this.lName = lName == null? "" : lName;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone == null? "" : phone;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email == null? "" : email;
    }

    /**************************************************************************
     *
     * Override the compareTo method to sort the Contact
     * by the first name
     *
     **************************************************************************/
    @Override
    public int compareTo(Contact obj) {
        return (this.fName).compareTo(obj.fName);
    }


    /**************************************************************************
     *
     * Override the toString method to Display the Contact
     *
     **************************************************************************/
    @Override
    public String toString() {
        return "\n" + fName + " " + lName + "\n\n" + phone;
    }

    /**************************************************************************
     *
     * This method return the tab separated string to
     * store the contact in the file
     *
     **************************************************************************/
    public String toLine() {
        return fName + "\t" + lName + "\t" + phone + "\t" + email;
    }
}
