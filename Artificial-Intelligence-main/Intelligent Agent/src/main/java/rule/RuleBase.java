package rule;

import java.util.*;
import javax.swing.*;

/**
 * The <code>RuleBase</code> defines the interface for the different rule
 * bases (BooleanRuleBase and FuzzyRuleBase).
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public interface RuleBase {


  /**
   * Sets the text area for display.
   *
   * @param txtArea the JTextArea object used to display messages
   *
   */
  public void setDisplay(JTextArea txtArea);


  /**
   * Displays a trace message.
   *
   * @param text the String object that contains the trace text
   *
   */
  public void trace(String text);


  /**
   * Displays all variables in the given text area.
   *
   * @param textArea the JTextArea object used for display
   *
   */
  public void displayVariables(JTextArea textArea);


  /**
   * Displays all rules in the given text area.
   *
   * @param textArea the JTextArea object used for display
   *
   */
  public void displayRules(JTextArea textArea);


  /**
   * Resets the rule base for another round of inferencing
   * by setting all variable values to null.
   *
   */
  public void reset();


  /**
   * Uses backward chaining to fire rules and find a value for the given goal variable.
   *
   * @param goalVarName the String object that contains the goal
   *
   */
  public void backwardChain(String goalVarName);


  /**
   * Uses forward chaining to set variables and fire rules.
   *
   */
  public void forwardChain();


  /**
   *  Returns a vector of all variables referenced in the consequents of
   *  rules in the rule base.
   *
   * @return the Vector object that contains the variables
   */
  public Vector<?> getGoalVariables();
}
