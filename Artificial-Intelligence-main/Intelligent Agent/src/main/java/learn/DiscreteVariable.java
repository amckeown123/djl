package learn;

import java.util.*;
import java.io.*;


/**
 * The <code>DiscreteVariable</code> class provides the support necessary
 * for variables that can take on a predefined set of numeric or symbolic
 * values.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
class DiscreteVariable extends Variable implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
protected int min;
  protected int max;


  /**
   * Creates a <code>DiscreteVariable</code> with the given name.
   *
   * @param name the String name given to the variable
   */
  public DiscreteVariable(String name) {
    super(name);
    labels = new Vector<String>();
  }


  /**
   * Set the minimum value for the variable.
   *
   * @param min the int minimum value for the variable
   */
  public void setMin(int min) {
    this.min = min;
  }


  /**
   * Set the maximum value for the variable.
   *
   * @param max the int maximum value for the variable
   */
  public void setMax(int max) {
    this.max = max;
  }


  /**
   * Used within a <code>DataSet</code> to compute the minimum and maximum value
   * for the variable.
   *
   * @param inValue the String that contains the value used to determine minimum
   *                or maximum value for the variable.
   */
  public void computeStatistics(String inValue) {
    if (labels.contains(inValue)) {
      return;
    } else {
      labels.addElement(inValue);
    }
  }


  /**
   * Converts a symbol to a one-of-N code.
   *
   * @param inValue   the String symbol to be converted
   * @param outArray  the double array where the one-of-N code one-of-N code
   *                  will be stored
   * @param inx       the int starting index where the one-of-N code should be
   *                  stored the output array
   *
   * @return the index of the next available position in the output array
   */
  public int normalize(String inValue, double[] outArray, int inx) {
    int index = getIndex(inValue);  // look up symbol index
    double code[] = new double[labels.size()];

    if (index < code.length) {
      code[index] = 1.0;
    }

    // copy one of N code to outArray, increment inx
    for (int i = 0; i < code.length; i++) {
      outArray[inx++] = code[i];
    }
    return inx;  // return output index
  }


  /**
   * Retrieves the number of discrete values the varible can take.
   *
   * @return the size of the one-of-N code when the variable is normalized
   */
  public int getNormalizedSize() {
    return labels.size();
  }


  /**
   * Retrieves the value of the given activation in a format that can be
   * displayed.
   *
   * @param act   the double array that contains the activation
   * @param start the int starting index for the activation within the array
   *
   * @return the value of the activation in a format that can be displayed
   */
  public String getDecodedValue(double[] act, int start) {
    int len = labels.size();
    String value;
    double max = -1.0;

    value = String.valueOf(0);
    for (int i = 0; i < len; i++) {
      if (act[start + i] > max) {
        max = act[start + i];
        value = getLabel(i);
      }
    }
    return value;
  }
}
