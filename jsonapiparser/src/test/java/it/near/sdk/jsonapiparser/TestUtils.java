package it.near.sdk.jsonapiparser;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author Federico Boschini
 */
public class TestUtils {

    public static JSONObject readJsonFile(Class clazz, String fileName) throws Exception {
        return new JSONObject(readStringFromFile(clazz, fileName));
    }

    public static String readStringFromFile(Class clazz, String fileName) throws Exception {
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(fileName);
        return TestUtils.readTextStream(inputStream);
    }

    public static String readTextStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

}
