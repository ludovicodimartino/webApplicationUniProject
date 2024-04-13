package it.unipd.dei.webapp.wacar.resource;

/**
 * Represents a circuit.
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class Circuit {

    /**
     * The circuit name
     */
    private final String name;

    /**
     * The circuit type
     */
    private final String type;
    
    /**
     * The length (in meter) of the circuit
     */
    private final int length;

    /**
     * The number of the corners
     */
    private final int cornersNumber;

    /**
     * The address of the circuit
     */
    private final String address;

    /**
     * A description about the circuit
     */
    private final String description;

    /**
     * The price for making a lap
     */
    private final int lapPrice;

    /**
     * The availability of the circuit
     */
    private boolean available;

    /**
     * The image of the circuit
     */
    private final byte[] image;

    /**
     * The MIME media type of the image of the circuit
     */
    private final String imageMediaType;

    public Circuit(String name, String type, int length, int cornersNumber, String address, String description, int lapPrice, boolean available, final byte[] image, final String imageMediaType) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.cornersNumber = cornersNumber;
        this.address = address;
        this.description = description;
        this.lapPrice = lapPrice;
        this.available = available;
        this.image = image;
        this.imageMediaType = imageMediaType;
    }

    public Circuit(String name, String type, int length, int cornersNumber, String address, String description, int lapPrice, final byte[] image, final String imageMediaType) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.cornersNumber = cornersNumber;
        this.address = address;
        this.description = description;
        this.lapPrice = lapPrice;
        this.image = image;
        this.imageMediaType = imageMediaType;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final int getLength() {
        return length;
    }

    public final int getCornersNumber() {
        return cornersNumber;
    }

    public final String getAddress() {
        return address;
    }

    public final String getDescription() {
        return description;
    }

    public final int getLapPrice() {
        return lapPrice;
    }

    public final boolean getAvailable() {
        return available;
    }

    public byte[] getImage() {
        return image;
    }

    public String getImageMediaType() {
        return imageMediaType;
    }

    @Override
    public String toString() {
        return "Circuit{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", length=" + length +
                ", cornersNumber=" + cornersNumber +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", lapPrice=" + lapPrice +
                ", available=" + available +
                '}';
    }
}
