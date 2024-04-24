package it.unipd.dei.webapp.wacar.resource;

/**
 * Represents a type (can be either a type of car or a type of circuit).
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Type {
    /**
     * The type name
     */
    private final String name;

    /**
     * Constructs a Type object with the specified name.
     *
     * @param name The name of the type.
     */
    public Type(String name) {
        this.name = name;
    }

    /**
     * Retrieves the name of the type.
     *
     * @return The name of the type.
     */
    public String getName() {
        return name;
    }
}
