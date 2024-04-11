package it.unipd.dei.webapp.wacar.resource;

/**
 * Represents which type of circuit a type of car can race on.
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CarCircuitSuitability {
    /**
     * The type of the car
     */
    private final String carType;

    /**
     * The type of the circuit
     */
    private final String circuitType;

    public CarCircuitSuitability(String carType, String circuitType) {
        this.carType = carType;
        this.circuitType = circuitType;
    }

    public String getCarType() {
        return carType;
    }

    public String getCircuitType() {
        return circuitType;
    }
}
