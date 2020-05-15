package hjem.is.model;

public class StorageMetaData {
    private float percentInventoryCost;
    private Integer id = null;

    public StorageMetaData(float percentInventoryCost) {
        this.percentInventoryCost = percentInventoryCost;
    }

    public StorageMetaData(float percentInventoryCost, Integer id) {
        this(percentInventoryCost);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public float getPercentInventoryCost() {
        return percentInventoryCost;
    }

    public void setPercentInventoryCost(float percentInventoryCost) {
        this.percentInventoryCost = percentInventoryCost;
    }
}
