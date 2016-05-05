package org.eshark.yaya.json4d3tree;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	String name;
	List<TreeNode> children  = new ArrayList<TreeNode>();
		
	public TreeNode(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	
	public void setChildren(TreeNode child) {
		this.children.add(child);
	}
}
