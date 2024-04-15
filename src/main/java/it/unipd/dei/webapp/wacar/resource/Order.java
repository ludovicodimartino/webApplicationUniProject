package it.unipd.dei.webapp.wacar.resource;

import java.sql.Timestamp;
import java.sql.Date;

/**
 * Represents an order.
 * 
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class    Order {
    /**
     * The account that made the order
     */
    private final String account;

    /**
     * The date
     */
    private final Date date;

    /**
     * The car brand
     */
    private final String carBrand;

    /**
     * The car model
     */
    private final String carModel;

    /**
     * The name of the circuit
     */
    private final String circuit;

    /**
     * Thew time in which the account created the order
     */
    private final Timestamp createdAt;

    /**
     * The number of laps 
     */
    private final int nLaps;

    /**
     * The price
     */
    private final int price;

    public Order(String account, Date date, String carBrand, String carModel, String circuit, Timestamp createdAt, int nLaps, int price) {
        this.account = account;
        this.date = date;
        this.carBrand =carBrand;
        this.carModel = carModel;
        this.circuit = circuit;
        this.createdAt = createdAt;
        this.nLaps = nLaps;
        this.price = price;
    }

    public String getAccount() {
        return account;
    }

    public Date getDate() {
        return date;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public String getCircuit() {
        return circuit;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public int getNLaps() {
        return nLaps;
    }

    public int getPrice() {
        return price;
    }
}
