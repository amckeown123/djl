package rule;

import java.util.*;

import javax.swing.*;


/**
 * The <code>BooleanRuleBase</code> class implements a set of rules and rule
 * variables along with methods for forward and backward chaining.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class BooleanRuleBase implements RuleBase {
  String name;
  Hashtable<String, Object> variableList = new Hashtable<String, Object>();  // all variables in the rulebase
  Clause clauseVarList[];
  Vector<Object> ruleList = new Vector<>();  // list of all rules
  Vector<?> conclusionVarList;        // queue of variables
  Rule rulePtr;                    // working pointer to current rule
  Clause clausePtr;                // working pointer to current clause
  Stack<Clause> goalClauseStack = new Stack<Clause>();  // for goals (cons clauses) and subgoals
  Hashtable<String, Object> effectors;
  Hashtable<String, Object> sensors;
  Vector<Object> factList;
  JTextArea textArea1;


  /**
   * Sets the text area for the display of information.
   *
   * @param txtArea the JTextArea to be used for the display of information
   */
  public void setDisplay(JTextArea txtArea) {
    textArea1 = txtArea;
  }


  /**
   * Creates a <code>BooleanRuleBase</code> with the given name.
   *
   * @param name the String that contains the name of the rule base
   */
  public BooleanRuleBase(String name) {
    this.name = name;
  }


  /**
   * Adds the given text to the trace information.
   *
   * @param text  the String that contains the text to be displayed as part of the
   *              trace information
   */
  public void trace(String text) {
    if (textArea1 != null) {
      textArea1.append(text);
    }
  }


  /**
   * Displays all variables and their values in the given text area.
   *
   * @param textArea  the JTextArea where the variables and their values are
   *                  displayed
   */
  public void displayVariables(JTextArea textArea) {
    Enumeration<Object> enum1 = variableList.elements();

    while (enum1.hasMoreElements()) {
      RuleVariable temp = (RuleVariable) enum1.nextElement();

      textArea.append("\n" + temp.name + " value = " + temp.value);
    }
  }


  /**
   * Displays all the rules and  facts in text format in the given text area.
   *
   * @param textArea  the JTextArea where the rules and facts are displayed
   */
  public void displayRules(JTextArea textArea) {
    textArea.append("\n" + name + " Rule Base: " + "\n");
    Enumeration<Object> enum1 = ruleList.elements();

    while (enum1.hasMoreElements()) {
      Rule temp = (Rule) enum1.nextElement();

      temp.display(textArea);
    }

    // now display the facts
    if (factList != null) {
      enum1 = factList.elements();
      while (enum1.hasMoreElements()) {
        Fact temp = (Fact) enum1.nextElement();

        temp.display(textArea);
      }
    }
  }


  /**
   * Dislays all rules in the conflict set.
   *
   * @param ruleSet the Vector that contains the conflict set of rules
   */
  public void displayConflictSet(Vector<Rule> ruleSet) {
    trace("\n" + " -- Rules in conflict set:\n");
    Enumeration<Rule> enum1 = ruleSet.elements();

    while (enum1.hasMoreElements()) {
      Rule temp = (Rule) enum1.nextElement();

      trace(temp.name + "(" + temp.numAntecedents() + "), ");
    }
  }


  /**
   * Resets the rule base for another round of inferencing.
   */
  public void reset() {
    trace("\n --- Setting all " + name + " variables to null");
    Enumeration<Object> enum1 = variableList.elements();

    while (enum1.hasMoreElements()) {
      RuleVariable temp = (RuleVariable) enum1.nextElement();

      temp.setValue(null);
    }
    if (factList != null) {
      enum1 = factList.elements();
      while (enum1.hasMoreElements()) {
        Fact temp = (Fact) enum1.nextElement();

        temp.fired = false;
      }
    }
    Enumeration<Object> enum2 = ruleList.elements();

    while (enum2.hasMoreElements()) {
      Rule temp = (Rule) enum2.nextElement();

      temp.reset();  // set fired flag to false
    }
  }


  /**
   * Determines if the given goal is true by finding the set of rules that
   * refer to the goal in their consequent clause and then testing the
   * antecedent clauses to determine if the rule should fire. If an antecedent
   * clause cannot be tested because a variable is unknown, that variable becomes
   * the new goal variable and the process recurses until the given goal is found
   * to be true or false.
   *
   * @param goalVarName the String that contains the name fo the goal variable
   */
  public void backwardChain(String goalVarName) {
    RuleVariable goalVar = (RuleVariable) variableList.get(goalVarName);
    Enumeration<?> goalClauses = goalVar.clauseRefs.elements();

    while (goalClauses.hasMoreElements()) {
      Clause goalClause = (Clause) goalClauses.nextElement();

      if (goalClause.consequent == false) {
        continue;
      }
      goalClauseStack.push(goalClause);
      Rule goalRule = goalClause.getRule();
      Boolean ruleTruth = goalRule.backChain();  // find rule truth value

      if (ruleTruth == null) {
        trace("\nRule " + goalRule.name + " is null, can't determine truth value.");
      } else if (ruleTruth.booleanValue() == true) {

        // rule is OK, assign consequent value to variable
        goalVar.setValue(goalClause.rhs);
        goalVar.setRuleName(goalRule.name);
        goalClauseStack.pop();                   // clear item from subgoal stack
        trace("\nRule " + goalRule.name + " is true, setting " + goalVar.name + ": = " + goalVar.value);
        if (goalClauseStack.empty() == true) {
          trace("\n +++ Found Solution for goal: " + goalVar.name);
          break;                                 // for now, only find first solution, then stop
        }
      } else {
        goalClauseStack.pop();                   // clear item from subgoal stack
        trace("\nRule " + goalRule.name + " is false, can't set " + goalVar.name);
      }
    }                                            // endwhile
    if (goalVar.value == null) {
      trace("\n +++ Could Not Find Solution for goal: " + goalVar.name);
    }
  }


  /**
   * Finds the set of rules that can fire when forward chaining.
   *
   * @param test  the boolean value that determines if the antecedents should be
   *              tested (<code>true</code>) or just checked (<code>false</code>)
   * @return the Vector of rules that can fire
   */
  public Vector<Rule> match(boolean test) {
    Vector<Rule> matchList = new Vector<Rule>();
    Enumeration<Object> enum1 = ruleList.elements();

    while (enum1.hasMoreElements()) {
      Rule testRule = (Rule) enum1.nextElement();

      if (test) {
        testRule.check();  // test the rule antecedents
      }
      if (testRule.truth == null) {
        continue;
      }

      // fire the rule only once for now
      if ((testRule.truth.booleanValue() == true) && (testRule.fired == false)) {
        matchList.addElement(testRule);
      }
    }
    displayConflictSet(matchList);
    return matchList;
  }


  /**
   * Selects a rule to fire when forward chaining, based on specificity.
   *
   * @param ruleSet the Vector of rules that can fire
   *
   * @return the selected rule
   */
  public Rule selectRule(Vector<?> ruleSet) {
    Enumeration<?> enum1 = ruleSet.elements();
    long numClauses;
    Rule nextRule;
    Rule bestRule = (Rule) enum1.nextElement();
    long max = bestRule.numAntecedents();

    while (enum1.hasMoreElements()) {
      nextRule = (Rule) enum1.nextElement();
      if ((numClauses = nextRule.numAntecedents()) > max) {
        max = numClauses;
        bestRule = nextRule;
      }
    }
    return bestRule;
  }


  /**
   * Fires rules, generating new data, based on the initial set of data. The
   * process repeats with the new data until no more rules and be fired.
   */
  public void forwardChain() {
    Vector<Rule> conflictRuleSet = new Vector<>();

    // first test all rules, based on initial data
    conflictRuleSet = match(true);  // see which rules can fire
    while (conflictRuleSet.size() > 0) {
      Rule selected = selectRule(conflictRuleSet);  // select the "best" rule

      selected.fire();  // fire the rule, do the consequent action, update all clauses and rules
      conflictRuleSet = match(false);  // see which rules can fire
    }
  }


  /**
   * Adds an effector and effector name to the rule base.
   *
   * @param obj           the Object that contains the effector
   * @param effectorName  the String that contains the name of the effector
   */
  public void addEffector(Object obj, String effectorName) {
    if (effectors == null) {
      effectors = new Hashtable<String, Object>();
    }
    effectors.put(effectorName, obj);
  }


  /**
   * Retrieves the effector associated with the given name.
   *
   * @param effectorName  the String that contains the name of the effector to be
   *                      be retrieved
   *
   * @return the effector object associated with the given name
   */
  public Object getEffectorObject(String effectorName) {
    return effectors.get(effectorName);
  }


  /**
   * Adds a sensor and sensor name to the rule base.
   *
   * @param obj         the Object that contains the sensor
   * @param sensorName  the String that contains the name of the sensor
   */
  public void addSensor(Object obj, String sensorName) {
    if (sensors == null) {
      sensors = new Hashtable<String, Object>();
    }
    sensors.put(sensorName, obj);
  }


  /**
   * Retrieves the sensor associated with the given name.
   *
   * @param sensorName  the String that contains the name of the sensor to be
   *                    be retrieved
   *
   * @return the sensor object associated with the given name
   */
  public Object getSensorObject(String sensorName) {
    return sensors.get(sensorName);
  }


  /**
   * Initializes the facts associated with this rule base.
   */
  public void initializeFacts() {
    if (factList != null) {
      Enumeration<Object> enum1 = factList.elements();

      while (enum1.hasMoreElements()) {
        Fact fact = (Fact) enum1.nextElement();

        fact.asserting(this);
      }
    }
  }


  /**
   * Adds a fact to this rule base.
   *
   * @param fact  the Fact to be added to the rule base
   */
  public void addFact(Fact fact) {
    if (factList == null) {
      factList = new Vector<Object>();
    }
    factList.addElement(fact);
  }


  /**
   * Adds a variable to this rule base
   *
   * @param variable the Variable to be added to the rule base
   */
  public void addVariable(RuleVariable variable) {
    variableList.put(variable.getName(), variable);
  }


  /**
   * Retrieves the list of variables.
   *
   * @return a Hashtable object that contains the list of variables
   *         defined in this ruleset
   */
  @SuppressWarnings("unchecked")
public Hashtable<String, Object> getVariables() {
    return (Hashtable<String, Object>) variableList.clone();
  }


  /**
   * Retreives all the variables referenced in the consequent of any rule in
   * the rule base.
   *
   * @return the Vector that contains the goal variables
   */
  public Vector<RuleVariable> getGoalVariables() {
    Vector<RuleVariable> goalVars = new Vector<RuleVariable>();
    Enumeration<Object> enum1 = variableList.elements();

    while (enum1.hasMoreElements()) {
      RuleVariable ruleVar = (RuleVariable) enum1.nextElement();
      Vector<?> goalClauses = ruleVar.clauseRefs;

      if ((goalClauses != null) && (goalClauses.size() != 0)) {
        goalVars.addElement(ruleVar);
      }
    }
    return goalVars;
  }


  /**
   * Retrives the variable associated with the given name.
   *
   * @param name  the String that contains the name of the variable
   *
   * @return the variable associated with the given name
   */
  public RuleVariable getVariable(String name) {
    if (variableList.containsKey(name)) {
      return (RuleVariable) variableList.get(name);
    }
    return null;
  }


  /**
   * Sets the value of the variable associated with the given name.
   *
   * @param name  the String that contains the name of the variable
   * @param value the String object that contains
   *
   * @return the variable associated with the given name
   */
  public void setVariableValue(String name, String value) {
    RuleVariable variable = getVariable(name);

    if (variable != null) {
      variable.setValue(value);
    } else {
      System.out.println("BooleanRuleBase: Can't set value, variable " + name + " is not defined!");
    }
  }
}
