package com.klee0kai.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionAnalyzer {

    public static void analyze(Object obj) {
        System.out.println("analyze obj: " + obj.toString());
        System.out.println("class: " + obj.getClass());

        for (Constructor<?> constr : obj.getClass().getConstructors()) {
            StringBuilder argsStr = new StringBuilder("(");
            for (Class<?> arg : constr.getParameterTypes()) {
                argsStr.append(" ").append(arg).append(",");
            }
            argsStr.append(")");
            System.out.println("constructor: " + argsStr);

        }
        for (Field field : obj.getClass().getFields()) {
            String isStatic = (field.getModifiers() & Modifier.STATIC) != 0 ? "static" : "";
            System.out.println("field: " + isStatic + " " + field.getName() + " " + field.getType());
        }
        for (Method method : obj.getClass().getMethods()) {
            StringBuilder argsStr = new StringBuilder("(");
            for (Class<?> arg : method.getParameterTypes()) {
                argsStr.append(" ").append(arg).append(",");
            }
            argsStr.append(")");
            String isStatic = (method.getModifiers() & Modifier.STATIC) != 0 ? "static" : "";

            System.out.println("method: " + isStatic + " " + method.getReturnType() + " " + method.getName() + argsStr);
        }

    }

}
