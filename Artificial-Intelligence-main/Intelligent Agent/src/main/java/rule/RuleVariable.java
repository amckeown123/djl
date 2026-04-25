package rule;

 
import java.util.*;

import javax.swing.*;


/**
 * The <code>RuleVariable</code> class implements the rule variable class
 * used in a boolean rule base.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class RuleVariable extends Variable {
  protected BooleanRuleBase rb;
  protected Vector<Clause> clauseRefs;  // clauses which refer to this var
  protected String promptText;  // used to prompt user for value
  protected String ruleName;    // if value is inferred, null = user provided


  /**
   * Creates a new rule variable object with specified name.
   *
   * @param rb the BooleanRuleBase object this rule variable belongs to
   * @param name the String object that contains the name of the variable
   */
  public RuleVariable(BooleanRuleBase rb, String name) {
    super(name);
    this.rb = rb;
    rb.addVariable(this);
    clauseRefs = new Vector<Clause>();
  }


  /**
   * Set the value of the variable and retests all clauses that refer to
   * this variable.
   *
   * @param value the String object that contains the new value
   */
  public void setValue(String value) {
    this.value = value;
    updateClauses();
  }


  /**
   * Prompts the user to provide a value for a variable during backward
   * inferencing (set to null if user cancels out of dialog).
   *
   * @return the String object that contains the user response
   */
  public String askUser() {
    String answer = null;

    // position dialog over parent dialog
    JFrame frame = new JFrame();
    RuleVarDialog dlg = new RuleVarDialog(frame, "Ask User for Value", true);

    dlg.setLocation(200, 200);
    dlg.setData(this);
    dlg.setVisible(true);
    answer = dlg.getData();  // retrieve user value
    setValue(answer);  // set value, update clauses
    return value;
  }


  /**
   *  Adds a reference to a rule clause.
   *
   * @param ref the Clause object to be added
   */
  public void addClauseRef(Clause ref) {
    clauseRefs.addElement(ref);
  }


  /**
   *  Checks all rule clauses that refer to this variable.
   */
  public void updateClauses() {
    Enumeration<Clause> enum1 = clauseRefs.elements();

    while (enum1.hasMoreElements()) {
      ((Clause) enum1.nextElement()).check();  // retest the truth condition
    }
  }


  /**
   * Sets the rule name.
   *
   * @param ruleName the String object that contains the name
   *
   */
  public void setRuleName(String ruleName) {
    this.ruleName = ruleName;
  }


  /**
   * Sets the prompt text for this variable.
   *
   * @param prompText the String object that contains the prompt text
   *
   */
  public void setPromptText(String prompText) {
    this.promptText = prompText;
  }


  /**
   * Retrieves the prompt text for this variable.
   *
   * @return the String object that contains the prompt text
   *
   */
  public String getPromptText() {
    return promptText;
  }


  /**
   * Not used.
   *
   * @param inValue the String object
   *
   */
  public void computeStatistics(String inValue) {}
  ;


  /**
   * Not used.
   *
   * @param inValue the String object
   * @param outArray the float[] object
   * @param inx the int object
   *
   * @return the int object
   *
   */
  public int normalize(String inValue, float[] outArray, int inx) {
    return inx;
  }
}

