package com.company;

import java.lang.reflect.Modifier;

public class Reflex {

    public static void main(String[] args) {
       TestForReflexion t = new TestForReflexion();
        Class test = t.getClass();
        int mods = test.getModifiers();
        System.out.println(test);
        if (Modifier.isPublic(mods)) {
            System.out.println("public");
        }
        if (Modifier.isPrivate(mods)) {
            System.out.println("private");
        }

    }
}
