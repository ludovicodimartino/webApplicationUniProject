package it.unipd.dei.webapp.wacar.resource;

import org.json.JSONObject;

/**
 * This class represents a car.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Car {
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
    private final int acceleration;

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

    public Car(final String brand, final String model, final String description, final int maxSpeed, final int horsepower, final int acceleration, final boolean available, final String type, final byte[] image, final String imageMediaType) {
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

    public int getAcceleration() {
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

    public JSONObject toJSON(){
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
}
