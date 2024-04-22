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

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final int getLength() {
        return length;
    }

    public final int getCornersNumber() {
        return cornersNumber;
    }

    public final String getAddress() {
        return address;
    }

    public final String getDescription() {
        return description;
    }

    public final int getLapPrice() {
        return lapPrice;
    }

    public final boolean getAvailable() {
        return available;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImageMediaType() {
        return imageMediaType;
    }

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

    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("circuit");

        jg.writeStartObject();

        jg.writeStringField("name", name);

        jg.writeStringField("type", type);

        jg.writeNumberField("length", length);

        jg.writeNumberField("corners number", cornersNumber);

        jg.writeStringField("address", address);

        jg.writeStringField("description", description);

        jg.writeNumberField("lap price", lapPrice);

        jg.writeBooleanField("available", available);

        /*
        if(this.hasPhoto()) {
            jg.writeStringField("photo",  photoPath);
        }
        */

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }

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

        byte[] decodedPhoto= null;
        if(!Objects.equals(jimage, "") && jimage != null) decodedPhoto= Base64.getDecoder().decode(jimage);
        if(Objects.equals(jimagemediatype, "")) jimagemediatype=null;

        return new Circuit(jname, jtype, jlength, jcornersNumber, jaddress, jdescription, jlapPrice, javailable, decodedPhoto, jimagemediatype);
    }

    public final boolean hasPhoto() {
        return image != null && image.length > 0 && imageMediaType != null && !imageMediaType.isBlank();
    }
}
