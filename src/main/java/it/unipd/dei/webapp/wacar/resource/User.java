package it.unipd.dei.webapp.wacar.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * User class represents a user entity in the database.
 * This class encapsulates user information such as ID, email, password, name, surname, address, and account type.
 * It provides constructors for various scenarios including user creation, login, and retrieval.

 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class User extends AbstractResource {

    /**
     * The unique identifier of the user.
     */
    private int id;

    /**
     * The email address of the user.
     */
    final String email;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The first name of the user.
     */
    private String name;

    /**
     * The last name of the user.
     */
    private String surname;

    /**
     * The address of the user.
     */
    private String address;

    /**
     * The type of account associated with the user (e.g., "admin", "customer", etc.).
     */
    private String accountType;

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param id       The unique identifier of the user.
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @param name     The first name of the user.
     * @param surname  The last name of the user.
     * @param address  The address of the user.
     */
    public User(int id, String email, String password, String name, String surname, String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param id      The unique identifier of the user.
     * @param email   The email address of the user.
     * @param name    The first name of the user.
     * @param surname The last name of the user.
     */
    public User(int id, String email, String name, String surname) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param id      The unique identifier of the user.
     * @param email   The email address of the user.
     * @param accountType    The account type of the user.
     */
    public User(int id, String email, String accountType) {
        this.id = id;
        this.email = email;
        this.accountType = accountType;
    }

    /**
     * Constructs a User object with the specified attributes.
     *
     * @param email   The email address of the user.
     * @param name    The first name of the user.
     * @param surname The last name of the user.
     */
    public User(String email, String name, String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    /**
     * Constructs a User object used for registration with the specified attributes.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     * @param name     The first name of the user.
     * @param surname  The last name of the user.
     * @param address  The address of the user.
     */
    public User(String email, String password, String name, String surname, String address) {
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.address = address;
    }

    /**
     * Constructs a User object used for login with the specified attributes.
     *
     * @param email    The email address of the user.
     * @param password The password of the user.
     */
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Constructs a User object used for registration to check if the user already exists with the specified email.
     *
     * @param email The email address of the user.
     */
    public User(String email) {
        this.email = email;
    }

    /**
     * Constructs a User object returned after login with the specified attributes.
     *
     * @param email       The email address of the user.
     * @param password    The password of the user.
     * @param name        The first name of the user.
     * @param surname     The last name of the user.
     * @param address     The address of the user.
     * @param accountType The type of account associated with the user.
     */
    public User(String email, String password, String name, String surname, String address, String accountType) {
        this.email = email;
        this.password = password;
        this.surname = surname;
        this.name = name;
        this.address = address;
        this.accountType = accountType;
    }

    /**
     * Retrieves the email address of the user.
     *
     * @return The email address of the user.
     */
    public final String getEmail() {
        return email;
    }

    /**
     * Retrieves the unique identifier of the user.
     *
     * @return The unique identifier of the user.
     */
    public final int getId() {
        return id;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return The first name of the user.
     */
    public final String getName() {
        return name;
    }

    /**
     * Retrieves the password of the user.
     *
     * @return The password of the user.
     */
    public final String getPassword() {
        return password;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return The last name of the user.
     */
    public final String getSurname() {
        return surname;
    }

    /**
     * Retrieves the address of the user.
     *
     * @return The address of the user.
     */
    public final String getAddress() {
        return address;
    }

    /**
     * Retrieves the type of account associated with the user.
     *
     * @return The type of account associated with the user.
     */
    public final String getType() {
        return accountType;
    }

    /**
     * Writes JSON representation of the user object to the provided output stream.
     * This method is used to serialize the user object into JSON format.
     *
     * @param out The output stream to which the JSON data will be written.
     * @throws Exception If an error occurs while writing JSON data.
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("user");

        jg.writeStartObject();

        jg.writeStringField("email", email);

        jg.writeStringField("password", password);

        jg.writeStringField("name", name);

        jg.writeStringField("surname", surname);

        jg.writeStringField("address", address);

        jg.writeStringField("accountType", accountType);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

    /**
     * Constructs a User object from JSON data read from the provided input stream.
     * This method is used to deserialize JSON data into a user object.
     *
     * @param in The input stream containing the JSON data representing the user object.
     * @return A new User object constructed from the JSON data.
     * @throws IOException If an error occurs while reading JSON data.
     */
    public static User fromJSON(final InputStream in) throws IOException {

        int jid = -1;
        String jpassword = null;
        String jname = null;
        String jsurname = null;
        String jaddress = null;
        String jaccountType = null;

        try {

            final JsonParser jp = JSON_FACTORY.createParser(in);
            // while we are not on the start of an element or the element is not a token element
            // advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"user".equals(jp.getCurrentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No User object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no User object found.");
                }
            }
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "id":
                            jp.nextToken();
                            jid = jp.getIntValue();
                            break;
                        case "password":
                            jp.nextToken();
                            jpassword = jp.getText();
                            break;
                        case "name":
                            jp.nextToken();
                            jname = jp.getText();
                            break;
                        case "surname":
                            jp.nextToken();
                            jsurname = jp.getText();
                            break;
                        case "address":
                            jp.nextToken();
                            jaddress = jp.getText();
                            break;
                        case "accountType":
                            jp.nextToken();
                            jaccountType = jp.getText();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a User object from JSON.", e);
            throw e;
        }
        return new User(jid, jpassword, jname, jsurname, jaddress, jaccountType);
    }
}
