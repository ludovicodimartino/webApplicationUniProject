package it.unipd.dei.webapp.wacar.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.text.ParseException;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Represents an order.
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Order extends AbstractResource {
    private final int id;

    /**
     * The account that made the order
     */
    private final String account;

    /**
     * The date
     */
    private final Date date;

    /**
     * The car brand
     */
    private final String carBrand;

    /**
     * The car model
     */
    private final String carModel;

    /**
     * The name of the circuit
     */
    private final String circuit;

    /**
     * Thew time in which the account created the order
     */
    private final Timestamp createdAt;

    /**
     * The number of laps 
     */
    private final int nLaps;

    /**
     * The price
     */
    private final int price;

    /**
     * Constructs a new Order object with the specified attributes.
     *
     * @param id        the id of the order
     * @param account   the email of the specific account
     * @param date      the date in which the experience will take place
     * @param carBrand  the brand of the car
     * @param carModel  the model of the car
     * @param circuit   the circuit in which the specified account will drive
     * @param createdAt the timestamp of when the order was created
     * @param nLaps     the number of laps
     * @param price     the price of the order
     */
    public Order(int id, String account, Date date, String carBrand, String carModel, String circuit, Timestamp createdAt, int nLaps, int price) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.carBrand =carBrand;
        this.carModel = carModel;
        this.circuit = circuit;
        this.createdAt = createdAt;
        this.nLaps = nLaps;
        this.price = price;
    }

    /**
     * Constructs a new Order object with the specified attributes.
     *
     * @param account   the email of the specific account.
     * @param date      the date in which the experience will take place.
     * @param carBrand  the brand of the car.
     * @param carModel  the model of the car.
     * @param circuit   the circuit in which the specified account will drive.
     * @param createdAt the timestamp of when the order was created.
     * @param nLaps     the number of laps.
     * @param price     the price of the order.
     */
    public Order(String account, Date date, String carBrand, String carModel, String circuit, Timestamp createdAt, int nLaps, int price) {
        this.id = -1;
        this.account = account;
        this.date = date;
        this.carBrand =carBrand;
        this.carModel = carModel;
        this.circuit = circuit;
        this.createdAt = createdAt;
        this.nLaps = nLaps;
        this.price = price;
    }

    /**
     * Returns the id number of the order.
     *
     * @return id number of the order.
     */
    public int getId() { return id; }

    /**
     * Returns the account email that created the order.
     *
     * @return the account email that created the order.
     */
    public String getAccount() {
        return account;
    }

    /**
     * Returns the date in which the experience will take place.
     *
     * @return the date in which the experience will take place.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns the car brand.
     *
     * @return the car brand.
     */
    public String getCarBrand() {
        return carBrand;
    }

    /**
     * Returns the car model.
     *
     * @return the car model.
     */
    public String getCarModel() {
        return carModel;
    }

    /**
     * Returns the circuit name.
     *
     * @return the circuit name.
     */
    public String getCircuit() {
        return circuit;
    }

    /**
     * Returns timestamp of when the order was created.
     *
     * @return timestamp of when the order was created.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the number of laps.
     *
     * @return the number of laps.
     */
    public int getNLaps() {
        return nLaps;
    }

    /**
     * Returns the price of the order.
     *
     * @return the price of the order.
     */
    public int getPrice() {
        return price;
    }

    @Override
    protected final void writeJSON(final OutputStream out) throws IOException {
        // Actual implementation of writing of the JSON

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("order");

        jg.writeStartObject();

        jg.writeNumberField("id", id);

        jg.writeStringField("account", account);

        jg.writeStringField("date", date.toString());

        jg.writeStringField("carBrand", carBrand);

        jg.writeStringField("carModel", carModel);

        jg.writeStringField("circuit", circuit);

        jg.writeNumberField("nLaps", nLaps);

        jg.writeNumberField("price", price);

        jg.writeStringField("createdAt", createdAt.toString());

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

    /**
     * Creates a {@code Employee} from its JSON representation.
     *
     * @param in the input stream containing the JSON document.
     *
     * @return the {@code Employee} created from the JSON representation.
     *
     * @throws IOException if something goes wrong while parsing.
     */
    public static Order fromJSON(final InputStream in) throws IOException  {
        // Read the JSON that contains the employee

        // the fields read from JSON
        int id = -1;
        Date date = null;
        String account = null;
        String carBrand = null;
        String carModel = null;
        String circuit = null;
        Timestamp createdAt = null;
        int nLaps = -1;
        int price = -1;

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            // while we are not on the start of an element or the element is not
            // a token element, advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"order".equals(jp.getCurrentName())) { // Keep reading tokens
                LOGGER.info("current token " + jp.getCurrentName());
                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("No order object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no order object found.");
                }
            }

            // Now I'm sure I'm inside the employee

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {

                    switch (jp.getCurrentName()) {
                        case "id":
                            jp.nextToken(); // I know next token is the value
                            id = jp.getIntValue();
                            break;
                        case "date":
                            jp.nextToken();
                            String d = jp.getText();

                            try {
                                java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(d);
                                date = new Date(utilDate.getTime());
                            } catch (ParseException pe) {

                            }
                            break;
                        case "account":
                            jp.nextToken();
                            account = jp.getText();
                            break;
                        case "carBrand":
                            jp.nextToken();
                            carBrand = jp.getText();
                            break;
                        case "carModel":
                            jp.nextToken();
                            carModel = jp.getText();
                            break;
                        case "circuit":
                            jp.nextToken();
                            circuit = jp.getText();
                            break;
                        case "nLaps":
                            jp.nextToken();
                            nLaps = jp.getIntValue();
                            break;
                        case "price":
                            jp.nextToken();
                            price = jp.getIntValue();
                            break;
                        case "createdAt":
                            jp.nextToken();

                            Instant instant = Instant.parse(jp.getText());
                            createdAt = Timestamp.from(instant);
                            break;
                    }
                }
            }
        } catch(IOException e) {
            LOGGER.error("Unable to parse an order object from JSON.", e);
            throw e;
        }

        return new Order(id, account, date, carBrand, carModel, circuit, createdAt, nLaps, price);
    }
}
