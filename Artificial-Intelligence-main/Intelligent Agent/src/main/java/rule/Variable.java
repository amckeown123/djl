package rule;

 
import java.util.*;


/**
 * The <code>Variable</code> class defines an abstract class for a variable
 * object.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public abstract class Variable {
  protected String name;
  protected String value;

  // used by categorical only
  protected Vector<String> labels;
  protected int column;


  /**
   * Creates a variable object.
   */
  public Variable() {}
  ;


  /**
   * Creates a variable object with specified name.
   *
   * @param name the String object that contains the variable name
   */
  public Variable(String name) {
    this.name = name;
    value = null;
  }


  /**
   * Retrieves the variable name.
   *
   * @return the String object that contains the variable name
   *
   */
  public String getName() {
    return name;
  }


  /**
   * Sets the value of the variable.
   *
   * @param value the String object that contains the value for the variable
   *
   */
  public void setValue(String value) {
    this.value = value;
  }


  /**
   * Retrieves the value of the variable.
   *
   * @return the String object that contains the value
   *
   */
  public String getValue() {
    return value;
  }


  /**
   * Sets the labels for the variable from the given string.
   *
   * @param newLabels the String object that contains the labels, separated
   *                  by spaces
   *
   */
  public void setLabels(String newLabels) {
    labels = new Vector<String>();
    StringTokenizer tok = new StringTokenizer(newLabels, " ");

    while (tok.hasMoreTokens()) {
      labels.addElement(new String(tok.nextToken()));
    }
  }


  /**
   * Retrieves the label at the specified index.
   *
   * @param index the integer of the index
   *
   * @return the String object that contains the label
   *
   */
  public String getLabel(int index) {
    return (String) labels.elementAt(index);
  }


  /**
   * Retrieves all the labels for this variable.
   *
   * @return the Vector object that contains the labels
   *
   */
  @SuppressWarnings("unchecked")
public Vector<String> getLabels() {
    return (Vector<String>) labels.clone();
  }


  /**
   * Retrieves a string containing all the labels separated by spaces.
   *
   * @return the String object that contains the labels
   */
  public String getLabelsAsString() {
    String labelList = new String();
    Enumeration<String> enum1 = labels.elements();

    while (enum1.hasMoreElements()) {
      labelList += enum1.nextElement() + " ";
    }
    return labelList;
  }


  /**
   * Retrieves the index of the given label.
   *
   * @param label the String object that contains the label whose index is returned
   *
   * @return the integer value of index or -1 if not found
   */
  public int getIndex(String label) {
    int index = -1;

    if (labels == null) {
      return index;
    }
    for (int i = 0; i < labels.size(); i++) {
      if (label.equals((String) labels.elementAt(i))) {
        index = i;
        break;
      }
    }
    return index;
  }


  /**
   * Checks if the variable is a categorical variable.
   *
   * @return the boolean true if the variable is categorical (has labels)
   *         or false if it is not categorical
   *
   */
  public boolean categorical() {
    if (labels != null) {
      return true;
    } else {
      return false;
    }
  }


  /**
   * Retrieves the name of the variable.
   *
   * @return the String object that contains the name
   *
   */
  public String toString() {
    return name;
  }


  /**
   * Sets the column (in a DataSet).
   *
   * @param column the integer that represents the column
   *
   */
  public void setColumn(int column) {
    this.column = column;
  }


  /**
   * Computes statistics for the variable.
   *
   * @param inValue the String object used as input to the computation
   *
   */
  public abstract void computeStatistics(String inValue);


  /**
   * Normalizes the variable.
   *
   * @param inValue the String object containing the input value
   * @param outArray the float[] object that contains the normalized value
   * @param inx the integer that represents the index
   *
   * @return the integer return value
   *
   */
  public abstract int normalize(String inValue, float[] outArray, int inx);


  /**
   * Retrieves the normalized size of the variable.
   *
   * @return the integer value (the normalized size)
   *
   */
  public int normalizedSize() {
    return 1;  // return default value of 1
  }


  /**
   * Retrieves the decoded value at the given index
   *
   * @param act the float[] object that contains the values to be decoded
   * @param index the integer index value
   *
   * @return the String object that contains the decoded value
   *
   */
  public String getDecodedValue(float[] act, int index) {
    return String.valueOf(act[index]);
  }
}
