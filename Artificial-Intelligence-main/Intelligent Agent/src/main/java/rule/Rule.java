package rule;

import java.util.*;

import javax.swing.*;


/**
 * The <code>Rule</code> class implements a rule with multiple antecedent clauses
 * and a single consequent clause.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class Rule {
  BooleanRuleBase rb;
  String name;
  Clause antecedents[];  // allow up to 4 antecedents for now
  Clause consequent;     //only 1 consequent clause allowed
  Boolean truth;         // states = (null=unknown, true, or false)
  boolean fired = false;


  /**
   * Creates a <code>Rule</code> for the given rule base, with the given name,
   * antecedent and consequent.
   *
   * @param rb    the BooleanRuleBase associated with this rule
   * @param name  the String that contains the name of the rule
   * @param lhs   the antecedent clause of this rule
   * @param rhs   the consequent clause of this rule
   */
  public Rule(BooleanRuleBase rb, String name, Clause lhs, Clause rhs) {
    this.rb = rb;
    this.name = name;
    antecedents = new Clause[1];
    antecedents[0] = lhs;
    lhs.addRuleRef(this);
    consequent = rhs;
    rhs.addRuleRef(this);
    rhs.setConsequent();
    rb.ruleList.addElement(this);  // add self to rule list
    truth = null;
  }


  /**
   * Creates a <code>Rule</code> for the given rule base, with the given name,
   * one or more antecedents and consequent.
   *
   * @param rb    the BooleanRuleBase associated with this rule
   * @param name  the String that contains the name of the rule
   * @param antecedents   the antecedent clauses of this rule
   * @param lhsClauses the Clause[] object
   * @param rhs   the consequent clause of this rule
   */
  public Rule(BooleanRuleBase rb, String name, Clause[] lhsClauses, Clause rhs) {
    this.rb = rb;
    this.name = name;
    antecedents = new Clause[lhsClauses.length];
    for (int i = 0; i < lhsClauses.length; i++) {
      antecedents[i] = lhsClauses[i];
      antecedents[i].addRuleRef(this);
    }
    consequent = rhs;
    rhs.addRuleRef(this);
    rhs.setConsequent();
    rb.ruleList.addElement(this);  // add self to rule list
    truth = null;
  }


  /**
   * Retrieves the number of antecedent clauses in this rule.
   *
   * @return the number of antecedents
   */
  long numAntecedents() {
    return antecedents.length;
  }


  /**
   * Retests all clauses and rules that reference a variable in a forward
   * chaining system.
   *
   * @param clauseRefs  the Vector that contains the clauses that reference a
   *                    certain variable
   */
  public static void checkRules(Vector<?> clauseRefs) {
    Enumeration<?> enum1 = clauseRefs.elements();

    while (enum1.hasMoreElements()) {
      Clause temp = (Clause) enum1.nextElement();
      Enumeration<?> enum2 = temp.ruleRefs.elements();

      while (enum2.hasMoreElements()) {
        ((Rule) enum2.nextElement()).check();  // retest the rule
      }
    }
  }


  /**
   * Tests the antecedent clauses in this rule in a forward chaining system.
   *
   * @return <code>true</code> if all the antecedent clauses are true, <code>false
   *         </code> if any of the antecedent clauses are false, or <code>null
   *         </code> if the truth value of any of the antecedent clauses cannot be
   *         determined
   */
  Boolean check() {
    rb.trace("\nTesting rule " + name);
    for (int i = 0; i < antecedents.length; i++) {
      if (antecedents[i].truth == null) {
        return truth = null;
      }
      if (antecedents[i].truth.booleanValue() == true) {
        continue;
      } else {
        return truth = false;  //don't fire this rule
      }
    }                                       // endfor
    return truth = true;  // could fire this rule
  }


  /**
   * Fires a rule in a forward chaining system by performing the consequent clause,
   * updating all clauses where any changed variables are referenced, and updating
   * all the rules that contain those clauses.
   */
  void fire() {
    rb.trace("\nFiring rule " + name);
    truth = true;
    fired = true;
    if (consequent.lhs == null) {

      // it's an effector
      ((EffectorClause) consequent).perform(rb);  // call the effector method
    } else {

      // set the variable value and update clauses
      consequent.lhs.setValue(consequent.rhs);

      // now retest any rules whose clauses just changed
      checkRules(consequent.lhs.clauseRefs);
    }
  }


  /**
   * Determines if a rule is true by recursively trying to prove its
   * antecedent clauses are true or by determining that any of its antecedent
   * clauses are false.
   *
   * @return <code>true</code> if the antecedent clauses of this rule can be
   *          proven true, or <code>false</code> if any of the antecedents
   *          are false.
   */
  Boolean backChain() {
    rb.trace("\nEvaluating rule " + name);
    for (int i = 0; i < antecedents.length; i++) {  // test each clause
      if (antecedents[i].truth == null) {
        rb.backwardChain(antecedents[i].lhs.name);
      }
      if (antecedents[i].truth == null) {  // we couldn't prove true or false
        antecedents[i].lhs.askUser();               // so ask user for help
        truth = antecedents[i].check();             // redundant?
      }                                             // endif
      if (antecedents[i].truth.booleanValue() == true) {
        continue;                                   // test the next antecedent (if any)
      } else {
        return truth = false;          // exit, if any are false
      }

    }                                               // endfor
    return truth = true;  // all antecedents are true
  }


  /**
   * Displays this rule in text format in the given text area.
   *
   * @param textArea the JTextArea where the rule is displayed
   */
  void display(JTextArea textArea) {
    textArea.append(name + ": IF ");
    for (int i = 0; i < antecedents.length; i++) {
      Clause nextClause = antecedents[i];

      textArea.append(nextClause.toString());
      if ((i + 1) < antecedents.length) {
        textArea.append("\n     AND ");
      }
    }
    textArea.append("\n     THEN ");
    textArea.append(consequent.toString() + "\n");
  }


  /**
   * Resets the rule, allowing it to be fired again.
   */
  void reset() {
    fired = false;
  }  // allow rule to fire again
}
