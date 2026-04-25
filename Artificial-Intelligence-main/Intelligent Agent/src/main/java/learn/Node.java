package learn;

import java.util.*;
import java.io.*;



/**
 * The <code>Node</code> class contains the label or name and the links for
 * a node in a  <code>DecisionTree</code>.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class Node extends Object implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String label;       // name of the node
  protected Vector<String> linkLabels;  // tests on links from parent to child
  protected Node parent;        // parent node
  protected Vector<Node> children;    // any children nodes


  /**
   * Creates a node.
   */
  public Node() {
    parent = null;
    children = new Vector<Node>();
    linkLabels = new Vector<String>();
  }


  /**
   * Creates a node with the given name.
   *
   * @param label the String that contains the name of the node
   */
  public Node(String label) {
    this.label = label;
    children = new Vector<Node>();
    parent = null;
    linkLabels = new Vector<String>();
  }


  /**
   * Creates a node with the given name and parent.
   *
   * @param parent the Node that is the parent of the node being created
   * @param label the String that contains the name of the node
   */
  public Node(Node parent, String label) {
    this.parent = parent;
    children = new Vector<Node>();
    this.label = label;
    linkLabels = new Vector<String>();
  }


  /**
   * Adds a child node and the link name for the link to that child.
   *
   * @param child the Node that is added as a child
   * @param linkLabel the String that contains the name of the link
   */
  public void addChild(Node child, String linkLabel) {
    children.addElement(child);
    linkLabels.addElement(linkLabel);
  }


  /**
   * Checks if the node has children nodes linked to it.
   *
   * @return  <code>true</code> if the node has children. Otherwise, returns <code>
   *          false</code>.
   */
  public boolean hasChildren() {
    if (children.size() == 0) {
      return false;
    } else {
      return true;
    }
  }


  /**
   * Sets the name of the node.
   *
   * @param label the String that contains the name of the node
   */
  public void setLabel(String label) {
    this.label = label;
  }


  /**
   * Displays the tree, starting with the given root node.
   *
   * @param root the Node that is the root of the tree to be displayed
   * @param offset the String
   */
  public static void displayTree(Node root, String offset) {
    if (root.children.size() == 0) {
      DecisionTree.appendText("\n" + offset + "    THEN (" + root.label + ")  (Leaf node)");
      return;
    } else {
      Enumeration<?> enum1 = root.children.elements();
      Enumeration<?> enum2 = root.linkLabels.elements();

      DecisionTree.appendText("\n" + offset + "   " + root.label + " (Interior node)");
      while (enum1.hasMoreElements()) {
        DecisionTree.appendText("\n" + offset + "   IF (" + (String) enum2.nextElement() + ")");
        displayTree((Node) enum1.nextElement(), offset + "   ");
      }
    }
  }
}
