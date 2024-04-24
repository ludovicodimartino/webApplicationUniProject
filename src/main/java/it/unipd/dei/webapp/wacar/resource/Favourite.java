package it.unipd.dei.webapp.wacar.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Timestamp;

/**
 * Represents a favourite item, typically associated with a user's preferences.
 * This class stores information about a user's favourite circuit, car brand, car model, account, and creation timestamp.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public class Favourite extends AbstractResource{

    /**
     * The name of the circuit associated with this record.
     */
    private final String circuit;
    /**
     * The brand of the car associated with this record.
     */
    private final String carBrand;
    /**
     * The model of the car associated with this record.
     */
    private final String carModel;
    /**
     * The account associated with this record.
     */
    private final String account;

    /**
     * The timestamp indicating when this record was created.
     */
    private Timestamp createdAt;

    /**
     * Constructs a Favourite object with the specified circuit, car brand, car model, account, and creation timestamp.
     *
     * @param circuit   The name of the circuit associated with this favourite.
     * @param carBrand  The brand of the car associated with this favourite.
     * @param carModel  The model of the car associated with this favourite.
     * @param account   The account associated with this favourite.
     * @param createdAt The timestamp indicating when this favourite was created.
     */
    public Favourite(String circuit, String carBrand, String carModel, String account, Timestamp createdAt) {
        this.circuit = circuit;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.account = account;
        this.createdAt = createdAt;
    }

    /**
     * Constructs a Favourite object with the specified circuit, car brand, car model, and account.
     *
     * @param circuit  The name of the circuit associated with this favourite.
     * @param carBrand The brand of the car associated with this favourite.
     * @param carModel The model of the car associated with this favourite.
     * @param account  The account associated with this favourite.
     */
    public Favourite(String circuit, String carBrand, String carModel, String account) {
        this.circuit = circuit;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.account = account;
    }
    /**
     * Retrieves the name of the circuit associated with this favourite.
     *
     * @return The name of the circuit.
     */
    public final String getCircuit() {
        return circuit;
    }
    /**
     * Retrieves the account associated with this favourite.
     *
     * @return The account associated with this favourite.
     */
    public final String getAccount() {
        return account;
    }

    /**
     * Retrieves the brand of the car associated with this favourite.
     *
     * @return The brand of the car.
     */
    public final String getCarBrand() {
        return carBrand;
    }

    /**
     * Retrieves the model of the car associated with this favourite.
     *
     * @return The model of the car.
     */
    public final String getCarModel() {
        return carModel;
    }
    /**
     * Retrieves the creation timestamp of this favourite.
     *
     * @return The creation timestamp of this favourite.
     */
    public final Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Writes JSON representation of the favourite object to the provided output stream.
     * This method is used to serialize the favourite object into JSON format.
     *
     * @param out The output stream to which the JSON data will be written.
     * @throws Exception If an error occurs while writing JSON data.
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("favourite");

        jg.writeStartObject();

        jg.writeStringField("circuit", circuit);

        jg.writeStringField("carBrand", carBrand);

        jg.writeStringField("carModel", carModel);

        jg.writeStringField("account", account);

        jg.writeFieldName("createdAt");

        jg.writeNumber(createdAt.getTime());

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

    /**
     * Constructs a Favourite object from JSON data read from the provided input stream.
     * This method is used to deserialize JSON data into a favourite object.
     *
     * @param in The input stream containing the JSON data representing the favourite object.
     * @return A new Favourite object constructed from the JSON data.
     * @throws IOException If an error occurs while reading JSON data.
     */
    public static Favourite fromJSON(final InputStream in) throws IOException {

        String jcircuit = null;
        String jcarBrand = null;
        String jcarModel = null;
        String jaccount = null;
        Timestamp jcreatedAt = null;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"favourite".equals(jp.getCurrentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No Favourite object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Favourite object found.");
                }
            }

            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "circuit":
                            jp.nextToken();
                            jcircuit = jp.getText();
                            break;
                        case "carBrand":
                            jp.nextToken();
                            jcarBrand = jp.getText();
                            break;
                        case "carModel":
                            jp.nextToken();
                            jcarModel = jp.getText();
                            break;
                        case "account":
                            jp.nextToken();
                            jaccount = jp.getText();
                            break;
                        case "createdAt":
                            jp.nextToken();
                            jcreatedAt = new Timestamp(jp.getLongValue());
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Favourite object from JSON.", e);
            throw e;
        }

        return new Favourite(jcircuit, jcarBrand, jcarModel, jaccount, jcreatedAt);
    }
}