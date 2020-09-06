package fr.rushcubeland.rcbapi.options;

import java.util.Arrays;

public enum OptionUnit {

    NEVER("NEVER"),
    ONLY_FRIENDS("ONLY_FRIENDS"),
    OPEN("OPEN");

    private String name;

    OptionUnit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static OptionUnit getByName(String name){
        return Arrays.stream(values()).filter(r -> r.getName().equalsIgnoreCase(name)).findAny().orElse(OptionUnit.OPEN);
    }

}
