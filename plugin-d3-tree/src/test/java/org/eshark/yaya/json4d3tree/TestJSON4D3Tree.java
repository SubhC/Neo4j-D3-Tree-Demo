package org.eshark.yaya.json4d3tree;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.harness.junit.Neo4jRule;
import org.neo4j.test.server.HTTP;

public class TestJSON4D3Tree {
    @Rule
    public Neo4jRule neo4j = new Neo4jRule()
            .withExtension("/v1.1", JSON4D3Tree.class)
            .withFixture(TEST_DATA);

    @Test
    public void sayHelloTest() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1.1/JSON4Tree/hello").toString());
        Map<String, String> expected = new HashMap<String, String>();
        System.out.println("Test it is working....");
        System.out.println("Response: " + response.rawContent());
        System.out.println("It actually worked...");
        
        expected.put("hello", "world");
        
        Map<String, String> results = response.content();
        assertTrue(results.equals(expected));
    }

    @Test
    public void findNodeByName() {
        String name ="Entry1";
    	HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1.1/JSON4Tree/findNode/"+name).toString());

        System.out.println("Test it is working....");
        System.out.println("Response: " + response.rawContent());
        System.out.println("It actually worked...");
        
        Map<String, String> expected = new HashMap<String, String>();
        expected.put("name", name);
        Map<String, String> results = response.content();
        assertTrue(results.equals(expected));
    }
    
    @Test
    public void getTreeJSON() {
    	 String name ="Entry1";
     	HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1.1/JSON4Tree/getTree/"+name).toString());
        System.out.println("Test it is working....");
        System.out.println("Response: " + response.rawContent());
        System.out.println("It actually worked...");


        //assertTrue(actual.equals(expected));
    }

    /*
    @Test
    public void shouldRespondToPreCachedStreamingPaths() {
        HTTP.Response response = HTTP.GET(neo4j.httpURI().resolve("/v1/service/paths_streaming_pre_cached/user1").toString());
        ArrayList actual = response.content();
        assertTrue(actual.equals(expected));
    }

    private static final ArrayList<HashMap<String,Object>> expected = new ArrayList<HashMap<String, Object>>() {{
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
            }});
            put("length", 0);
        }});
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
                add("user2");
            }});
            put("length", 1);
        }});
        add(new HashMap<String,Object>(){{
            put("paths", new ArrayList<String>(){{
                add("user1");
                add("user2");
                add("user3");
            }});
            put("length", 2);
        }});

    }};
*/
    public static final String TEST_DATA =
            new StringBuilder()
            .append("create (s1:Switch{  name:  'Entry1'})") 
            .append("create (s2:Switch{  name:  'Entry2'})") 
            .append("create (s3:Switch{  name:  'Entry3'})") 
            .append("create (s4:Switch{  name:  'Entry4'})") 
            .append("create (s5:Switch{  name:  'Entry5'})") 
            .append("create (s6:Switch{  name:  'Entry6'})") 
            .append("create (s7:Switch{  name:  'Entry7'})") 
            .append("MERGE (s1)-[:CONNECTED_TO]->(s2)")
            .append("MERGE (s1)-[:CONNECTED_TO]->(s3)")
            .append("MERGE (s3)-[:CONNECTED_TO]->(s4)")
            .append("MERGE (s2)-[:CONNECTED_TO]->(s5)")
            .append("MERGE (s5)-[:CONNECTED_TO]->(s6)")
            .append("MERGE (s5)-[:CONNECTED_TO]->(s7)")
                    .toString();
}