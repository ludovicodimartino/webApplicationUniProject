package it.unipd.dei.webapp.wacar.resource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Objects;

import static it.unipd.dei.webapp.wacar.resource.AbstractResource.JSON_FACTORY;

/**
 * This class represents a car.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Car extends AbstractResource{
    /**
     * The brand of the car
     */
    private final String brand;

    /**
     * The model of the car
     */
    private final String model;

    /**
     * The description of the car
     */
    private final String description;

    /**
     * The max speed of the car
     */
    private final int maxSpeed;

    /**
     * The horsepower of the car
     */
    private final int horsepower;

    /**
     * The acceleration of the car (0-100) in seconds
     */
    private final float acceleration;

    /**
     * Whether the car is available or not
     */
    private final boolean available;

    /**
     * The type of the car
     */
    private final String type;

    /**
     * The image of the car
     */
    private final byte[] image;

    /**
     * The MIME media type of the image of the car
     */
    private final String imageMediaType;

    public Car(final String brand, final String model, final String description, final int maxSpeed, final int horsepower, final float acceleration, final boolean available, final String type, final byte[] image, final String imageMediaType) {
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.maxSpeed = maxSpeed;
        this.horsepower = horsepower;
        this.acceleration = acceleration;
        this.available = available;
        this.type = type;
        this.image = image;
        this.imageMediaType = imageMediaType;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getHorsepower() {
        return horsepower;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getType() {
        return type;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImageMediaType() {
        return imageMediaType;
    }

    /*
    public JSONObject toJSON() {
        JSONObject uJson = new JSONObject();
        uJson.put("brand", brand);
        uJson.put("model", model);
        uJson.put("description", description);
        uJson.put("maxSpeed", maxSpeed);
        uJson.put("horsepower", horsepower);
        uJson.put("0-100", acceleration);
        uJson.put("available", available);
        return uJson;
    }
    */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {

        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();

        jg.writeFieldName("car");

        jg.writeStartObject();

        jg.writeStringField("brand", brand);

        jg.writeStringField("model", model);

        jg.writeStringField("description", description);

        jg.writeNumberField("max speed", maxSpeed);

        jg.writeNumberField("horsepower", horsepower);

        jg.writeNumberField("0-100", acceleration);

        jg.writeBooleanField("available", available);

        jg.writeStringField("type", type);

        /*
        if(this.hasPhoto()) {
            jg.writeStringField("photo",  photoPath);
        }
        */

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    @Override
    public String toString() {
        return "Car{" +
                "brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", description='" + description + '\'' +
                ", maxSpeed=" + maxSpeed +
                ", horsepower=" + horsepower +
                ", acceleration=" + acceleration +
                ", available=" + available +
                ", type='" + type + '\'' +
                '}';
    }

    public static Car fromJSON(final InputStream in) throws IOException {

        String jbrand = null;
        String jmodel = null;
        String jdescription = null;
        int jmaxSpeed = -1;
        int jhorsepower = -1;
        int jacceleration = -1;
        boolean javailable = false;
        String jtype = null;
        String jimage = null;
        String jimagemediatype = null;

        try {

            final JsonParser jp = JSON_FACTORY.createParser(in);
            // while we are not on the start of an element or the element is not a token element
            // advance to the next element (if any)
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"car".equals(jp.getCurrentName())) {
                if (jp.nextToken() == null) {
                    LOGGER.error("No Car object found in the stream.");
                    throw new EOFException("Unable to parse JSON: no Car object found.");
                }
            }
            while (jp.nextToken() != JsonToken.END_OBJECT) {
                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "brand":
                            jp.nextToken();
                            jbrand = jp.getText();
                            break;
                        case "model":
                            jp.nextToken();
                            jmodel = jp.getText();
                            break;
                        case "description":
                            jp.nextToken();
                            jdescription = jp.getText();
                            break;
                        case "maxSpeed":
                            jp.nextToken();
                            jmaxSpeed = jp.getIntValue();
                            break;
                        case "horsepower":
                            jp.nextToken();
                            jhorsepower = jp.getIntValue();
                            break;
                        case "0-100":
                            jp.nextToken();
                            jacceleration = jp.getIntValue();
                            break;
                        case "available":
                            jp.nextToken();
                            javailable = jp.getBooleanValue();
                            break;
                        case "type":
                            jp.nextToken();
                            jtype = jp.getText();
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
            LOGGER.error("Unable to parse a Car object from JSON.", e);
            throw e;
        }

        byte[] decodedPhoto = null;
        if (!Objects.equals(jimage, "") && jimage != null) decodedPhoto = Base64.getDecoder().decode(jimage);
        if (Objects.equals(jimagemediatype, "")) jimagemediatype = null;

        return new Car(jbrand, jmodel, jdescription, jmaxSpeed, jhorsepower, jacceleration, javailable, jtype, decodedPhoto, jimagemediatype);
    }

    public final boolean hasPhoto() {
        return image != null && image.length > 0 && imageMediaType != null && !imageMediaType.isBlank();
    }
}
