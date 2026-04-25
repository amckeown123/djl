package rule;


/**
 * The <code>FuzzyClause</code> class implements a clause with a left-hand
 * side value, an operator, and a righ-hand side value used in both the
 * antecedent and consequent parts of a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */

 public class FuzzyClause extends Object {
  FuzzyRuleVariable lhs;  // Lefthand side value
  int op;                 // Operator
  FuzzySet rhs;           // Righthand side value
  boolean consequent;     // true if clause is a consequent clause


   /**
   * Creates a new fuzzy clause with the specified lvalue, operator, and
   * rvalue.
   *
   * @param     lhs the FuzzyRuleVariable which is the lvalue
   * @param     op the integer operator
   * @param     rhs the FuzzySet which is the rvalue
   */
  protected FuzzyClause(FuzzyRuleVariable lhs, int op, FuzzySet rhs) {
    this.lhs = lhs ;
    this.op = op ;
    this.rhs = rhs ;
    if (op == FuzzyOperator.AsgnIs) {
      consequent = true;    // consequent
    } else {
      consequent = false ; // antecedent
    }
  }


  /**
   * Evaluates the clause.
   *
   * @return the double value that results from the evaluation of the clause
   *
   */
  protected double eval() {
    if (consequent == false) {
      return FuzzyOperator.cmpIs(lhs, rhs); // antecedent
    } else {
      FuzzyOperator.asgnIs(lhs, rhs);       // consequent
      return 0.0 ;
    }
  }

  /**
   * Evaluates the clause as a consequent clause.
   *
   * @param truthValue the double value that represents the truth value
   *
   * @return    0.0
   */
  protected double eval(double truthValue) {
         FuzzyOperator.asgnIs(lhs, rhs, truthValue);
         return 0.0 ;
  }

  /**
   * Retrieves the left-hand side of the clause.
   *
   * @return the FuzzyRuleVariable object that is the lhs
   *
   */
  public FuzzyRuleVariable getLhs() {
    return lhs;
  }


  /**
   * Retrieves the operator of the clause.
   *
   * @return the int that represents the operator
   *
   */
  public int getOp() {
    return op;
  }


  /**
   * Retrieves the operator as a String object.
   *
   * @return the String object that represents the operator
   */
  public String getOpAsString() {
    return FuzzyOperator.Operator(op);
  }


  /**
   * Retrieves the right-hand side of the clause.
   *
   * @return the FuzzySet object that is the rhs
   */
  public FuzzySet getRhs() {
    return rhs;
  }


  /**
   * Retrieves the referent from the rhs FuzzySet object.
   *
   * @return the integer that represents the referent
   *
   */
  protected int getReferent() {
    return rhs.getReferent();
  }


  /**
   * Retrieves the referent from the lhs FuzzyRuleVariable object.
   *
   * @return the integer that represents the referent
   *
   */
  protected int getLhsReferent() {
    return lhs.getReferent();
  }


  /**
   * Retrieves the referent from the rhs FuzzySet object.
   *
   * @return the integer that represents the referent
   *
   */
  protected int getRhsReferent() {
    return rhs.getReferent();
  }


  /**
   * Retrieve a string describing the contents of the object.
   *
   * @return a String containing the current contents of the object
   */
  public String toString() {
    String tmpStr = "";

    tmpStr = lhs.getName();
    tmpStr = tmpStr + " " + FuzzyOperator.Operator(op) + " ";
    tmpStr = tmpStr + rhs.getSetName();

    return tmpStr;
  }
}
