package com.example.youtube.define;

import com.example.youtube.Contact.DeXuat;


import java.util.ArrayList;

public class Define_Methods {
    public boolean CHECK(String title, ArrayList<DeXuat> arrayList){
        for (DeXuat deXuat:arrayList){
            if(title.equals(deXuat.getText())==true)
                return true;
        }
        return false;
    }

}
