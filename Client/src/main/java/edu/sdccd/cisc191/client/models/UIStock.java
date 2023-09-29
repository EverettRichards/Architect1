package edu.sdccd.cisc191.client.models;

//This is a temporary import until Common app directory is functional.
import edu.sdccd.cisc191.client.common.Stock;

import java.util.Objects;

public class UIStock extends Stock {
    private Long id;
    public UIStock(Long id, String newTicker, String newName, String newDescription,
                   String newSector, double newPrice, double newDividend){
        this.setId(id);
        this.setTicker(newTicker);
        this.setName(newName);
        this.setDescription(newDescription);
        this.setDividend(newDividend);
        this.setSector(newSector);
        this.setPrice(newPrice);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return this.id; }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.getTicker());
    }

    @Override
    public String toString() {
        return "Stock{" + "id=" + this.id + ", ticker='" + this.getTicker() + '\'' + ", name='" + this.getName() + '\'' + ", description='" + this.getDescription() + '\'' + ", sharePrice=" + this.getPrice() + ", dividendYield=" + this.getDividend() + ", stockSector='" + this.getSector() + '\'' + '}';
    }
}
