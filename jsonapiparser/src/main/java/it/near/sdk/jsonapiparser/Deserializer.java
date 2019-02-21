package it.near.sdk.jsonapiparser;

import java.lang.reflect.Field;
import java.util.HashMap;

import it.near.sdk.jsonapiparser.exceptions.NotExtendingResourceException;

/**
 * Deserializer uses reflection to create objects and set fields.
 */
public class Deserializer {

    private HashMap<String, Class> registeredClasses = new HashMap<>();

    /**
     * Register your class for a JSON type.
     * <p>
     * Example:
     * registerResourceClass("articles", Article.class);
     *
     * @param typeName      Name of the JSONAPI type.
     * @param resourceClass Class for mapping.
     * @see Resource
     */
    public void registerResourceClass(String typeName, Class resourceClass) {
        registeredClasses.put(typeName, resourceClass);
    }

    /**
     * Creates an instance of an object via its name.
     *
     * @param resourceName Name of the resource.
     * @return Instance of the resourceName class.
     * @throws InstantiationException        Throws exception when not able to create instance of class.
     * @throws IllegalAccessException        Throws exception when not able to create instance of class.
     * @throws NotExtendingResourceException Throws exception when not able to create instance of class.
     */
    public Resource createObjectFromString(String resourceName) throws InstantiationException, IllegalAccessException, NotExtendingResourceException {
        Class objectClass = registeredClasses.get(resourceName);
        try {
            return (Resource) objectClass.newInstance();
        } catch (InstantiationException e) {
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        } catch (ClassCastException e) {
            throw new NotExtendingResourceException(objectClass + " is not inheriting Resource");
        }
    }

    /**
     * Sets the field of the resourceObject with the data.
     *
     * @param resourceObject Object with field to be set.
     * @param fieldName      Name of the field.
     * @param data           Data to set.
     * @return Resource with or without field set
     */
    public Resource setField(Resource resourceObject, String fieldName, Object data) {
        Field field = null;
        try {
            field = findUnderlying(resourceObject.getClass(), fieldName);
            field.set(resourceObject, data);
        } catch (IllegalAccessException e) {
            Logger.debug("Could not access " + field.getName() + " field");
        } catch (IllegalArgumentException e) {
            // Logger.debug("The field " + field.getName() + " may be null");
        } catch (NoSuchFieldException e) {
            // e.printStackTrace();
        }

        return resourceObject;
    }

    public static Field findUnderlying(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> current = clazz;
        do {
            try {
                return current.getDeclaredField(fieldName);
            } catch (Exception e) {
            }
        } while ((current = current.getSuperclass()) != null);
        throw new NoSuchFieldException();
    }

    /**
     * Sets the Id field of the resourceObject extending {@link Resource}.
     *
     * @param resourceObject Object extending {@link Resource}.
     * @param data           Data with Id (as String or Int)
     * @return ResourceObject with set Id as String.
     * @throws NotExtendingResourceException when none of the superclasses are {@link Resource}.
     */
    public Resource setIdField(Resource resourceObject, Object data) throws NotExtendingResourceException {
        Class superClass;
        try {
            superClass = getJsonAPIParserResourceSuperClass(resourceObject);
        } catch (NotExtendingResourceException e) {
            throw e;
        }

        try {
            Field field = superClass.getDeclaredField("Id");
            field.setAccessible(true);
            if (data instanceof String) {
                field.set(resourceObject, data);
            } else {
                field.set(resourceObject, String.valueOf(data));
            }
        } catch (NoSuchFieldException e) {
            Logger.debug("No field Id found. That should not happened.");
        } catch (IllegalAccessException e) {
            Logger.debug("Could not access field Id");
        }

        return resourceObject;
    }

    /**
     * Returns the superclass if instance of {@link Resource}.
     *
     * @param resourceObject Object to find the superclass.
     * @return {@link Resource} class.
     * @throws NotExtendingResourceException when resourceObject is not extending {@link Resource}.
     */
    private Class getJsonAPIParserResourceSuperClass(Resource resourceObject) throws NotExtendingResourceException {
        Class superClass = resourceObject.getClass().getSuperclass();
        do {
            if (superClass == Resource.class) {
                break;
            }
            superClass = superClass.getSuperclass();
        } while (superClass != null);

        if (superClass == null) { //should not happen, cause createObjectFromString() checks
            throw new NotExtendingResourceException(resourceObject.getClass() + " is not inheriting Resource");
        }

        return superClass;
    }

    public HashMap<String, Class> getRegisteredClasses() {
        return registeredClasses;
    }

    public void setRegisteredClasses(HashMap<String, Class> registeredClasses) {
        this.registeredClasses = registeredClasses;
    }
}
