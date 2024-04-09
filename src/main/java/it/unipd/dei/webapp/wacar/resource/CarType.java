package it.unipd.dei.webapp.wacar.resource;

/**
 * This class represents a car type.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CarType {
    private final String name;

    public CarType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
