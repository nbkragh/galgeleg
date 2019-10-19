package com.opgaver.galgeleg;

public class SingletonGalgeLogik {
    private static final GalgeLogik INSTANCE = new GalgeLogik();

    private SingletonGalgeLogik() {
    }

    static GalgeLogik getInstance() {
        return INSTANCE;
    }

}
