package com.example.progettoprog3.Model;

public class User {
    private String nome;
    private String cognome;
    private String email;

    public User(String nome, String cognome, String email){
        this.cognome = cognome;
        this.nome = nome;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }
}
