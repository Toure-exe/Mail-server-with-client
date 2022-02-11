package com.example.progettoprog3.Model;

/**
 *
 * @author Giulio Taralli & Ismaila Toure & Lorenzo Camilleri
 */
public class User {

    private final String name;
    private final String surname;
    private final String email;

    /**
     * Constructor of the user class, creates a new user.
     * @param name the name of the user
     * @param surname the surname of the user
     * @param email the e-mail String of the user
     */
    public User(String name, String surname, String email) {
        this.surname = surname;
        this.name = name;
        this.email = email;
    }

    /**
     * ToString method that generates a String of user information.
     * @return the string of the user information
     */
    @Override
    public String toString() {
        return "User{" +
                "name='" + this.name + '\'' +
                ", username='" + this.surname + '\'' +
                ", email='" + this.email + '\'' +
                '}';
    }

    /**
     * Get function of the e-mail parameter String email
     * @return the e-mail string of the user
     */
    public String getEmail() {
        return this.email;
    }
}