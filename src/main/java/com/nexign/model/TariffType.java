package com.nexign.model;

public enum TariffType {
    UNLIMITED("06"),
    BY_MINUTE("03"),
    ORDINARY("11");

    private final String tariff;

    TariffType(String tariff) {
        this.tariff = tariff;
    }

    public String getTariff() {
        return tariff;
    }

    public static TariffType fromString(String tariff) {
        for (TariffType type : TariffType.values()) {
            if (type.getTariff().equals(tariff)) {
                return type;
            }
        }
        return null;
    }
}
