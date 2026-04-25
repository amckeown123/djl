package learn;

import java.util.*;
import java.io.*;


/**
 * The <code>Variable</code> abstract class provides the common support
 * necessary for continuous, numeric discrete, and categorical variables.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P.Bigus and Jennifer Bigus 1997, 2001
 *
 */
public abstract class Variable implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected String name;
  protected String value;
  protected int column;

  // used by categorical only
  protected Vector<String> labels;


  /**
   * Creates a <code>Variable</code>.
   */
  public Variable() {
    name = null;
    value = null;
    labels = null;
  }


  /**
   * Creates a <code>Variable</code> with the given name.
   *
   * @param name the String that contains the name of the variable
   */
  public Variable(String name) {
    this.name = name;
    value = null;
    labels = null;
  }


  /**
   * Retrieves the name of the variable.
   *
   * @return the name of the variable
   */
  public String getName() {
    return name;
  }


  /**
   * Sets the value of the variable.
   *
   * @param val the String value of the variable
   */
  public void setValue(String val) {
    value = val;
  }


  /**
   * Retrieves the value of the variable.
   *
   * @return the value of the variable
   */
  public String getValue() {
    return value;
  }


  /**
   * Sets the labels using the given label string.
   *
   * @param labels the String that contains the labels for a categorical variable
   *
   * @param Labels the String
   */
  public void setLabels(String Labels) {
    labels = new Vector<String>();
    StringTokenizer tok = new StringTokenizer(Labels, " ");

    while (tok.hasMoreTokens()) {
      labels.addElement(new String(tok.nextToken()));
    }
  }


  /**
   * Retrieves the label with the specified index.
   *
   * @param index the integer index of the desired label
   *
   * @return the label at the given index
   */
  public String getLabel(int index) {
    return (String) labels.elementAt(index);
  }


  /**
   * Retrieves all the labels from a categorical variable.
   *
   * @return a string that contains all the labels, separated by spaces
   */
  public String getLabels() {
    String labelList = new String();
    Enumeration<String> enum1 = labels.elements();

    while (enum1.hasMoreElements()) {
      labelList += enum1.nextElement() + " ";
    }
    return labelList;
  }


  /**
   * Retrieves the index for the given label.
   *
   * @param label the String for which the index is retrieved
   *
   * @return the index of the given label, -1 if label was not found
   */
  public int getIndex(String label) {
    int i = 0, index = -1;
    Enumeration<String> enum1 = labels.elements();

    while (enum1.hasMoreElements()) {
      if (label.equals(enum1.nextElement())) {
        index = i;
        break;
      }
      i++;
    }
    return index;
  }


  /**
   * Determines if the variable is categorical.
   *
   * return <code>true if the variable is categorical. Otherwise, returns <code>
   *        false</code>
   *
   * @return the boolean
   */
  public boolean isCategorical() {
    return (labels != null);
  }


  /**
   * Sets the column.
   *
   * @param col the integer value of the column
   */
  public void setColumn(int col) {
    column = col;
  }


  /**
   * Computes the minimum and maximum values for this variable based on the given
   * string, but can also be used to compute other statistics as well.
   *
   * @param inValue the String on which the statistics are based
   */
  public abstract void computeStatistics(String inValue);


  /**
   * Converts the given symbol for use in the network.
   *
   * @param inValue   the String to be converted
   * @param outArray  the double array of  converted values
   * @param inx       the integer index that indicates where the converted output
   *                  is to be stored in the array
   *
   * @return the index of the next element in the array
   */
  public abstract int normalize(String inValue, double[] outArray, int inx);


  /**
   * Retrieves the normalized size of this variable.
   *
   * @return the normalized size
   */
  public int getNormalizedSize() {
    return 1;
  }

  /*
   * Retrieves the activation value in a format that can be displayed.
   *
   * @param act the double array of activation values
   * @param index the index of the activation to be displayed
   *
   * @return the value in a format that can be displayed
   */


  /**
   * Method getDecodedValue
   *
   * @param act the double[]
   * @param index the int
   *
   * @return the String
   *
   */
  public String getDecodedValue(double[] act, int index) {
    return String.valueOf(act[index]);
  }
}
