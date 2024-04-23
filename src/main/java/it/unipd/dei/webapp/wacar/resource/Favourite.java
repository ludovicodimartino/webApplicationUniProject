package it.unipd.dei.webapp.wacar.resource;

import org.json.JSONObject;

import java.sql.Timestamp;

public class Favourite{
    private String circuit;
    private String carBrand;
    private String carModel;
    private String account;
    private Timestamp createdAt;

    /**
     * Create a Favourite combination of car and circuit
     * @param circuit
     * @param carBrand
     * @param carModel
     * @param account
     * @param createdAt
     */
    public Favourite(String circuit, String carBrand, String carModel, String account, Timestamp createdAt){
        this.circuit = circuit;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.account = account;
        this.createdAt = createdAt;
    }

    public Favourite(String circuit, String carBrand, String carModel, String account){
        this.circuit = circuit;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.account = account;
    }

    public final String getCircuit() {
        return circuit;
    }

    public final String getAccount() {
        return account;
    }

    public final String getCarBrand() {
        return carBrand;
    }

    public final String getCarModel() {
        return carModel;
    }

    public final Timestamp getCreatedAt() {
        return createdAt;
    }

    public JSONObject toJSON(){
        JSONObject uJson = new JSONObject();
        uJson.put("circuit", circuit);
        uJson.put("carBrand", carBrand);
        uJson.put("carModel", carModel);
        uJson.put("account", account);
        uJson.put("createdAt", createdAt);

        return uJson;
    }


}