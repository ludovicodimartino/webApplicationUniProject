package it.unipd.dei.webapp.wacar.resource;

import org.json.JSONObject;

public class User {

    private int id;
    final String email;
    private String password;
    private String name;
    private String surname;


    public User(int id, String email, String password, String name, String surname){
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }


    // used when the professor gets the list of students
    public User(int id, String email, String name, String surname){
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;

    }



    // this is used in registration
    public User(String email, String password, String name, String surname){
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
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


    public JSONObject toJSON(){
        JSONObject uJson = new JSONObject();
        uJson.put("id", id);
        uJson.put("email", email);
        uJson.put("name", name);
        uJson.put("surname", surname);

        return uJson;
    }
}
