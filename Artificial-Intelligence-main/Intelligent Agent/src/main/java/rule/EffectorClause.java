package rule;

import java.util.*;



/**
 * The <code>EffectorClause</code> class adds effectors to a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class EffectorClause extends Clause {
  Effector object;      // object to call
  String effectorName;  // method to call
  String arguments;     // parameters to pass


  /**
   * Creates an <code>EffectorClause</code> object with the given name
   * and argument string.
   *
   * @param eName the String object that contains the name of the effector
   * @param args the String object that contains arguments for the effector
   *
   */
  public EffectorClause(String eName, String args) {
    ruleRefs = new Vector<Rule>();
    truth = true;       // always true
    consequent = true;  // must be consequent
    effectorName = eName;
    arguments = args;
  }


  /**
   * Returns a display string for the effector.
   *
   * @return the String object that contains a displayable string
   *
   */

  public String display() {
    return "effector(" + effectorName + "," + arguments + ") ";
  }

  // call the effector method on the target object


  /**
   * Peforms the effector clause.
   *
   * @param rb the BooleanRuleBase object that this clause is
   *        part of
   *
   * @return the Boolean object that contains the value <code>true</code>
   *
   */
  public Boolean perform(BooleanRuleBase rb) {
    object = (Effector) (rb.getEffectorObject(effectorName));
    object.effector(this, effectorName, arguments);
    return truth;  // always true
  }
}
