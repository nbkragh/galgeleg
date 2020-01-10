package com.opgaver.galgeleg;

public class GalgeLogikSingleton {
    private static final GalgeLogik INSTANCE = new GalgeLogik();

    private GalgeLogikSingleton() {
    }

    static GalgeLogik getInstance() {
        return INSTANCE;
    }

}
