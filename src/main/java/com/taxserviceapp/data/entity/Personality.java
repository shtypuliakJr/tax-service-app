package com.taxserviceapp.data.entity;

public enum Personality {

    NATURAL_PERSON("Natural person"),
    LEGAL_PERSON("Legal person");

    private String personalityName;

    Personality(String personalityName) {
        this.personalityName = personalityName;
    }

    public String getPersonalityName() {
        return personalityName;
    }
}
