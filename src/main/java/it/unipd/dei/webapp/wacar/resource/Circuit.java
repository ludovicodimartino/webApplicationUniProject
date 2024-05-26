package it.unipd.dei.webapp.wacar.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Objects;

import static it.unipd.dei.webapp.wacar.resource.AbstractResource.JSON_FACTORY;

/**
 * Represents a circuit.
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Circuit extends AbstractResource {

    /**
     * The circuit name
     */
    private final String name;

    /**
     * The circuit type
     */
    private final String type;
    
    /**
     * The length (in meter) of the circuit
     */
    private final int length;

    /**
     * The number of the corners
     */
    private final int cornersNumber;

    /**
     * The address of the circuit
     */
    private final String address;

    /**
     * A description about the circuit
     */
    private final String description;

    /**
     * The price for making a lap
     */
    private final int lapPrice;

    /**
     * The availability of the circuit
     */
    private boolean available;

    /**
     * The image of the circuit
     */
    private final byte[] image;

    /**
     * The MIME media type of the image of the circuit
     */
    private final String imageMediaType;

    /**
     * Constructs a Circuit object with the specified attributes.
     *
     * @param name            The name of the circuit.
     * @param type            The type or category of the circuit (e.g., road, karting).
     * @param length          The length of the circuit in meters.
     * @param cornersNumber   The number of corners on the circuit.
     * @param address         The address/location of the circuit.
     * @param description     The description of the circuit.
     * @param lapPrice        The price of one lap on the circuit.
     * @param available       True if the circuit is available for use; otherwise, false.
     * @param image           The image data of the circuit.
     * @param imageMediaType The media type of the image (e.g., "image/jpeg", "image/png").
     */
    public Circuit(String name, String type, int length, int cornersNumber, String address, String description, int lapPrice, boolean available, final byte[] image, final String imageMediaType) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.cornersNumber = cornersNumber;
        this.address = address;
        this.description = description;
        this.lapPrice = lapPrice;
        this.available = available;
        this.image = image;
        this.imageMediaType = imageMediaType;
    }


    /**
     * Constructs a Circuit object with the specified attributes (assuming the circuit is available by default).
     *
     * @param name            The name of the circuit.
     * @param type            The type or category of the circuit (e.g., road, karting).
     * @param length          The length of the circuit in meters.
     * @param cornersNumber   The number of corners on the circuit.
     * @param address         The address/location of the circuit.
     * @param description     The description of the circuit.
     * @param lapPrice        The price of one lap on the circuit.
     * @param image           The image data of the circuit.
     * @param imageMediaType The media type of the image (e.g., "image/jpeg", "image/png").
     */
    public Circuit(String name, String type, int length, int cornersNumber, String address, String description, int lapPrice, final byte[] image, final String imageMediaType) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.cornersNumber = cornersNumber;
        this.address = address;
        this.description = description;
        this.lapPrice = lapPrice;
        this.image = image;
        this.imageMediaType = imageMediaType;
    }

    /**
     * Retrieves the name of the circuit.
     *
     * @return The name of the circuit.
     */
    public final String getName() {
        return name;
    }

    /**
     * Retrieves the type or category of the circuit.
     *
     * @return The type or category of the circuit.
     */
    public final String getType() {
        return type;
    }

    /**
     * Retrieves the length of the circuit in meters.
     *
     * @return The length of the circuit.
     */
    public final int getLength() {
        return length;
    }

    /**
     * Retrieves the number of corners on the circuit.
     *
     * @return The number of corners on the circuit.
     */
    public final int getCornersNumber() {
        return cornersNumber;
    }

    /**
     * Retrieves the address/location of the circuit.
     *
     * @return The address/location of the circuit.
     */
    public final String getAddress() {
        return address;
    }

    /**
     * Retrieves the description of the circuit.
     *
     * @return The description of the circuit.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Retrieves the price of one lap on the circuit.
     *
     * @return The price of one lap on the circuit.
     */
    public final int getLapPrice() {
        return lapPrice;
    }

    /**
     * Retrieves the availability status of the circuit.
     *
     * @return {@code true} if the circuit is available; {@code false} otherwise.
     */
    public final boolean getAvailable() {
        return available;
    }

    /**
     * Retrieves the image data of the circuit.
     *
     * @return The image data of the circuit.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Retrieves the media type of the circuit image.
     *
     * @return The media type of the circuit image (e.g., "image/jpeg", "image/png").
     */
    public String getImageMediaType() {
        return imageMediaType;
    }

    /**
     * Returns a string representation of the circuit object.
     *
     * @return A string containing the name, type, length, corners number, address,
     *         description, lap price, and availability of the circuit.
     */
    @Override
    public String toString() {
        return "Circuit{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", cornersNumber=" + cornersNumber +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", lapPrice=" + lapPrice +
                ", available=" + available +
                '}';
    }

    /**
     * Writes the circuit data in JSON format to the provided output stream.
     *
     * @param out The output stream to which the JSON data will be written.
     * @throws Exception If an error occurs while writing JSON data to the output stream.
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("circuit");

        jg.writeStartObject();

        jg.writeStringField("name", name);

        jg.writeStringField("type", type);

        jg.writeNumberField("length", length);

        jg.writeNumberField("cornersNumber", cornersNumber);

        jg.writeStringField("address", address);

        jg.writeStringField("description", description);

        jg.writeNumberField("lapPrice", lapPrice);

        jg.writeBooleanField("available", available);

        /*
        if(this.hasPhoto()) {
            jg.writeStringField("photo",  photoPath);
        }
        */

        jg.writeStringField("imageMediaType", imageMediaType);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

    /**
     * Constructs a Circuit object from JSON data read from the provided input stream.
     *
     * @param in The input stream containing the JSON data representing the circuit object.
     * @return A new Circuit object constructed from the JSON data.
     * @throws IOException if there's an error reading from the input stream or parsing the JSON data.
     */
    public static Circuit fromJSON(final InputStream in) throws IOException {

        String jname = null;
        String jtype = null;
        int jlength = -1;
        int jcornersNumber = -1;
        String jaddress = null;
        String jdescription = null;
        int jlapPrice = -1;
        boolean javailable = false;
        String jimage = null;
        String jimagemediatype = null;

        try {

            final JsonParser jp = JSON_FACTORY.createParser(in);
            // while we are not on the start of an element or the element is not a token element
            // advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"circuit".equals(jp.getCurrentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No Circuit object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Circuit object found.");
                }
            }
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "name":
                            jp.nextToken();
                            jname = jp.getText();
                            break;
                        case "type":
                            jp.nextToken();
                            jtype = jp.getText();
                            break;
                        case "length":
                            jp.nextToken();
                            jlength = jp.getIntValue();
                            break;
                        case "cornersNumber":
                            jp.nextToken();
                            jcornersNumber = jp.getIntValue();
                            break;
                        case "address":
                            jp.nextToken();
                            jaddress = jp.getText();
                            break;
                        case "description":
                            jp.nextToken();
                            jdescription = jp.getText();
                            break;
                        case "lapPrice":
                            jp.nextToken();
                            jlapPrice = jp.getIntValue();
                            break;
                        case "available":
                            jp.nextToken();
                            javailable = jp.getBooleanValue();
                            break;
                        case "image":
                            jp.nextToken();
                            jimage = jp.getText();
                            break;
                        case "imageMediaType":
                            jp.nextToken();
                            jimagemediatype = jp.getText();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse a Circuit object from JSON.", e);
            throw e;
        }

        byte[] decodedImage = null;
        if(!Objects.equals(jimage, "") && jimage != null) decodedImage = Base64.getDecoder().decode(jimage);
        if(Objects.equals(jimagemediatype, "")) jimagemediatype=null;

        return new Circuit(jname, jtype, jlength, jcornersNumber, jaddress, jdescription, jlapPrice, javailable, decodedImage, jimagemediatype);
    }

    /**
     * Checks if the circuit has a photo available.
     *
     * @return {@code true} if the circuit has a photo; {@code false} otherwise.
     */
    public final boolean hasPhoto() {
        return image != null && image.length > 0 && imageMediaType != null && !imageMediaType.isBlank();
    }
}
