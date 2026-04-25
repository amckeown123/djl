package rule;


import java.util.*;
import javax.swing.*;


/**
 * The <code>FuzzyRuleBase</code> class implements a set of rules and rule
 * variables along with a method for forward chaining.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class FuzzyRuleBase implements RuleBase {
  static int nextId = 0;
  private String name = "";

  // RuleBase options
  private double alphaCut = FuzzyDefs.AlphaCutDefault;
  private int correlationMethod = FuzzyDefs.CorrelationMethodDefault;
  private int defuzzifyMethod = FuzzyDefs.DefuzzifyMethodDefault;
  private int inferenceMethod = FuzzyDefs.InferenceMethodDefault;

  // Lists of variables
  private int varId = FuzzyDefs.VarIdInitial;  // Unique Id of next variable
  private Hashtable<String, Object> variableList = new Hashtable<String, Object>();  // List of all variables

  // Lists of rules
  private int ruleId = FuzzyDefs.RuleIdInitial;  // Unique Id of next rule
  private Vector<Object> ruleList = new Vector<Object>();        // List of all rules
  private Vector<FuzzyRule> cndRuleList = new Vector<FuzzyRule>();     // List of conditional rules
  private Vector<FuzzyRule> uncRuleList = new Vector<FuzzyRule>();  // Sequence of unconditional rules

  // Fact Base
  private BitSet fbInitial = new BitSet();       // Initial Fact Base

  private JTextArea textArea;


  /**
   * Create a new fuzzy rule base.
   *
   * @param name the String object that contains the name of the rule base
   */
  public FuzzyRuleBase(String name) {
    this.name = name;
  }


  /**
   * Sets the text area for the display of information.
   *
   * @param textArea the JTextArea to be used for the display of information
   */
  public void setDisplay(JTextArea textArea) {
    this.textArea = textArea;
  }


  /**
   * Adds the given text to the trace information.
   *
   * @param text  the String  the text to be displayed as part of the
   *               trace information
   */
  public void trace(String text) {
    if (textArea != null) {
      textArea.append(text);
    }
  }


  /**
   * Perform the forward chaining processing of a fuzzy rulebase.
   *
   * <p>Fire the rules in the ruleset.
   *
   * <p>The order of processing is as follows:
   * <ol>
   * <li>Determine whether the ruleset is executable. If it is,
   *     processing continues.
   * <li>The rules in the ruleset are reset. (See below.)
   * <li>All unconditional rules (facts or assertions) are fired.
   * <li>Conditional rules are fired until they quiesce.
   * </ol>
   *
   */
  public void forwardChain() {
     // reset all rules, variables keep values set by user
      Enumeration<Object> enum1 = ruleList.elements();

      while (enum1.hasMoreElements()) {
        ((FuzzyRule) (enum1.nextElement())).reset();
      }

      // The working fact base starts with the fact base
      // from the initial setup.
      BitSet factBase = (BitSet) fbInitial.clone();

      factBase.set(0);

      // Process the unconditional rules (assertions).
      processAssertionRules(factBase);

      // Process conditional rules in the fuzzy manner.
      processConditionalRules(factBase);
  }


  /**
   * Reset the ruleset so that rules can be fired again by setting each
   * variable is to 0.0 and all rules to their original state.
   */
  public void reset() {
    Enumeration<Object> enum1 = variableList.elements();

    while (enum1.hasMoreElements()) {
      ((FuzzyRuleVariable) (enum1.nextElement())).reset();
    }
    enum1 = ruleList.elements();
    while (enum1.hasMoreElements()) {
      ((FuzzyRule) (enum1.nextElement())).reset();
    }
  }


  /**
   * Sets the name of the rulebase.
   *
   * @param     name the new name of the rulebase
   */
  public void setName(String name) {
    this.name = name;
  }


  /**
   * Retrieves the name of the rulebase.
   *
   * @return  the current name of the rulebase
   */
  public String getName() {
    return name;
  }


  /**
   * Sets the alphacut threshold to the specified value.
   *
   * @param alphaCut the double value for the new alphacut threshold
   */
  public void setAlphaCut(double alphaCut) {
    if ((alphaCut > 0.0) && (alphaCut < 1.0)) {
      this.alphaCut = alphaCut;
    }
  }


  /**
   * Retrieves the current alphacut threshold.
   *
   * @return  the current alphacut value
   */
  public double getAlphaCut() {
    return alphaCut;
  }


  /**
   * Sets the correlation method to the specified type.
   *
   * @param correlationMethod the integer that represents to new
   *                          correlation mehtod type (enumerated in
   *                          the FuzzyDefs class)
   */
  public void setCorrelationMethod(int correlationMethod) {
    switch (correlationMethod) {
      case FuzzyDefs.PRODUCT :
      case FuzzyDefs.MINIMISE :
        this.correlationMethod = correlationMethod;
        break;
    }
  }


  /**
   * Retrieves the current correlation method.
   *
   * @return the current correlation method
   */
  public int getCorrelationMethod() {
    return correlationMethod;
  }


  /**
   * Sets the defuzzification method to the specified type.
   *
   * @param defyzzifyMethod the integer that represents the new defuzzification method
   *            (enumerated in FuzzyDefs class)
   */
  public void setDefuzzifyMethod(int defuzzifyMethod) {
    switch (defuzzifyMethod) {
      case FuzzyDefs.CENTROID :
      case FuzzyDefs.MAXHEIGHT :
        this.defuzzifyMethod = defuzzifyMethod;
        break;
    }
  }


  /**
   * Retrieves the current defuzzification method.
   *
   * @return  the current defuzzification method
   */
  public int getDefuzzifyMethod() {
    return defuzzifyMethod;
  }


  /**
   * Sets the inference method to the specified type.
   *
   * @param inferenceMethod the new inference method (enumerated in the
   *                        FuzzyDefs class)
   */
  public void setInferenceMethod(int inferenceMethod) {
    switch (inferenceMethod) {
      case FuzzyDefs.FUZZYADD :
      case FuzzyDefs.MINMAX :
        this.inferenceMethod = inferenceMethod;
        break;
    }
  }


  /**
   * Retrieves the current inference method.
   *
   * @return the current inference method
   */
  public int getInferenceMethod() {
    return inferenceMethod;
  }


  /**
   * Adds a variable to this rule base
   *
   * @param variable the Variable to be added to the rule base
   */
  public void addVariable(FuzzyRuleVariable variable) {
    variableList.put(variable.getName(), variable);
  }


  /**
   * Retrives the fuzzy variable associated with the given name.
   *
   * @param name  the String that contains the name of the variable
   *
   * @return the variable associated with the given name
   */
  public FuzzyRuleVariable getVariable(String name) {
    if (variableList.containsKey(name)) {
      return (FuzzyRuleVariable) variableList.get(name);
    }
    return null;
  }


  /**
   * Retrieve the list of variables.
   *
   * @return    a list of variables defined in this ruleset
   */
  @SuppressWarnings("unchecked")
public Hashtable<String, Object> getVariables() {
    return (Hashtable<String, Object>) variableList.clone();
  }


  /**
   * Adds a conditional rule to this rule base.
   *
   * @param rule the FuzzyRule object to be added
   *
   */
  public void addConditionalRule(FuzzyRule rule) {
    ruleList.addElement(rule);  // add a rule to the rule list
    cndRuleList.addElement(rule);
  }


  /**
   * Adds an unconditional rule to this rule base.
   *
   * @param rule the FuzzyRule object to be added
   *
   */
  public void addUnconditionalRule(FuzzyRule rule) {
    ruleList.addElement(rule);  // add a rule to the rule list
    uncRuleList.addElement(rule);
  }


  /**
   * Retrieve the list of rules.
   *
   * @return   a list of rules defined in this ruleset
   */
  public Hashtable<?, ?> getRules() {
    return (Hashtable<?, ?>) ruleList.clone();
  }


  /**
   * Creates a fuzzy clause for use in constructing a rule.
   *
   * @param     lhs the ContinuousFuzzyRuleVariable object that is the
   *            lefthand side of the clause (must be a reference to an
   *            existing variable)
   * @param     oper the integer that represents the relation connecting
   *            the left- and righthand sides of the clause
   * @param     hedges the String object that contains the list of hedges
   *            to be applied to the fuzzy set (if
   *            not used in the rule, hedges must be the empty string, and
   *            not null)
   * @param     setName the String that contains the righthand side of the
   *            clause, the name of a fuzzy set.
   *
   * @return the FuzzyClause object created
   */
  public FuzzyClause createClause(ContinuousFuzzyRuleVariable lhs, int oper, String hedges, String setName) {

    // Retrieve the fuzzy set object based on hedges and setName
    FuzzySet rhs = getFuzzySet(lhs, setName, hedges);

    // Create a clause to place into a rule.
    FuzzyClause clause = new FuzzyClause(lhs, oper, rhs);

    return clause;
  }

  /**
   * This method retrieves a fuzzy set with specified hedges.
   *
   * @param     lhs the ContinousFuzzyRuleVariable object that is the
   *            lefthand side of a potential clause
   * @param     setName the String object that contains
   *            the righthand side of a clause, the name of a fuzzy set
   * @param     hedges the String object that contains a
   *            list of hedges to be applied to the fuzzy set
   * @return    a reference to a righthand side object, a FuzzySet.
   */
  private FuzzySet getFuzzySet(ContinuousFuzzyRuleVariable lhs, String setName, String hedges) {
    FuzzySet rhs = null;
    String value = setName.trim();
    String tmpHedges = hedges.trim();

    // The value on the Rhs represents a fuzzy set name;
    // The named set must exist.
    if (lhs.setExist(value)) {
      if (tmpHedges.length() == 0) {
        rhs = lhs.getSet(value);
      } else {
        rhs = lhs.getOrAddHedgedSet(value, tmpHedges);
      }
    } else {
      System.out.println("Error: Invalid fuzzy set name " + value);
    }
    return rhs;
  }


  /**
   * Retrieves the initial fact base.
   *
   * @return    Working memory as it exists at the start of
   *            inferencing.
   */
  public BitSet getInitialFactBase() {
    return (BitSet) fbInitial.clone();
  }


  /**
   * Clears the ruleset of all variables and rules,  set the ruleset name
   * to the empty string (""), and set all ruleset options to default values.
   *
   */
  public void clear() {

    // Ruleset name
    name = "";

    // Ruleset options
    alphaCut = FuzzyDefs.AlphaCutDefault;
    correlationMethod = FuzzyDefs.CorrelationMethodDefault;
    defuzzifyMethod = FuzzyDefs.DefuzzifyMethodDefault;
    inferenceMethod = FuzzyDefs.InferenceMethodDefault;

    // Lists of variables
    setVarId(FuzzyDefs.VarIdInitial);
    variableList.clear();

    // Lists of rules
    setRuleId(FuzzyDefs.RuleIdInitial);
    ruleList.clear();
    cndRuleList.clear();
    uncRuleList.removeAllElements();

    // Fact Base
    fbInitial = new BitSet();
  }


  /**
   * Fires all assertion rules.
   *
   * @param     factBase  the BitSet indicating which variables have known
   *                      values which is updated as each rule fires
   */
  private void processAssertionRules(BitSet factBase) {
    trace("\nProcessing unconditional fuzzy rules ");
    Enumeration<FuzzyRule> enum1 = uncRuleList.elements();

    while (enum1.hasMoreElements()) {
      FuzzyRule rule = (FuzzyRule) (enum1.nextElement());

      rule.fire(alphaCut, factBase);
    }
  }


  /**
   * Fires all conditional (if-then) rules.
   *
   * @param     factBase the BitSet indicating which variables have known
   *            values which is updated as each rule fires.
   */
  private void processConditionalRules(BitSet factBase) {
    boolean moreRules = true;
    Vector<FuzzyRule> tmpRuleSet = new Vector<FuzzyRule>();

    trace("\nProcessing conditional fuzzy rules ");
    while (moreRules) {

      // Create a rule set:
      // Examine all conditional rules.
      // If a rule has fired, ignore it.
      // If a rule has not fired, and if it can be fired given
      // the current fact base, add it to the rule set.
      Enumeration<FuzzyRule> enum1 = cndRuleList.elements();

      while (enum1.hasMoreElements()) {
        FuzzyRule rule = (FuzzyRule) (enum1.nextElement());

        if (!rule.isFired()) {

          // The rule has not fired; check the rule's antecedents reference
          BitSet tmpRuleFacts = rule.getRdReferences();
          BitSet tempFacts = rule.getRdReferences();

          tempFacts.and(factBase);

          // If the antecedents reference variables whose values have been
          // determined, the rule can fire, so add it to the rule set.
          if (tempFacts.equals(tmpRuleFacts)) {
            tmpRuleSet.addElement(rule);
          }
        }
      }

      // If the rule set is empty, no rules can be fired; exit the loop.
      if (tmpRuleSet.isEmpty()) {
        moreRules = false;
        break;
      }

      // Okay, we have some rules in the generated rule set;
      // Attempt to fire them all.  Rules that fire successfully
      // update the fact base directly.
      for (int i = 0; i < tmpRuleSet.size(); i++) {
        FuzzyRule rule = (FuzzyRule) tmpRuleSet.elementAt(i);

        trace("\nFiring fuzzy rule: " + rule.getName());
        rule.fire(alphaCut, factBase);
      }

      // We may or may not have fired all the rules in the rule set;
      // In any event, do a purge of the rule set to get ready for the
      // next iteration of the while loop.
      tmpRuleSet.removeAllElements();
    }
  }



   /**
   * Displays the variables in the given text area.
   *
   * @param textArea the JTextArea object where the variables are displayed
   *
   */
  public void displayVariables(JTextArea textArea) {
    Enumeration<Object> enum1 = variableList.elements();

    while (enum1.hasMoreElements()) {
      FuzzyRuleVariable temp = (FuzzyRuleVariable) enum1.nextElement();

      textArea.append("\n" + temp.getName() + " value = " + temp.getNumericValue());
    }
  }


   /**
   * Displays the rules in the given text area.
   *
   * @param textArea the JTextArea object where the rules are displayed
   *
   */
  public void displayRules(JTextArea textArea) {
    textArea.append("\n" + name + " Fuzzy Rule Base: " + "\n");
    Enumeration<Object> enum1 = ruleList.elements();

    while (enum1.hasMoreElements()) {
      FuzzyRule temp = (FuzzyRule) enum1.nextElement();

      textArea.append("\n" + temp.toString());
    }
    textArea.append("\n");
  }


  /**
   * Not supported in a fuzzy rule base.
   *
   * @param goalVarName the String object
   */
  public void backwardChain(String goalVarName) {
    textArea.append("\nBackward Chaining is not supported in fuzzy rule base");
  }


  /**
   * Not supported in a fuzzy rule base.
   *
   * @return the Vector object
   */
  public Vector<?> getGoalVariables() {
    return new Vector<>();
  }


public int getVarId() {
	return varId;
}


public void setVarId(int varId) {
	this.varId = varId;
}


public int getRuleId() {
	return ruleId;
}


public void setRuleId(int ruleId) {
	this.ruleId = ruleId;
}
}
