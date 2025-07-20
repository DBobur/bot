package uz.app.service.model.enums;

public enum QuantityUnit {
    PIECE("pcs"),
    KILOGRAM("kg"),
    LITER("l");

    private final String unitName;

    QuantityUnit(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }
}

