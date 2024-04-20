package it.unipd.dei.webapp.wacar.resource;

import org.json.JSONObject;

/**
 * User class represents a user entity in the database.
 * This class encapsulates user information such as ID, email, password, name, surname, address, and account type.
 * It provides constructors for various scenarios including user creation, login, and retrieval.
 */
public class User {

    private int id;
    final String email;
    private String password;
    private String name;
    private String surname;
    private String address;
    private  String accountType;



    public User(int id, String email, String password, String name, String surname, String address){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;

    }


    public User(int id, String email, String name, String surname){
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;

    }



    // this is used in registration
    public User(String email, String password, String name, String surname, String address){
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.address =address;
    }

    // this is used in login
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }

    // this is used in registration to check if user already exists
    public User(String email){
        this.email = email;
    }

    // this is returned after login
    public User(String email, String password, String name, String surname, String address, String accountType){
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.address =address;
        this.accountType = accountType;
    }






    public final String getEmail() {
        return email;
    }

    public final int getId() {
        return id;
    }

    public final String getName() {
        return name;
    }

    public final String getPassword() {
        return password;
    }

    public final String getSurname() {
        return surname;
    }
    public final String getAddress() {return address;}
    public final String getType() {return accountType;}


    public JSONObject toJSON(){
        JSONObject uJson = new JSONObject();
        uJson.put("id", id);
        uJson.put("email", email);
        uJson.put("name", name);
        uJson.put("surname", surname);

        return uJson;
    }


}
