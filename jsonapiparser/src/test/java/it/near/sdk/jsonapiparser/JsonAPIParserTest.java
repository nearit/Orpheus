package it.near.sdk.jsonapiparser;

import com.google.common.collect.Maps;

import org.hamcrest.core.IsInstanceOf;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import it.near.sdk.jsonapiparser.models.SingleRelationship;
import it.near.sdk.jsonapiparser.models.TestChildModel;
import it.near.sdk.jsonapiparser.models.TestModel;
import it.near.sdk.jsonapiparser.models.TestWithChildModel;
import it.near.sdk.jsonapiparser.models.TestWithChildrenModel;
import it.near.sdk.jsonapiparser.models.inheritance.TestFather;
import it.near.sdk.jsonapiparser.models.inheritance.TestMother;
import it.near.sdk.jsonapiparser.models.inheritance.TestParent;
import it.near.sdk.jsonapiparser.utils.JsonAPIUtils;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

/**
 * Created by cattaneostefano on 27/02/2017.
 */
public class JsonAPIParserTest {

    private JsonAPIParser jsonAPIParser;

    @Before
    public void setUP() {
        Deserializer.setRegisteredClasses(Maps.<String, Class>newHashMap());
        jsonAPIParser = new JsonAPIParser();
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("test", TestModel.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("test_child", TestChildModel.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("test_with_child", TestWithChildModel.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("test_with_children", TestWithChildrenModel.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("mother", TestMother.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("father", TestFather.class);
        jsonAPIParser.getFactory().getDeserializer().registerResourceClass("test_with_rel", SingleRelationship.class);
    }

    @Test
    public void parseElement() throws Exception {
        JSONObject jsonObject = readJsonFile("single_resource.json");
        TestModel object = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestModel.class);
        assertNotNull(object);
        assertThat(object, instanceOf(TestModel.class));
        assertThat(object.getId(), is("1"));
        assertThat(object.content, is("contenuto"));
        assertThat(object.double_value.doubleValue(), is(45.09843));
        assertThat(object.int_value.intValue(), is(5000));
    }

    @Test
    public void parseElementWithExtraAttribute() throws Exception {
        JSONObject jsonObject = readJsonFile("extra_attribute_resource.json");
        TestModel object = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestModel.class);
        assertNotNull(object);
        assertThat(object, instanceOf(TestModel.class));
        assertThat(object.getId(), is("1"));
        assertThat(object.content, is("contenuto"));
    }

    @Test
    public void parseElementWithMissingAttribute() throws Exception {
        JSONObject jsonObject = readJsonFile("missing_attributes_resource.json");
        TestModel object = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestModel.class);
        assertNotNull(object);
        assertThat(object, instanceOf(TestModel.class));
        assertThat(object.getId(), is("1"));
        assertThat(object.content, is(nullValue()));
    }

    @Test
    public void parsingList() throws Exception {
        JSONObject jsonObject = readJsonFile("resource_array.json");
        List<TestModel> objectList = JsonAPIUtils.parseList(jsonAPIParser, jsonObject, TestModel.class);
        assertNotNull(objectList);
        assertThat(objectList, not(empty()));
        assertThat(objectList, hasSize(2));
        assertThat(objectList, everyItem(IsInstanceOf.<TestModel>instanceOf(TestModel.class)));
    }

    @Test
    public void parsingRelationshipSimple() throws Exception {
        JSONObject jsonObject = readJsonFile("simple_relationship_resource.json");
        TestWithChildModel objWithChild = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestWithChildModel.class);
        assertThat(objWithChild, is(notNullValue()));
        assertThat(objWithChild.getId(), is("a7663e8c-1c7e-4c3f-95d5-df976f07f81a"));
        assertThat(objWithChild.content, is("i've got a child"));
        assertThat(objWithChild.child, is(notNullValue()));
        TestChildModel child = objWithChild.child;
        assertThat(child.getIsFavoChild(), is(true));
    }

    @Test
    public void parsingMultipleRelationship() throws Exception {
        JSONObject jsonObject = readJsonFile("multi_relationship_resource.json");
        TestWithChildrenModel objWithChildren = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestWithChildrenModel.class);
        assertThat(objWithChildren, is(notNullValue()));
        assertThat(objWithChildren.getId(), is("a7663e8c-1c7e-4c3f-95d5-df976f07f81a"));
        assertThat(objWithChildren.content, is("i've got children"));
        List<TestChildModel> children = objWithChildren.children;
        assertThat(children, hasSize(2));
        assertThat(children.get(0).getId(), is("e7cde6f7-c2fe-4e4d-9bdc-40dd9b4b4597"));
        assertThat(children.get(0).getIsFavoChild(), is(true));
        assertThat(children.get(1).getId(), is("d232d2c1-1c47-4888-bb38-5c7e0893dea5"));
        assertThat(children.get(1).getIsFavoChild(), is(false));
    }

    @Test
    public void parseElementWithStringListAttribute() throws Exception {
        JSONObject jsonObject = readJsonFile("list_attribute_with_spaces.json");
        TestModel testModel = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, TestModel.class);
        assertThat(testModel.strings, is(notNullValue()));
        assertThat(testModel.strings, hasSize(3));
        assertThat(testModel.strings, hasItem("one"));
        assertThat(testModel.strings, hasItem("two"));
        assertThat(testModel.strings, hasItem("number three"));
    }

    @Test
    public void parseInheritedModelsInList() throws Exception {
        JSONObject jsonObject = readJsonFile("inheritance_list.json");
        List<TestParent> parents = JsonAPIUtils.parseList(jsonAPIParser, jsonObject, TestParent.class);
        assertMother(parents.get(0), "address1", "mom1");
        assertMother(parents.get(1), "address2", "mom2");
        assertFather(parents.get(2), "address3", "dad3");
        assertMother(parents.get(3), "address4", "mom4");
        assertFather(parents.get(4), "address5", "dad5");
    }

    @Test
    public void parseTernaryDependency() throws Exception {
        JSONObject jsonObject = readJsonFile("ternary_dependency.json");
        SingleRelationship element = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, SingleRelationship.class);
        assertNotNull(element);
        assertThat(element.content, is("father"));
        element = element.child;
        assertNotNull(element);
        assertThat(element.content, is("son"));
        element = element.child;
        assertNotNull(element);
        assertThat(element.content, is("grandson"));
        assertNull(element.child);
    }

    @Test
    public void parseCircularRelationships() throws Exception {
        JSONObject jsonObject = readJsonFile("circular_relationship.json");
        SingleRelationship element = JsonAPIUtils.parseElement(jsonAPIParser, jsonObject, SingleRelationship.class);
        assertNotNull(element);
        assertThat(element.content, is("one"));
        SingleRelationship element2 = element.child;
        assertNotNull(element2);
        assertThat(element2.content, is("other"));
        SingleRelationship oneAgain = element2.child;
        assertThat(element, is(oneAgain));
    }

    private void assertMother(TestParent testParent, String expectedAddress, String expectedMom) {
        assertThat(testParent, is(instanceOf(TestMother.class)));
        TestMother mother = (TestMother) testParent;
        assertThat(mother.address, is(expectedAddress));
        assertThat(mother.mom, is(expectedMom));
    }

    private void assertFather(TestParent testParent, String expectedAddress, String expectedDad) {
        assertThat(testParent, is(instanceOf(TestFather.class)));
        TestFather father = (TestFather) testParent;
        assertThat(father.address, is(expectedAddress));
        assertThat(father.dad, is(expectedDad));
    }

    private JSONObject readJsonFile(String filename) throws Exception {
        return TestUtils.readJsonFile(getClass(), filename);
    }

}
