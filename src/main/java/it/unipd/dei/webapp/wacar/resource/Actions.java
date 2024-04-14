package it.unipd.dei.webapp.wacar.resource;

/**
 * Contains constants for the actions performed by the application.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
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
     * Get the page for the insertion of a new car.
     */
    public static final String GET_INSERT_CAR_PAGE = "GET_INSERT_CAR_PAGE";


	/**
	 * Get the page for the insertion of a new circuit.
	 */
	public static final String GET_INSERT_CIRCUIT_PAGE = "GET_INSERT_CIRCUIT_PAGE";

	/**
	 * The search of circuit where a type of car can race.
	 */
	public static final String CREATE_ORDER_LIST_CARS = "CREATE_ORDER_LIST_CAR";

	/**
	 * Listing of all the available cars.
	 */
	public static final String CREATE_ORDER_LIST_SUITABLE_CIRCUIT = "CREATE_ORDER_LIST_SUITABLE_CIRCUIT";

	/**
	 * Display user form to complete the order.
	 */
	public static final String CREATE_ORDER_SET_OTHER_INFO = "CREATE_ORDER_SET_OTHER_INFO";

	/**
	 * Show the new order.
	 */
	public static final String SHOW_ORDER = "SHOW_ORDER";

	/**
	 * Insertion of a new order.
	 */
	public static final String INSERT_ORDER = "INSERT_ORDER";

	/**
	 * Loading of a car image.
	 */
	public static final String LOAD_CAR_IMAGE = "LOAD_CAR_IMAGE";

	/**
	 * Getting the page to insert/visualize and delete car-circuit mappings.
	 */
	public static final String GET_MAPPING_PAGE = "GET_MAPPING_PAGE";

    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}