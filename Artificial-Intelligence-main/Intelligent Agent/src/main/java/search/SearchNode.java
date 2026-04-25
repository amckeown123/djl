package search;

import java.util.*;
import javax.swing.*;


/**
 * The <code>SearchNode</code> class contains the label or name and the
 * state of a node in a  <code>SearchGraph</code>.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 *
 */
public class SearchNode extends Object {
  protected String label;                  // symbolic name
  protected Object state;                  // defines the state-space
  protected Object oper;                   // operator used to generate this node
  protected Vector<SearchNode> links;                  // edges or links to other nodes
  protected int depth;                     // depth in a tree from start node
  protected boolean expanded;              // indicates if node has been expanded
  protected boolean tested;                // indicates if node was ever tested
  protected float cost = 0;                // cost to get to this node
  private static JTextArea traceTextArea;  // used for trace only
  public static final int FRONT = 0;
  public static final int BACK = 1;
  public static final int INSERT = 2;


  /**
   * Creates a <code>SearchNode</code> with a given name and state.
   *
   * @param label the String that represents the node name
   * @param state the Object the represents the state of the node
   */
  SearchNode(String label, Object state) {
    this.label = label;
    this.state = state;
    depth = 0;
    links = new Vector<SearchNode>();
    oper = null;
    expanded = false;
    tested = false;
  }


  /**
   * Adds a link to another <code>SearchNode</code> in the graph.
   *
   * @param node the SearchNode to be linked to
   */
  public void addLink(SearchNode node) {
    links.addElement(node);
  }


  /**
   * Adds links to a set of <code>SearchNodes</code> in the graph.
   *
   * @param node the SearchNode array that contains the nodes to be linked
   * @param nodes the SearchNode[] object that contains
   */
  public void addLinks(SearchNode[] nodes) {
    for (int i = 0; i < nodes.length; i++) {
      links.addElement(nodes[i]);
    }
  }


  /**
   * Determines if the <code>SearchNode</code> is a leaf node in the graph.
   *
   * @return  <code>true</code> if the node is a leaf. Otherwise,
   *          returns <code>false</code>.
   */
  public boolean leaf() {
    return (links.size() == 0);
  }


  /**
   * Sets the depth of this <code>SearchNode</code> in the graph.
   *
   * @param depth the depth of the node in the graph
   */
  public void setDepth(int depth) {
    this.depth = depth;
  }


  /**
   * Sets the operator of the <code>SearchNode</code>.
   *
   * @param oper  the Object that contains the definition of the operation
   *              that created the state of the node
   */
  public void setOperator(Object oper) {
    this.oper = oper;
  }


  /**
   * Sets the <code>SearchNode</code> flag that indicates the node has
   * been expanded.
   */
  public void setExpanded() {
    expanded = true;
  }


  /**
   * Sets the <code>SearchNode</code> flag that indicates whether the node
   * has been expanded.
   *
   * @param state the boolean that indicates whether the node has been
   *              expanded or not
   */
  public void setExpanded(boolean state) {
    expanded = state;
  }


  /**
   * Indicates whether the node has been expanded or not.
   *
   * @return  <code>true</code> if the node has been expanded. Otherwise,
   *          returns <code>false</false>.
   */
  public boolean isExpanded() {
    return expanded;
  }


  /**
   * Sets the <code>SearchNode</code> flag that indicates that the node
   * has been tested.
   */
  public void setTested() {
    tested = true;
  }


  /**
   * Sets the <code>SearchNode</code> flag that indicates whether the node
   * has been tested.
   *
   * @param state the boolean that indicates whether the node has been
   *              tested or not
   */
  public void setTested(boolean state) {
    tested = state;
  }


  /**
   * Indicates whether the node has been tested or not.
   *
   * @return  <code>true</code> if the node has been tested. Otherwise,
   *          returns <code>false</false>.
   */
  public boolean isTested() {
    return tested;
  }


  /**
   * Sets the trace text area.
   *
   * @param  the JTextArea to be displayed in the trace text area
   * @param textArea the JTextArea object that contains
   */
  static public void setDisplay(JTextArea textArea) {
    traceTextArea = textArea;
  }


  /**
   * Gets the <code>Object</code> that represents the state of the node.
   *
   * @return the <code>Object</code> the represents state of the node
   */
  public Object getState() {
    return state;
  }


  /**
   * Initializes the node for another search.
   */
  public void reset() {
    depth = 0;
    expanded = false;
    tested = false;
  }


  /**
   * Writes a trace statement, using indentation to indicate depth within
   * the graph.
   */
  public void trace() {
    String indent = new String();

    for (int i = 0; i < depth; i++) {
      indent += "  ";
    }
    traceTextArea.append(indent + "Searching " + depth + ": " + label + " with state = " + state + "\n");
  }


  /**
   * Expands the node and add to a queue at specified position.
   *
   * @param queue     the Vector to which the node is added
   * @param position  where in the queue the node should be added;
   *                   0=front, 1=back, 2=base position on node cost
   */
  public void expand(Vector<SearchNode> queue, int position) {
    setExpanded();
    for (int j = 0; j < links.size(); j++) {
      SearchNode nextNode = (SearchNode) links.elementAt(j);

      if (!nextNode.tested) {
        nextNode.setTested(true);
        nextNode.setDepth(depth + 1);
        switch (position) {
          case FRONT :
            queue.insertElementAt(nextNode, 0);
            break;
          case BACK :
            queue.addElement(nextNode);
            break;
          case INSERT :
            boolean inserted = false;
            float nextCost = nextNode.cost;

            for (int k = 0; k < queue.size(); k++) {

              // find where to insert this node
              if (nextCost < ((SearchNode) queue.elementAt(k)).cost) {
                queue.insertElementAt(nextNode, k);  // insert
                inserted = true;
                break;                               // exit the for loop
              }
            }

            // couldn't find place to insert, just add to end
            if (!inserted) {
              queue.addElement(nextNode);
            }
            break;
        }
      }
    }
  }
}
