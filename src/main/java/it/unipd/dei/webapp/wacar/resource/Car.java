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
    private final String brand;
    private final String model;
    private final String description;
    private final int maxSpeed;
    private final int horsepower;
    private final int acceleration;
    private final boolean available;
    private final String type;
    private final String image; // Temporary

    public Car(String brand, String model, String description, int maxSpeed, int horsepower, int acceleration, boolean available, String type, String image) {
        this.brand = brand;
        this.model = model;
        this.description = description;
        this.maxSpeed = maxSpeed;
        this.horsepower = horsepower;
        this.acceleration = acceleration;
        this.available = available;
        this.type = type;
        this.image = image;
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

    public String getImage() {
        return image;
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
