package it.unipd.dei.webapp.wacar.resource;

/**
 * Contains constants for the actions performed by the application.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Actions {
    /**
     * Insertion of a new car.
     */
    public static final String INSERT_CAR = "INSERT_CAR";

    /**
     * Insertion of a new circuit.
     */
    public static final String INSERT_CIRCUIT = "INSERT_CIRCUIT";

    /**
     * Insertion of a new circuit.
     */
    public static final String GET_INSERT_CAR_PAGE = "GET_INSERT_CAR_PAGE";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}
