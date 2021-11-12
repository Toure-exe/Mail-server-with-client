package com.example.progettoprog3.Model;

import java.util.ArrayList;

public class UsersList {
    private ArrayList<User> users;

    public UsersList(){
        this.users = new ArrayList<>();
        users.add(new User("Mario","Rossi","mario@rossi.it"));
        users.add(new User("Luigi","Bianchi","luigi@bianchi.it"));
        users.add(new User("Giulio","Cesare","giulio@cesare.it"));
    }

    public boolean search(String email){
        boolean found = false;
        if(email != null){
            for(User temp : this.users){
                if(email.equals(temp.getEmail()))
                    found = true;
            }
        }
        return found;
    }


}
