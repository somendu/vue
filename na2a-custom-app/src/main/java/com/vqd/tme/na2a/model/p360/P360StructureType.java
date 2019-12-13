package com.vqd.tme.na2a.model.p360;

public enum P360StructureType {

    PRODUCT_STRUCTURE ("Product Structure"),
    VEHICLE_STRUCTURE ("Vehicle Structure"),
    GENERATION_STRUCTURE ("Generation Structure"),
    LOCAL_VEHICLE_STRUCTURE ("Local Vehicle Structure"),
    PROJECT_STRUCTURE ("Project Structure"),
    SETTINGS_STRUCTURE ("Settings Structure"),
    INCOMPATIBILITY_STRUCTURE ("Incompatibility Structure"),
    MAGLEV_STRUCTURE ("Maglev Structure"),
    UNKNOWN_STRUCTURE ("Unknown Structure");

    private final String name;

    P360StructureType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static P360StructureType findByName(String name) {
        for (P360StructureType type : P360StructureType.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return UNKNOWN_STRUCTURE;
    }
}
