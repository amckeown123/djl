package search;

import java.util.*;


/**
 * The <code>SearchGraph</code> class is a container for a set of <code>
 * SearchNodes</code>. A <code>SearchGraph</code> is used by the five
 * basic search algorithms: depth-first, breadth-first, iterated,
 * best-first, and genetic.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
class SearchGraph extends Object {
  String name;
  Hashtable<String, SearchNode> graph = new Hashtable<String, SearchNode>();


  /**
   * Creates a <code>SearchGraph</code> with the given name.
   *
   * @param name  the String that identifies the name of the graph
   */
  public SearchGraph(String name) {
    this.name = name;
  }


  /**
   * Returns the search graph.
   *
   * @return the search graph
   */
  public Hashtable<String, SearchNode> getGraph() {
    return graph;
  }


  /**
   * Returns the search node associated with the given node name.
   *
   * @param nodeName  the String that names a node in the graph
   * @return          the search node associated with the given node name
   */
  public SearchNode getNode(String nodeName) {
    return (SearchNode) graph.get(nodeName);
  }


  /**
   * Resets each search node in the graph.
   */
  void reset() {
    Enumeration<SearchNode> enum1 = graph.elements();

    while (enum1.hasMoreElements()) {
      SearchNode nextNode = (SearchNode) enum1.nextElement();

      nextNode.reset();
    }
  }


  /**
   * Adds a node to the graph, using node label as key.
   *
   * @param node  the SearchNode to be added to the graph
   */
  void put(SearchNode node) {
    graph.put(node.label, node);
  }


  /**
   * Does a depth-first search on graph, starting the the given initial
   * node and continuing until the goal state is reached.
   *
   * @param initialNode the SearchNode at which the search begins
   * @param goalState   the Object being searched for in the graph
   *
   * @return  the <code>SearchNode</code> containing the goal is returned,
   *          if the goal state is found. Otherwise, <code>null</code>
   *          is returned.
   */
  public SearchNode depthFirstSearch(SearchNode initialNode, Object goalState) {
    Vector<SearchNode> queue = new Vector<SearchNode>();

    queue.addElement(initialNode);
    initialNode.setTested(true);  // test each node once
    while (queue.size() > 0) {
      SearchNode testNode = (SearchNode) queue.firstElement();

      queue.removeElementAt(0);
      testNode.trace();   // display trace information
      if (testNode.getState().equals(goalState)) {
        return testNode;  // found it
      }
      if (!testNode.isExpanded()) {
        testNode.expand(queue, SearchNode.FRONT);
      }
    }
    return null;
  }

  /*
   * Does a breadth-first search on graph, starting the the given initial
   * node and continuing until the goal state is reached.
   *
   * @param initialNode   the SearchNode at which the search begins
   * @param goalState     the Object being searched for in the graph
   *
   * @return  the <code>SearchNode</code> containing the goal is returned,
   *          if the goal state is found. Otherwise, <code>null</code>
   *          is returned.
   */


  /**
   * Method breadthFirstSearch
   *
   * @param initialNode the SearchNode object
   * @param goalState the Object object
   *
   * @return the SearchNode object
   *
   */
  public SearchNode breadthFirstSearch(SearchNode initialNode, Object goalState) {
    Vector<SearchNode> queue = new Vector<SearchNode>();

    queue.addElement(initialNode);
    initialNode.setTested(true);  // test each node once
    while (queue.size() > 0) {
      SearchNode testNode = (SearchNode) queue.firstElement();

      queue.removeElementAt(0);
      testNode.trace();
      if (testNode.getState().equals(goalState)) {
        return testNode;  // found it
      }
      if (!testNode.isExpanded()) {
        testNode.expand(queue, SearchNode.BACK);
      }
    }
    return null;
  }


  /**
   * Does a depth-first search on graph, starting the the given initial
   * node and continuing until the goal state is reached or the given
   * depth is reached.
   *
   * @param initialNode the SearchNode at which the search begins
   * @param goalState   the Object being searched for in the graph
   * @param maxDepth    the maximum depth to be searched
   *
   * @return  the <code>SearchNode</code> containing the goal is returned,
   *          if the goal state is found. Otherwise, <code>null</code>
   *          is returned.
   */
  public SearchNode depthLimitedSearch(SearchNode initialNode, Object goalState, int maxDepth) {
    Vector<SearchNode> queue = new Vector<SearchNode>();

    queue.addElement(initialNode);
    initialNode.setTested(true);  // only test each node once
    while (queue.size() > 0) {
      SearchNode testNode = (SearchNode) queue.firstElement();

      queue.removeElementAt(0);
      testNode.trace();
      if (testNode.getState().equals(goalState)) {
        return testNode;
      }

      // limit the depth of search to maxDepth
      if (testNode.depth < maxDepth) {
        if (!testNode.isExpanded()) {
          testNode.expand(queue, SearchNode.FRONT);
        }
      }
    }
    return null;
  }


  /**
   * Does a iterated deepening search on graph, a series of depth-first
   * searches starting at the given initial node and continuing until
   * the goal state is reached or an arbitrary depth is reached.
   *
   * @param initialNode the SearchNode at which the search begins
   *
   * @param startNode the SearchNode object
   * @param goalState   the Object being searched for in the graph
   *
   * @return  the <code>SearchNode</code> containing the goal is returned,
   *          if the goal state is found. Otherwise, <code>null</code>
   *          is returned.
   */
  public SearchNode iterDeepSearch(SearchNode startNode, Object goalState) {
    int maxDepth = 10;  // arbitrary limit

    for (int j = 0; j < maxDepth; j++) {
      reset();
      SearchNode answer = depthLimitedSearch(startNode, goalState, j);

      if (answer != null) {
        return answer;
      }
    }
    return null;  // failed to find solution in maxDepth
  }


  /**
   * Does a best-first search on graph, starting the the given initial node
   * and continuing until the goal state is reached. The "best" solution is
   * determined by the cost associated with each <code>SearchNode</code>.
   *
   * @param initialNode the SearchNode at which the search begins
   * @param goalState   the Object being searched for in the graph
   *
   * @return  the <code>SearchNode</code> containing the goal is returned,
   *          if the goal state is found. Otherwise, <code>null</code>
   *          is returned.
   */
  public SearchNode bestFirstSearch(SearchNode initialNode, Object goalState) {
    Vector<SearchNode> queue = new Vector<SearchNode>();

    queue.addElement(initialNode);
    initialNode.setTested(true);  // only test each node once
    while (queue.size() > 0) {
      SearchNode testNode = (SearchNode) queue.firstElement();

      queue.removeElementAt(0);
      testNode.trace();
      if (testNode.getState().equals(goalState)) {
        return testNode;
      }

      // now, heuristically add nodes to queue
      // insert the child nodes according to cost
      if (!testNode.isExpanded()) {
        testNode.expand(queue, SearchNode.INSERT);
      }
    }
    return null;
  }
}
