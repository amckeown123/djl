package rule;




/**
 * The <code>FuzzyRuleVariable</code> class is a abstract class that defines
 * the attributes and methods for a fuzzy rule variable.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public abstract class FuzzyRuleVariable {
  static int nextId = 0;       // For generated Ids only
  protected int type;          // Variable type
  protected int id;            // Unique Id
  protected String name;       // Variable name
  protected FuzzyRuleBase rb;  // Containing ruleset


  /**
   * Creates a new fuzzy variable with the given type, rule base, and name.
   *
   * @param type the integer that represent the type of the variable
   * @param rb the FuzzyRuleBase object this variable is a part of
   * @param name the String object that contains the name of the variable
   */
  protected FuzzyRuleVariable(int type, FuzzyRuleBase rb, String name) {
    this.type = type;
    this.rb = rb;
    this.id = nextId++;  // give unique identifier for use in BitSets
    this.name = name;
    rb.addVariable(this);  // add self to rule base
  }


  /**
   * Retrieves the id of the variable to which this object refers.
   *
   * @return   the id of the variable to which this value refers
   */
  public int getReferent() {
    return getId();
  }


  /**
   * Retrieves the type of this object.
   *
   * @return  the integer that represents the type of the variable
   */
  public int getType() {
    return type;
  }


  /**
   * Sets the value string of the variable.
   *
   * @param newValue the String object that is the value of this variable
   *
   */
  abstract void setValueString(String newValue);


  /**
   * Retrieves the value string of this variable.
   *
   * @return the String object that contains the value
   *
   */
  abstract String getValueString();


  /**
   * Resets the variable.
   */
  abstract void reset();


  /**
   * Retrives the variable type as a string.
   *
   * @return the String object that represents the variable type
   *
   */
  public String getTypeAsString() {
    return FuzzyDefs.DataType(type);
  }


  /**
   * Retrieves the id of the variable.
   *
   * @return the integer that represents the variable id
   *
   */
  public int getId() {
    return id;
  }


  /**
   * Retrieves the variable name.
   *
   * @return the String object that contains the name
   *
   */
  public String getName() {
    return name;
  }


  /**
   * Retrieve the rule base this variable is a part of.
   *
   * @return the FuzzyRuleBase object this variable belongs to
   *
   */
  FuzzyRuleBase getRuleBase() {
    return rb;
  }


  /**
   * Sets the value of this object from a fuzzy value.
   *
   * @param newValue the FuzzySet object that this variable is set to
   */
  public abstract void setFuzzyValue(FuzzySet newValue);


  /**
   * Sets the value of this object from a FuzzySet object.
   *
   * @param newValue the FuzzySet object that contains the new value
   */
  public abstract void setValue(FuzzySet newValue);


  /**
   * Retrieves the value of this object as a numeric value.
   *
   * @return    a double, if the value of this object can be represented as a
   *            numeric value
   */
  public abstract double getNumericValue();


  /**
   * Retrieve a string describing the contents of the object.
   *
   * @return    a String containing the name of the object
   */
  public String toString() {
    return name;
  }
}
