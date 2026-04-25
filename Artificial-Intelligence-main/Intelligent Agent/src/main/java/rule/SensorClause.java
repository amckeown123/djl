package rule;

import java.util.*;



/**
 * The <code>SensorClause</code> class implements a sensor clause that
 * can be used as the antecedent in a rule.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class SensorClause extends Clause {
  Sensor object;
  String sensorName;


  /**
   * Creates a <code>SensorClause</code> object with the given parameters.
   *
   * @param sName the String object that contains the name of the sensor
   * @param Lhs the RuleVariable object to be set in this sensor clause
   *
   */
  SensorClause(String sName, RuleVariable Lhs) {
    lhs = Lhs;
    cond = new Condition("=");
    rhs = " ";
    lhs.addClauseRef(this);
    ruleRefs = new Vector<Rule>();
    truth = null;
    consequent = false;  // must be antecedent
    sensorName = sName;
  }


  /**
   * Displays information about this sensor clause.
   *
   * @return the String object for display.
   *
   */
  public String display() {
    return "sensor(" + sensorName + "," + rhs + ") ";
  }


  /**
   * Checks the truth value returned from the sensor.
   *
   * @return the Boolean object returned from the sensor
   *
   */
  public Boolean check() {
    if (consequent == true) {
      return null;
    }
    if (lhs.value == null) {
      BooleanRuleBase rb = ((Rule) ruleRefs.firstElement()).rb;

      object = (Sensor) (rb.getSensorObject(sensorName));
      truth = object.sensor(this, sensorName, lhs);
    }
    return truth;
  }
}

