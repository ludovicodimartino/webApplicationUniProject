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

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
