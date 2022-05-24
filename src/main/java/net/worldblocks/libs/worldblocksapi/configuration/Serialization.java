package net.worldblocks.libs.worldblocksapi.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class Serialization {

    private static final List<Class<? extends Serializable>> CONFIG_MAP = new ArrayList<>();

    public static void register(Class<? extends Serializable> clazz) {
        CONFIG_MAP.add(clazz);
    }

    public static Class<? extends Serializable> getSerializableClass(Serializable configItem) {
        for (Class<? extends Serializable> configItems : CONFIG_MAP) {
            if (configItems.equals(configItem.getClass())) {
                return configItems;
            }
        }
        return null;
    }

    public static Object deserialize(Class<? extends Serializable> clazz, YamlFile yamlFile, String path) {
        Field field;
        Method method;
        try {
            method = clazz.getMethod("deserialize", YamlFile.class, String.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }

        if (!Modifier.isStatic(method.getModifiers())) {
            return null;
        }

        try {
            return method.invoke(clazz, yamlFile, path);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;

    }

}
