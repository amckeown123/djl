package rule;


import java.util.*;


/**
 * The <code>ContinuousFuzzyRuleVariable</code> class implements a variable that
 * can hold a continuous value.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class ContinuousFuzzyRuleVariable extends FuzzyRuleVariable  {
  double discourseLo;         // Universe of discourse, low end
  double discourseHi;         // Universe of discourse, high end
  Hashtable<String, FuzzySet> setList;          // Fuzzy sets defined over this variable
  double valCrisp;            // Crisp value of fuzzy work set (if known)
  WorkingFuzzySet valFzy;     // Fuzzy Solution Variable
  boolean valKnown;           // Is crisp value known (T) or undefined?
  WorkingFuzzySet valFzyTmp;  // Fuzzy working space to hold temp copies


  /**
   * Creates a new variable with the specified initial values.
   *
   * @param base        the FuzzyRuleBase for this variable
   * @param id          the integer identifier for this variable
   * @param name        the String that contains the name of this variable
   * @param discourseLo the double that contains the low end of the universe of
   *                    discourse
   * @param discourseHi the double that contians the high end of the universe of
   *                    discourse
   */
  ContinuousFuzzyRuleVariable(FuzzyRuleBase base, String name, double discourseLo, double discourseHi) {
    super(FuzzyDefs.ContinuousVariable, base, name);
    this.discourseLo = discourseLo;
    this.discourseHi = discourseHi;
    setList = new Hashtable<String, FuzzySet>();
    valKnown = false;
    valCrisp = 0.0;
    valFzy = new WorkingFuzzySet(this, name + " Fuzzy Solution Space", rb.getAlphaCut(), discourseLo, discourseHi);
    valFzyTmp = new WorkingFuzzySet(this, name + " Fuzzy Work Space", rb.getAlphaCut(), discourseLo, discourseHi);
  }


  /**
   * Retrieves the value of this object as a fuzzy value.
   *
   * @return a <code>FuzzySet</code>, if the value of this object can be
   *         represented as a fuzzy value
   */
  public FuzzySet getFuzzyValue() {
    return valFzy;
  }


  /**
   * Retrieves the value of this object as a numeric value.
   *
   * @return a <code>double</code>, if the value of this object can be represented
   *         as a numeric value
   */
  public double getNumericValue() {
    return getRawValue();
  }


  /**
   * Retrieves the value of this object as a symbolic value.
   *
   * @return a <code>String</code>, if the value of this object can be represented
   *         as a symbolic value
   */
  public String getSymbolicValue() {
    return Double.toString(getRawValue());
  }

  /**
   * Sets the value of this object to the given fuzzy value.
   *
   * @param newValue  the FuzzySet value to which this variable is set
   */
  public void setFuzzyValue(FuzzySet newValue) {
    setRawValue(newValue);
  }


  /**
   * Sets the value of this object to the given numeric value.
   *
   * @param newValue  the double value to which this variable is set
   */
  public void setNumericValue(double newValue) {
    setRawValue(newValue);
  }


  /**
   * Sets the value of this object to the given symbolic value.
   *
   * @param newValue  the String value to which this variable is set
   */
  public void setSymbolicValue(String newValue) {
    setNumericValue(Double.valueOf(newValue).doubleValue());
  }


  /**
   * Sets the value of this object to the given fuzzy literal value.
   *
   * @param newValue  the FuzzyLiteral value to which this variable is set
   */
  public void setValue(FuzzySet newValue) {
         setFuzzyValue(newValue);
  }


  /**
   * Sets the crisp value of this variable from the given String.
   *
   * @param newValue  the String value from which the crisp value for this
   *                  variable is set
   */
  void setValueString(String newValue) {
    Double value;

    try {
      value = Double.valueOf(newValue);
      setRawValue(value.doubleValue());
    } catch (NumberFormatException e) {
      System.out.println("Error: " + name + " " + newValue);
    }
  }


  /**
   * Retrieves this variable's crisp value as a String.
   *
   * @return the String that represents this variable's crisp value
   */
  String getValueString() {
    return Double.toString(valCrisp);
  }


  /**
   * Resets the variable to its intial state.
   */
  void reset() {
    valKnown = false;
    valCrisp = 0.0;
    valFzy.reset();
    valFzyTmp.reset();
  }


  /**
   * Retrieves the low end value of the universe of discourse.
   *
   * @return the low end value of the universe of discourse
   */
  public double getDiscourseLo() {
    return discourseLo;
  }


  /**
   * Retrieves the high end value of the universe of discourse.
   *
   * @return the high end value of the universe of discourse
   */
  public double getDiscourseHi() {
    return discourseHi;
  }


  /**
   * Checks if the given value is within the universe of discourse.
   *
   * @param value the double value to be checked
   *
   * @return <code>true</code> if the value is within the universe. Otherwise
   *          returns <code>false</code>.
   */
  boolean withinUniverseOfDiscourse(double value) {
       if ((discourseLo <= value) && (value <= discourseHi)) {
            return true;
        } else {
            return false;
        }
  }


  /**
   * Sets the crisp value of this object to the given numeric value.
   *
   * @param newValue  the double value to which this variable is set
   */
  void setRawValue(double newValue) {
    if (withinUniverseOfDiscourse(newValue)) {
      valCrisp = newValue;
    } else {
      System.out.println("Error: Value is out of range " + name);
    }
  }


  /**
   * Retrieves the variable's crisp value.
   *
   * @return the double which is this variable's crisp value
   */
  double getRawValue() {
    return valCrisp;
  }


  /**
   * Sets the raw value for this variable using the given <code>FuzzySet</code>.
   *
   * @param newSet the FuzzySet object that contains the new values
   *
   */
  void setRawValue(FuzzySet newSet) {

    //This is an assertion following the "Minimum Law of Fuzzy Assertions".
    //Change the Fuzzy Work Space 'valFzy' with the new set.
     valFzy.copyOrAssertFzy(newSet);

     //When all done, defuzzify the work space and store in valCrisp
    valCrisp = valFzy.defuzzify(rb.getDefuzzifyMethod());
  }


  /**
   * Sets the truth value in the new fuzzy set.
   *
   * @param newSet the FuzzySet object that contains the new set
   *               correlated to the truth value
   * @param truthValue the double object that contains the truth value
   *                   used for correlation
   *
   */
  void setFuzzyValue(FuzzySet newSet, double truthValue) {

    //This is correlation with a truth value.
    //Change the Fuzzy Work Space 'valFzy' with the new set
    //correlated to truth value 'truthValue'
    valFzyTmp.correlateWith(newSet, rb.getCorrelationMethod(), truthValue);
    valFzy.implicateTo(valFzyTmp, rb.getInferenceMethod());

    //When all done, defuzzify the work space and store in valCrisp
    valCrisp = valFzy.defuzzify(rb.getDefuzzifyMethod());
  }


  /**
   * Retrieves the fuzzy sets.
   *
   * @return the Hashtable object that contains list of fuzzy sets.
   *
   */
  @SuppressWarnings("unchecked")
public Hashtable<String, FuzzySet> getFuzzySets() {
    return (Hashtable<String, FuzzySet>) setList.clone();
  }


  /**
   * Retrieves the fuzzy work area of this continuous variable.
   *
   * @return  the FuzzySet object that contains the work area.
   */
  public FuzzySet getFuzzyWorkArea() {
    return valFzyTmp;
  }


  /**
   * Checks to see if the given set name exists in the list of fuzzy
   * sets for this continuous variable.
   *
   * @param setName the String object that contains set name
   *
   * @return the boolean <code>true</code> if the set name exists or
   *         <code>false</code> if it does not exist in the list
   *
   */
  boolean setExist(String setName) {
    return setList.containsKey(setName);
  }


  /**
   * Retrieves the fuzzy set that has the given name.
   *
   * @param setName the String object that contains name of the fuzzy
   *                set to be retrieved
   *
   * @return the FuzzySet object with the given name, or <code>null</code>
   *          if the set does not exist
   *
   */
  FuzzySet getSet(String setName) {
    if (setExist(setName)) {
      return (FuzzySet) (setList.get(setName));
    } else {
      return null;
    }
  }


  /**
   * Retrieves the named hedged set if it exists or adds it if it does not
   * already exist in the continuous variable.
   *
   * @param setName the String object that contains the base set name
   * @param hedges the String object that contains the hedged set name
   *
   * @return the FuzzySet object that contains the hedged set
   *
   */
  FuzzySet getOrAddHedgedSet(String setName, String hedges) {
    String hedgeName = setName + " " + hedges;

    // The base set must, of course, exist!
    if (!setExist(setName)) {
      System.out.println("Error: Unknown Set " + name + " " + setName);
    }

    // If the hedged set already exists, simply return it.
    if (setExist(hedgeName)) {
      return getSet(hedgeName);
    }

    // The hedged set does not exist; create it.
    // Attempt to create a clone of the specified set.
    ((FuzzySet) (setList.get(setName))).addClone(hedgeName);

    // Clone was created and added to the set list;
    // Now apply the hedges to the clone.
    ((FuzzySet) (setList.get(hedgeName))).applyHedges(hedges);

    // Now return the newly created hedged set.
    return getSet(hedgeName);
  }


  
  /**
   * Adds the shoulder set.
   *
   * @param setName the String object that contains the name of
   *                the set to be added
   * @param alphaCut the double value for the alpha cut
   * @param ptBeg the double value for the begin point
   * @param ptEnd the double value for the end point
   * @param setDir the int value for the direction
   */
  synchronized void addSetShoulder(String setName, double alphaCut, double ptBeg, double ptEnd, int setDir) {
     setList.put(setName, new ShoulderFuzzySet(this, setName, alphaCut, ptBeg, ptEnd, setDir));
  }


  /**
   * Creates a new trapezoid fuzzy set and adds it to this continuous variable.
   *
   *
   * @param setName the String object that contains the name of
   *                       the new fuzzy set
   * @param alphaCut the double value for the alpha cut
   * @param ptLeft the double value of the point at which the lower left corner of the
   *               trapezoid is placed
   * @param ptLeftCore the double value of the point at which the upper left corner of the
   *            trapezoid is placed
   * @param ptRightCore the double value of the point at which the upper right corner of the
   *            trapezoid is placed
   * @param ptRight the double value of the point at which the lower right corner of the
   *            trapezoid is placed
   */
  synchronized void addSetTrapezoid(String setName, double alphaCut, double ptLeft, double ptLeftCore, double ptRightCore, double ptRight) {
     setList.put(setName, new TrapezoidFuzzySet(this, setName, alphaCut, ptLeft, ptLeftCore, ptRightCore, ptRight));
  }


  /**
   * Creates a new triangle fuzzy set and adds it to the specified
   * continuous variable.
   *
   * @param setName the String object that contains the name of the
   *                continuous variable that is to have a
   *                new fuzzy set added to it
   * @param alphaCut the double value of the alphacut for the newly created fuzzy set
   * @param ptLeft the double value of the point at which the lower left corner of the
   *               triangle is placed
   * @param ptCenter the double value of the point at which the upper point of the
   *               triangle is placed
   * @param ptRight the double value of the point at which the lower right corner of the
   *               triangle is placed
   */
  synchronized void addSetTriangle(String setName, double alphaCut, double ptLeft, double ptCenter, double ptRight) {
     setList.put(setName, new TriangleFuzzySet(this, setName, alphaCut, ptLeft, ptCenter, ptRight));
  }


  /**
   * Returns the name of this variable.
   *
   * @return the String object that contains the name
   *
   */
  public String toString() {
    return name;
  }
}
