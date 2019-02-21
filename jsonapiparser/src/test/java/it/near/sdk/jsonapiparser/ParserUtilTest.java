package it.near.sdk.jsonapiparser;

import com.google.common.collect.Maps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class ParserUtilTest {

    @Test
    public void shouldBuildProperJsonAPIParser() {
        Map<String, Class> map = Maps.newHashMap();
        map.put("string", String.class);
        map.put("long", Long.class);
        map.put("date", Date.class);
        map.put("list", List.class);

        // Deserializer.setRegisteredClasses(Maps.<String, Class>newHashMap());
        JsonAPIParser jsonAPIParser = ParserUtil.buildJsonAPIParserFrom(map);

        assertThat(jsonAPIParser.getFactory().getDeserializer().getRegisteredClasses(), is(map));
    }

}