package it.near.sdk.jsonapiparser;

import java.util.Map;

public class ParserUtil {

    public static JsonAPIParser buildJsonAPIParserFrom(Map<String, Class> classes) {
        JsonAPIParser jsonAPIParser = new JsonAPIParser();
        for (Map.Entry<String, Class> entry : classes.entrySet()) {
            jsonAPIParser.getFactory().getDeserializer().registerResourceClass(entry.getKey(), entry.getValue());
        }
        return jsonAPIParser;
    }
}
