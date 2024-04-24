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

/**
 * Class to represent an object of type Car
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

    /**
     * Constructs a new Car object with the specified attributes.
     *
     * @param brand           The brand of the car.
     * @param model           The model of the car.
     * @param description     The description of the car.
     * @param maxSpeed        The maximum speed of the car in kilometers per hour.
     * @param horsepower      The horsepower of the car.
     * @param acceleration    The acceleration of the car (0-100 km/h time in seconds).
     * @param available       True if the car is available for use; otherwise, false.
     * @param type            The type or category of the car (e.g., sedan, SUV).
     * @param image           The image data of the car.
     * @param imageMediaType  The media type of the image (e.g., "image/jpeg", "image/png").
     */
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

    /**
     * Retrieves the brand of the car.
     *
     * @return The brand of the car.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Retrieves the model of the car.
     *
     * @return The model of the car.
     */
    public String getModel() {
        return model;
    }

    /**
     * Retrieves the description of the car.
     *
     * @return The description of the car.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the maximum speed of the car in kilometers per hour.
     *
     * @return The maximum speed of the car.
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Retrieves the horsepower of the car.
     *
     * @return The horsepower of the car.
     */
    public int getHorsepower() {
        return horsepower;
    }

    /**
     * Retrieves the acceleration of the car (0-100 km/h time in seconds).
     *
     * @return The acceleration of the car.
     */
    public float getAcceleration() {
        return acceleration;
    }

    /**
     * Checks if the car is available for use.
     *
     * @return True if the car is available; otherwise, false.
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Retrieves the type or category of the car.
     *
     * @return The type or category of the car.
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieves the image data of the car.
     *
     * @return The image data of the car.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Retrieves the media type of the car image.
     *
     * @return The media type of the car image (e.g., "image/jpeg", "image/png").
     */
    public String getImageMediaType() {
        return imageMediaType;
    }

    /**
     * Writes the car object's data in JSON format to the provided output stream.
     *
     * @param out The output stream to write the JSON data to.
     * @throws Exception if there's an error during JSON writing.
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
            jg.writeStringField("image",  photoPath);
        }
        */

        jg.writeStringField("imageMediaType", imageMediaType);

        jg.writeEndObject();

        jg.writeEndObject();

        jg.flush();
    }


    /**
     * Returns a string representation of the car object.
     *
     * @return A string containing the brand, model, description, maximum speed, horsepower,
     *         acceleration, availability, and type of the car.
     */
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

    /**
     * Constructs a Car object from JSON data read from the provided input stream.
     *
     * @param in The input stream containing the JSON data representing the car object.
     * @return A new Car object constructed from the JSON data.
     * @throws IOException if there's an error reading from the input stream or parsing the JSON data.
     */
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

        byte[] decodedImage = null;
        if (!Objects.equals(jimage, "") && jimage != null) decodedImage = Base64.getDecoder().decode(jimage);
        if (Objects.equals(jimagemediatype, "")) jimagemediatype = null;

        return new Car(jbrand, jmodel, jdescription, jmaxSpeed, jhorsepower, jacceleration, javailable, jtype, decodedImage, jimagemediatype);
    }
    /**
     * Checks if the car has a photo available.
     *
     * @return {@code true} if the car has a photo; {@code false} otherwise.
     */
    public final boolean hasPhoto() {
        return image != null && image.length > 0 && imageMediaType != null && !imageMediaType.isBlank();
    }
}
