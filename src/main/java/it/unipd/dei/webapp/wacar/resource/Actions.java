package it.unipd.dei.webapp.wacar.resource;

/**
 * Contains constants for the actions performed by the application.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Actions {
	/**
	 * Get the information of an admin.
	 */
	public static final String ADMIN_INFO = "ADMIN_INFO";

	/**
	 * Get the information of a user.
	 */
	public static final String USER_INFO = "USER_INFO";

	/**
	 * Update user informations
	 */
	public static final String UPDATE_ACCOUNT= "UPDATE_ACCOUNT";

    /**
     * Insertion of a new car.
     */
    public static final String INSERT_CAR = "INSERT_CAR";

    /**
     * Insertion of a new circuit.
     */
    public static final String INSERT_CIRCUIT = "INSERT_CIRCUIT";

	/**
	 * Insertion of a new circuit type.
	 */
	public static final String INSERT_CIRCUIT_TYPE = "INSERT_CIRCUIT_TYPE";

	/**
	 * Insertion of a new car type.
	 */
	public static final String INSERT_CAR_TYPE = "INSERT_CAR_TYPE";

    /**
     * Get the page for the insertion of a new car.
     */
    public static final String GET_INSERT_CAR_PAGE = "GET_INSERT_CAR_PAGE";

	/**
	 * Get the page for the insertion of a new circuit.
	 */
	public static final String GET_INSERT_CIRCUIT_PAGE = "GET_INSERT_CIRCUIT_PAGE";

	/**
	 * Get the page for the insertion of a new circuit type.
	 */
	public static final String GET_INSERT_CIRCUIT_TYPE_PAGE = "GET_INSERT_CIRCUIT_TYPE_PAGE";

	/**
	 * Get the page for the insertion of a new car type.
	 */
	public static final String GET_INSERT_CAR_TYPE_PAGE = "GET_INSERT_CAR_TYPE_PAGE";


	/**
	 * Get of a favourite.
	 */
	public static final String GET_FAVOURITE = "GET_FAVOURITE";

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
	 * Insertion of a new favourite
	 */
	public static final String INSERT_FAVOURITE = "INSERT_FAVOURITE";

	/**
	 * Show the new favourite
	 */
	public static final String SHOW_FAVOURITE = "SHOW_FAVOURITE";

	/**
	 * List all favourites of the user
	 */
	public static final String LIST_FAVOURITES = "LIST_FAVOURITES";

	/**
	 * Delete the selected favourite
	 */
	public static final String DELETE_FAVOURITE = "DELETE_FAVOURITE";

	/**
	 * Loading of a car image
	 */
	public static final String LOAD_CAR_IMAGE = "LOAD_CAR_IMAGE";

	/**
	 * Loading of a car image
	 */
	public static final String LOAD_CIRCUIT_IMAGE = "LOAD_CIRCUIT_IMAGE";

	/**
	 * Getting the page to insert/visualize and delete car-circuit mappings.
	 */
	public static final String GET_MAPPING_PAGE = "GET_MAPPING_PAGE";

	/**
	 * Insert a car-circuit mapping.
	 */
	public static final String INSERT_MAPPING = "INSERT_MAPPING";

	/**
	 * Delete a car-circuit mapping.
	 */
	public static final String DELETE_MAPPING = "DELETE_MAPPING";

	/**
	 * Edit a car.
	 */
	public static final String EDIT_CAR = "EDIT_CAR";

	/**
	 * Get the edit car page.
	 */
	public static final String GET_EDIT_CAR_PAGE = "GET_EDIT_CAR_PAGE";

	/**
	 * Getting the page to insert/visualize and delete car-circuit mappings.
	 */
	public static final String GET_ALL_CARS = "GET_ALL_CARS";

	/**
	 * Getting the page to insert/visualize and delete car-circuit mappings.
	 */
	public static final String GET_ALL_CIRCUITS = "GET_ALL_CIRCUITS";

	/**
	 * Getting the orders of a specific user.
	 */
	public static final String GET_ORDERS_BY_ACCOUNT = "GET_ORDERS_BY_ACCOUNT";

	/**
	 * Getting the orders of a specific id.
	 */
	public static final String GET_ORDERS_BY_ID = "GET_ORDERS_BY_ID";

	/**
	 * Getting the orders of a specific id.
	 */
	public static final String DELETE_ORDER = "DELETE_ORDER";

	/**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}