package rule;

import java.util.*;



/**
 * The <code>Clause</code> class implements the both the antecedent and
 * consequent parts of a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class Clause {
  Vector<Rule> ruleRefs;
  RuleVariable lhs;
  String rhs;
  Condition cond;
  boolean consequent;  // true if clause is a consequent clause
  Boolean truth;       // states = null(unknown), true or false


  /**
   * Creates a clause with the given rule variable on the left-hand side, the
   * condition, and string value on the right-hand side.
   *
   * @param lhs the RuleVariable that makes up the left-hand side of this clause
   * @param cond the Condition which tests equality, greater than, or less than
   * @param rhs the String that makes up the right-hand side of this clause
   */
  public Clause(RuleVariable lhs, Condition cond, String rhs) {
    this.lhs = lhs;
    this.cond = cond;
    this.rhs = rhs;
    lhs.addClauseRef(this);
    ruleRefs = new Vector<Rule>();
    truth = null;
    consequent = false;  // default is antecedent
  }


  /**
   * Creates a clause.
   */
  public Clause() {}


  /**
   * Retrieves the <code>Clause</code> in a format that can be displayed.
   *
   * @return the <code> String representation of this clause
   */
  public String toString() {
    return lhs.name + cond.toString() + rhs + " ";
  }


  /**
   * Registers the given rule with this clause.
   *
   * @param ref the Rule to be registered with this clause
   */
  public void addRuleRef(Rule ref) {
    ruleRefs.addElement(ref);
  }


  /**
   * Performs a test of the clause.  This method handles String and numeric values.
   * It does not perform a lot of error checking!
   *
   * @return  a Boolean <code>true</code> if the logical condition is true, <code>false
   * </code> if the logical condition is false, or <code>null</code> if the
   * clause is a consequent clause or the left-hand side of the clause is
   * unbound.
   */
  public Boolean check() {
    if (consequent == true) {
      return truth = null;
    }
    if (lhs.value == null) {
      return truth = null;  // can't check truth, if variable value is undefined
    } else {
      Double lhsNumericValue = null;
      Double rhsNumericValue = null;
      boolean bothNumeric = true;

      try {
        lhsNumericValue = Double.valueOf(lhs.value);
        rhsNumericValue = Double.valueOf(rhs);
      } catch (Exception e) {

        // if exception is thrown, assume one or both values are NOT numeric
        // treat them as String values
        bothNumeric = false;
      }
      switch (cond.index) {
        case 1 :
          if (bothNumeric) {
            truth = lhsNumericValue.compareTo(rhsNumericValue) == 0;
          } else {
            truth = lhs.value.equalsIgnoreCase(rhs);
          }
          break;
        case 2 :
          if (bothNumeric) {
            truth = lhsNumericValue.compareTo(rhsNumericValue) > 0;
          } else {
            truth = lhs.value.compareTo(rhs) > 0;
          }
          break;
        case 3 :
          if (bothNumeric) {
            truth = lhsNumericValue.compareTo(rhsNumericValue) < 0;
          } else {
            truth = lhs.value.compareTo(rhs) < 0;
          }
          break;
        case 4 :
          if (bothNumeric) {
            truth = lhsNumericValue.compareTo(rhsNumericValue) != 0;
          } else {
            truth = lhs.value.compareTo(rhs) != 0;
          }
          break;
      }
      return truth;
    }
  }


  /**
   * Determines if the clause is a consequent clause.
   */
  public void setConsequent() {
    consequent = true;
  }


  /**
   * Retrieves the rule that owns this clause.
   *
   * @return the owning rule if this clause is a consquent. Otherwise returns
   *          <code>null</code>.
   */
  public Rule getRule() {
    if (consequent == true) {
      return (Rule) ruleRefs.firstElement();
    } else {
      return null;
    }
  }
}
