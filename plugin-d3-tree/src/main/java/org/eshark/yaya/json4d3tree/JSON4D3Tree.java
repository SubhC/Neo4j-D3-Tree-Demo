package org.eshark.yaya.json4d3tree;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.TraversalDescription;

@javax.ws.rs.Path("/JSON4Tree")
public class JSON4D3Tree {
	private static GraphDatabaseService db;
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public JSON4D3Tree(@Context GraphDatabaseService graphDatabaseService) {
		db = graphDatabaseService;
	}

	@GET
	@javax.ws.rs.Path("/hello")
	@Produces({ "application/json" })
	public Response helloWorld() throws IOException {
		Map<String, String> results = new HashMap<String, String>();
		results.put("hello", "world");

		return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
	}

	@GET
	@javax.ws.rs.Path("/findNode/{name}/")
	@Produces({ "application/json" })
	public Response findNode(@PathParam("name") final String name, @Context final GraphDatabaseService db)
			throws IOException {
		System.out.println(" Attribute Name To Search: " + name);
		String attrubute = "name";
		Map<String, Object> result;

		try (Transaction tx = db.beginTx()) {
			final Node swtch = db.findNode(Labels.Switch, attrubute, name);
			if (swtch != null)
				result = swtch.getAllProperties();
			else
				result = new HashMap<String, Object>();
			tx.success();
		}

		return Response.ok().entity(objectMapper.writeValueAsString(result)).build();
	}

	@GET
	@javax.ws.rs.Path("/getTree/{name}/")
	@Produces({ "application/json" })
	public Response getTree(@PathParam("name") final String name, @Context final GraphDatabaseService db)
			throws IOException {
		
		System.out.println(" Attribute Name To SearchByName: " + name);
		
		HashMap<Long, TreeNode> treeNodeMap = new HashMap<Long, TreeNode>();
		
		TreeNode rootNode = null;
		
		String attrubute = "name";

		try (Transaction tx = db.beginTx()) {
			final Node swtch = db.findNode(Labels.Switch, attrubute, name);
			if (swtch != null) {
				TraversalDescription td = db.traversalDescription().depthFirst()
						.expand(PathExpanders.forTypeAndDirection(RelationshipTypes.CONNECTED_TO, Direction.OUTGOING));
				
				for (Path directoryPath : td.traverse(swtch)) 
				{
					Iterable<Relationship> connectedTos = directoryPath.endNode().getRelationships(Direction.OUTGOING,RelationshipTypes.CONNECTED_TO);
					
					if (connectedTos != null) 
					{
						for(Relationship connectedTo : connectedTos)
						{
							//For the Current Relationship
							//get the start node as parent
							Node parentNode = connectedTo.getStartNode();
							Long parentNodeId = parentNode.getId();
							TreeNode parentTreeNode = treeNodeMap.get(parentNodeId);
							
							if(parentTreeNode == null)
							{
								////Populate the Parent Details
								parentTreeNode = new TreeNode(parentNode.getProperty("name", "NoName").toString());
								if(rootNode == null)
									rootNode = parentTreeNode;
								//Add to the linear HashMap for subsequent searches
								treeNodeMap.put(parentNodeId, parentTreeNode);
							}
								
							//For the Current Relationship get the end node Children
							Node childNode = connectedTo.getEndNode();
							Long childNodeId = childNode.getId();
							TreeNode childTreeNode = treeNodeMap.get(childNodeId);
							
							if(childTreeNode == null)
							{
								childTreeNode = new TreeNode(childNode.getProperty("name", "NoName").toString());
								treeNodeMap.put(childNodeId, childTreeNode);
								parentTreeNode.setChildren(childTreeNode);
							}
						}
				}
			}
			tx.success();
		}
		}
		/*
		 System.out.println("JSON: " + objectMapper.writeValueAsString(rootNode));
		 System.out.println("LinearHashMap: " + objectMapper.writeValueAsString(treeNodeMap ));
		 */
		
		return Response.ok().entity(objectMapper.writeValueAsString(rootNode)).build();
	
	}
}
