package it.unipd.dei.webapp.wacar.utils;

/**
 * An enum to distinguish between car type and circuit type.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public enum CarOrCircuitType {

    /**
     * Represents the car type.
     */
    CAR_TYPE("car type"),

    /**
     * Represents the circuit type.
     */
    CIRCUIT_TYPE("circuit type");

    /**
     * The name of the Type. It will be displayed in messages.
     */
    private final String name;

    /**
     * Constructor of the enum.
     *
     * @param name the name of the Type, it will be displayed in messages.
     */
    CarOrCircuitType(String name){
        this.name = name;
    }

    /**
     * Get the name of the Type.
     *
     * @return the name of the type.
     */
    public String getName() {
        return name;
    }
}
