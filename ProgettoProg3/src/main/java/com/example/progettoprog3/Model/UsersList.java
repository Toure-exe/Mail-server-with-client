package com.example.progettoprog3.Model;

import java.util.ArrayList;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class UsersList {

    private final ArrayList<User> users;

    /**
     * Constructor of the UserList, it creates an ArrayList of users.
     * For the delivery of this project, the users are fixed and immutable.
     */
    public UsersList() {
        this.users = new ArrayList<>();
        this.users.add(new User("Mario","Rossi","mario@rossi.it"));
        this.users.add(new User("Luigi","Bianchi","luigi@bianchi.it"));
        this.users.add(new User("Giulio","Cesare","giulio@cesare.it"));
    }

    /**
     * Searching function for the string email of the users.
     * @param email the string e-mail of the user to searched for.
     * @return true if it is present, false otherwise
     */
    public boolean search(String email) {
        boolean found = false;
        if (email != null) {
            for (User temp : this.users) {
                if (email.equals(temp.getEmail())) {
                    found = true;
                    break;
                }
            }
        }
        return found;
    }
}
