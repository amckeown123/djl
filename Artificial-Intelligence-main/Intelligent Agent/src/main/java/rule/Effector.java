package rule;


/**
 * The <code>Effector</code> interface allows any class to be
 * called as an effector by a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public abstract interface Effector {


  /**
   * Defines the effector method that must be implemented by
   * every class that can be called as an effector.
   *
   * @param obj the Object that implements an effector
   * @param eName the String object that contains the name of the effector
   * @param args the String object that contains the parameters for the effector
   *
   * @return the long value (usually a status code)
   *
   */
  public long effector(Object obj, String eName, String args);
}
