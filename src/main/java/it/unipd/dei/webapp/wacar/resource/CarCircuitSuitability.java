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

    /**
     * Constructs a CarCircuitSuitability object with the specified car type and circuit type.
     *
     * @param carType     The type of car.
     * @param circuitType The type of circuit.
     */
    public CarCircuitSuitability(String carType, String circuitType) {
        this.carType = carType;
        this.circuitType = circuitType;
    }

    /**
     * Retrieves the type of car associated with this suitability.
     *
     * @return The type of car.
     */
    public String getCarType() {
        return carType;
    }

    /**
     * Retrieves the type of circuit associated with this suitability.
     *
     * @return The type of circuit.
     */
    public String getCircuitType() {
        return circuitType;
    }
}
