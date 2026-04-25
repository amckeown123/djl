package rule;

import java.util.*;


/**
 * The <code>FuzzyRule</code> class implements a fuzzy rule with multiple antecedent clauses
 * and a single consequent clause.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class FuzzyRule extends Object {
  private FuzzyRuleBase rb;                  // Fuzzy rule base

  private String name;                       // Name or label of this rule
  private BitSet rdRefs;                     // Variables referenced in this rule
  private BitSet wrRefs;                     // Variable set by this rule
  private boolean firedFlag;                 // Whether the rule has fired
  private Vector<FuzzyClause> antecedents;                // Antecedent clauses
  private FuzzyClause consequent;  // Consequent clause


  /**
   * Creates a new fuzzy rule with a single antecedent and a
   * single consequent clause.
   *
   * @param rb the FuzzyRuleBase object this rule belongs to
   * @param name the String object that contains the name of the rule
   * @param lhs the FuzzyClause object that contains the
   *            antecedent clause
   * @param rhs the FuzzyClause object the contains the consequent
   *            clause
   */
  FuzzyRule(FuzzyRuleBase rb, String name, FuzzyClause lhs, FuzzyClause rhs) {
    this.setRb(rb);
    this.name = name;
    rdRefs = new BitSet();
    wrRefs = new BitSet();
    firedFlag = false;
    antecedents = new Vector<FuzzyClause>();
    antecedents.addElement(lhs);
    consequent = rhs;
    if (consequent != null) {
      addWrReference(consequent.getLhsReferent());
      addRdReference(consequent.getRhsReferent());
    }
    rb.addConditionalRule(this);  // add self to rule list
  }


  /**
   * Creates a new fuzzy rule with one or more antecedent clauses and a
   * single consequent clause.
   *
   * @param rb the FuzzyRuleBase object this rule belongs to
   * @param name the String object that contains the name of the rule
   * @param lhsClauses the FuzzyClause[] object that contains the
   *            antecedent clauses
   * @param rhs the consequent FuzzyClause object the contains the consequent
   *            clause
   */
  FuzzyRule(FuzzyRuleBase rb, String name, FuzzyClause[] lhsClauses, FuzzyClause rhs) {
    this.setRb(rb);
    this.name = name;
    rdRefs = new BitSet();
    wrRefs = new BitSet();
    firedFlag = false;
    antecedents = new Vector<FuzzyClause>();
    for (int i = 0; i < lhsClauses.length; i++) {
      antecedents.addElement(lhsClauses[i]);
    }
    consequent = rhs;
    if (consequent != null) {
      addWrReference(consequent.getLhsReferent());
      addRdReference(consequent.getRhsReferent());
    }
    rb.addConditionalRule(this);  // add self to rule list
  }


  /**
   * Creates a new fuzzy unconditional rule (a Fact).
   *
   * @param rb the FuzzyRuleBase object that this rule belongs to
   * @param name the String that contains the rule name
   * @param rhs the FuzzyClause object that contains the consequent clause
   */
  FuzzyRule(FuzzyRuleBase rb, String name, FuzzyClause rhs) {
    this.setRb(rb);
    this.name = name;
    rdRefs = new BitSet();
    wrRefs = new BitSet();
    firedFlag = false;
    antecedents = new Vector<FuzzyClause>();
    consequent = rhs;
    if (consequent != null) {
      addWrReference(consequent.getLhsReferent());
      addRdReference(consequent.getRhsReferent());
    }
    rb.addUnconditionalRule(this);  // add self to rule list
  }


  /**
   * Retrieves the name of this rule.
   *
   * @return the String object that contains the name of the rule
   *
   */
  public String getName() {
    return name;
  }


  /**
   * Adds to the set of variables referenced in this rule.
   *
   * @param id the integer that represents the id of the variable referenced
   *
   */
  void addRdReference(int id) {
    rdRefs.set(id);
  }


  /**
   * Retrieves the set of the variables referenced in this rule.
   *
   * @return the BitSet object that represents the referenced variables
   *
   */
  public BitSet getRdReferences() {
    return (BitSet) rdRefs.clone();
  }


  /**
   * Adds to the set of variables set by this rule.
   *
   * @param id the integer that represents the id of the variable referenced
   *
   */
  void addWrReference(int id) {
    wrRefs.set(id);
  }


  /**
   * Retrieves the set of the variables set by this rule.
   *
   * @return the BitSet object that represents the referenced variables
   *
   */
  public BitSet getWrReferences() {
    return (BitSet) wrRefs.clone();
  }


  /**
   * Retrieves the flag that indicates if this rule has been fired.
   *
   * @return the boolean true if fired or false if not fired
   *
   */
  public boolean isFired() {
    return firedFlag;
  }


  /**
   * Resets the rule (fired flag).
   *
   */
  void reset() {
    firedFlag = false;
  }


  /**
   * Retrieves the antecedent clauses of this rule.
   *
   * @return the Vector object that contains the antecedents
   *
   */
  public Vector<FuzzyClause> getAntecedents() {
    return antecedents;
  }


  /**
   * Retrieves the consequent clause.
   *
   * @return the FuzzyClause object that contains the consequent
   *
   */
  public FuzzyClause getConsequent() {
    return consequent;
  }


  /**
   * Fires this rule.
   * <p>
   * If this is an unconditional rule (there are no antecedents),
   * simply evaluates the consequent for its side-effects.
   * <p>
   * If this is a conditional rule (there are antecedents),
   * evaluates the antecedents:
   * <ul>
   * <li> If the value of an antecedent clause is below the alphacut
   *      threshold, the rule does not fire.
   * <li> Keep track of the minimum truthvalue returned when evaluating
   *      each antecedent clause.
   * <li> Pass the minimum to the correlation method ('eval' on the
   *      consequent clause).
   * <li> Update working memory to show that the variable in the
   *      consequent clause has changed.
   * </ul>
   *
   * @param alphaCut the double value for the alphaCut
   * @param workingSet the BitSet object that contains the working set
   *
   */
  void fire(double alphaCut, BitSet workingSet) {

     // (If there are no antecedents or a consequent, serious error!)
     if (antecedents.isEmpty()) {  // unconditional rule
      if (consequent != null) {
        consequent.eval();
        workingSet.or(wrRefs);
        return;
      } else {

        // If there are no antecedents, there must at least be a consequent!
        System.out.println("Error: FuzzyRule cannot fire" + name);
      }
    }

    // This is a conditional rule (there are antecedents),
    FuzzyClause clause;
    double truthValue;
    double truthValueMin = 1.0;
    boolean skipConsequent = false;

    for (int i = 0; i < antecedents.size(); i++) {

      // Get an antecedent clause and evaluate it.
      clause = (FuzzyClause) (antecedents.elementAt(i));
      truthValue = clause.eval();
      firedFlag = true;

      // Check the truth value against the alphacut threshold.
      // If the truth value is less than the threshold
      // stop evaluating the antecedent clauses and
      // prevent the consequent from being evaluated.
      if (truthValue <= alphaCut) {
        skipConsequent = true;
        break;
      }

      // Keep track of the smallest truth value from each
      // evaluated antecedent clause.
      if (truthValue < truthValueMin) {
        truthValueMin = truthValue;
      }
    }

    // If all the antecedent clauses evaluated successfully,
    // evaluate the consequent clause, and if that is evaluated
    // successfully, update the fact base.
    if (!skipConsequent) {
      if (consequent != null) {
        consequent.eval(truthValueMin);
        workingSet.or(wrRefs);
      }
    }
  }


  /**
   * Retrieve a string describing the contents of the object.
   *
   * @return    a String containing the current contents of the object
   */
  public String toString() {
    Enumeration<FuzzyClause> enum1;
    String tmpStr = "";

    if (antecedents.size() == 0) {  //  unconditional rule (fact)
      tmpStr = name + ": " + consequent.toString();
    } else {                        // conditional rule
      tmpStr = name + ": IF ";
      enum1 = antecedents.elements();
      while (enum1.hasMoreElements()) {
        tmpStr = tmpStr + ((FuzzyClause) (enum1.nextElement())).toString();
        if (enum1.hasMoreElements()) {
          tmpStr = tmpStr + "\n     AND ";
        }
      }
      tmpStr = tmpStr + "\n     THEN " + consequent.toString();
    }
    return tmpStr;
  }


public FuzzyRuleBase getRb() {
	return rb;
}


public void setRb(FuzzyRuleBase rb) {
	this.rb = rb;
}
}
