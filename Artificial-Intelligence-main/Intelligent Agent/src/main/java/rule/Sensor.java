package rule;


/**
 * The <code>Sensor</code> interface allows any class to be
 * called as an sensor in a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public abstract interface Sensor {


  /**
   * Defines the sensor method that must be implemented by
   * every class that can be called as a sensor.
   *
   * @param obj the Object that implements an sensor
   * @param sName the String object that contains the name of the sensor
   * @param lhs the RuleVariable object that is assigned a value from the
   *            sensor
   *
   * @return the Boolean object
   *
   */
  public Boolean sensor(Object obj, String sName, RuleVariable lhs);
}

