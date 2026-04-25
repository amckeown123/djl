package rule;


/**
 * The <code>FuzzyDefs</code> class implements the fuzzy operators.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
/**
 * This class defines the "Fuzzy Operator" class.
 *
 */
public class FuzzyOperator extends Object {

  /**
   * Fuzzy assignment (Is).
   *
   */
  final static public int AsgnIs = 1;  // Assignment: Is

  /**
   * Fuzzy equal to (Is).
   *
   */
  final static public int CmpIs = 2;   // Is

  /**
   * Creates a <code>FuzzyOperator</code> object.
   *
   */
  private FuzzyOperator() {}



  /**
   * Retrieves the Operator string.
   *
   * @param item the integer that represents the operator
   *
   * @return the String object that represents the operator
   *
   */
  final static String Operator(int item) {
    switch (item) {
      case AsgnIs :
        return "is";
      case CmpIs :
        return "is";
      default :
        return "Unrecognized";
    }
  }


  /**
   * Assigns the rhs fuzzy set to the lhs fuzzy rule variable.
   *
   * @param lhs the FuzzyRuleVariable object that is the lhs variable
   * @param rhs the FuzzySet object that is the rhs value
   *
   */
  static void asgnIs(FuzzyRuleVariable lhs, FuzzySet rhs) {
        lhs.setFuzzyValue(rhs);
  }


  /**
   * Assigns the rhs fuzzy set to the lhs fuzzy rule variable with a truth
   * value.
   *
   * @param lhs the FuzzyRuleVariable object that is the lhs variable
   * @param rhs the FuzzySet object that is the rhs value
   * @param truthValue the double truth value
   *
   */
  static void asgnIs(FuzzyRuleVariable lhs, FuzzySet rhs, double truthValue) {

        //This is correlation with a truth value.
        ((ContinuousFuzzyRuleVariable) lhs).setFuzzyValue(rhs, truthValue);
  }




  /**
   * Determines membership of a variable in a fuzzy set.
   *
   * @param lhs the FuzzyRuleVariable object whose membership is checked
   * @param rhs the FuzzySet object in which the variable membership is checked
   *
   * @return the double membership value
   *
   */
  static double cmpIs(FuzzyRuleVariable lhs, FuzzySet rhs) {

        // Take crisp value and look up membership
        return (rhs.membership(lhs.getNumericValue()));
  }
}
