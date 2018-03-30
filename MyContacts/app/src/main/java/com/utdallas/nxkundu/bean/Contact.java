package com.utdallas.nxkundu.bean;

import java.io.Serializable;

/**
 * Created by nxkundu on 3/28/18.
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

    @Override
    public int compareTo(Contact obj) {
        //return (this.fName + " " + this.lName).compareTo(obj.fName + " " + obj.lName);
        return (this.fName).compareTo(obj.fName);
    }


    @Override
    public String toString() {
        return "\n" + fName + " " + lName + "\n\n" + phone;
    }

    public String toLine() {
        return fName + "\t" + lName + "\t" + phone + "\t" + email;
    }
}
