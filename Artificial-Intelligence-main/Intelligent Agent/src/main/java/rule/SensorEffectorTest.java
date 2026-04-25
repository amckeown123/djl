package rule;

public class SensorEffectorTest implements Sensor, Effector {

  public SensorEffectorTest() {
  }

  public static void main(String[] args) {
      SensorEffectorTest seTest = new SensorEffectorTest() ;
      seTest.test() ;

  }

  
  private void test() {

  BooleanRuleBase rb = new BooleanRuleBase("test") ;
  initVehiclesRuleBase(rb) ;


    // should be a Mini-Van
    rb.setVariableValue("vehicle", null);
    rb.setVariableValue("vehicleType", null);
//    rb.setVariableValue("size", "medium");
//    rb.setVariableValue("num_wheels", "4");
//    rb.setVariableValue("num_doors", "3");
 //   rb.setVariableValue("motor", "yes");

     rb.initializeFacts(); 
     rb.forwardChain();

   
}

/**
   * Method initVehiclesRuleBase
   *
   * @param rb the BooleanRuleBase object
   *
   */

public void initVehiclesRuleBase(BooleanRuleBase rb) {
    RuleVariable vehicle = new RuleVariable(rb, "vehicle");

    vehicle.setLabels("Bicycle Tricycle MotorCycle Sports_Car Sedan MiniVan Sports_Utility_Vehicle");
    vehicle.setPromptText("What kind of vehicle is it?");
    RuleVariable vehicleType = new RuleVariable(rb, "vehicleType");

    vehicleType.setLabels("cycle automobile");
    vehicleType.setPromptText("What type of vehicle is it?");
    RuleVariable size = new RuleVariable(rb, "size");

    size.setLabels("small medium large");
    size.setPromptText("What size is the vehicle?");
    RuleVariable motor = new RuleVariable(rb, "motor");

    motor.setLabels("yes no");
    motor.setPromptText("Does the vehicle have a motor?");
    RuleVariable num_wheels = new RuleVariable(rb, "num_wheels");

    num_wheels.setLabels("2 3 4");
    num_wheels.setPromptText("How many wheels does it have?");
    RuleVariable num_doors = new RuleVariable(rb, "num_doors");

    num_doors.setLabels("2 3 4");
    num_doors.setPromptText("How many doors does it have?");

    // Note: at this point all variables values are NULL
 //   Condition cEquals = new Condition("=");
 //   Condition cNotEquals = new Condition("!=");
 //   Condition cLessThan = new Condition("<");

    // define rules
 //   Rule Bicycle = new Rule(rb, "bicycle", new Clause[]{ new Clause(vehicleType, cEquals, "cycle"),
 //                                                        new Clause(num_wheels, cEquals, "2"),
 //                                                        new Clause(motor, cEquals, "no") },
 //                                                       new Clause(vehicle, cEquals, "Bicycle"));
 //   Rule Tricycle = new Rule(rb, "tricycle", new Clause[]{ new Clause(vehicleType, cEquals, "cycle"),
 //                                                          new Clause(num_wheels, cEquals, "3"),
 //                                                          new Clause(motor, cEquals, "no") },
 //                                                          new Clause(vehicle, cEquals, "Tricycle"));
 //   Rule Motorcycle = new Rule(rb, "motorcycle", new Clause[]{ new Clause(vehicleType, cEquals, "cycle"),
 //                                                              new Clause(num_wheels, cEquals, "2"),
 //                                                             new Clause(motor, cEquals, "yes") },
 //                                                              new Clause(vehicle, cEquals, "Motorcycle"));
 //   Rule SportsCar = new Rule(rb, "sportsCar", new Clause[]{ new Clause(vehicleType, cEquals, "automobile"),
  //                                                           new Clause(size, cEquals, "small"),
  //                                                           new Clause(num_doors, cEquals, "2") },
  //                                                           new Clause(vehicle, cEquals, "Sports_Car"));
 //   Rule Sedan = new Rule(rb, "sedan", new Clause[]{ new Clause(vehicleType, cEquals, "automobile"),
 //                                                    new Clause(size, cEquals, "medium"),
 //                                                    new Clause(num_doors, cEquals, "4") },
 //                                                    new Clause(vehicle, cEquals, "Sedan"));
 //   Rule MiniVan = new Rule(rb, "miniVan", new Clause[]{ new Clause(vehicleType, cEquals, "automobile"),
 //                                                        new Clause(size, cEquals, "medium"),
 //                                                        new Clause(num_doors, cEquals, "3") },
 //                                                        new Clause(vehicle, cEquals, "MiniVan"));
 //   Rule SUV = new Rule(rb, "SUV", new Clause[]{ new Clause(vehicleType, cEquals, "automobile"),
 //                                                new Clause(size, cEquals, "large"),
 //                                                new Clause(num_doors, cEquals, "4") },
 //                                                new Clause(vehicle, cEquals, "Sports_Utility_Vehicle"));
 //   Rule Cycle = new Rule(rb, "Cycle", new Clause(num_wheels, cLessThan, "4"), new Clause(vehicleType, cEquals, "cycle"));
 //   Rule Automobile = new Rule(rb, "Automobile", new Clause[]{ new Clause(num_wheels, cEquals, "4"),
 //                                                              new Clause(motor, cEquals, "yes") },
 //                                                              new Clause(vehicleType, cEquals, "automobile"));


    // add to rule base for sensors, effectors, and facts ...
    // 
//  Rule EffectorTest = new Rule(rb, "EffectorTest",
//     new Clause(num_wheels, cEquals, "4"),
//     new EffectorClause("display", "It has 4 wheels!")) ;

   rb.addEffector( this, "display") ;  // test it

  RuleVariable sensor_var = new RuleVariable(rb, "sensor_var") ;
   sensor_var.setLabels("2 3 4") ;
   sensor_var.setPromptText("What does the sensor say?") ;
   rb.variableList.put(sensor_var.name,sensor_var) ;

//  Rule SensorTest = new Rule(rb, "SensorTest",
//     new Clause[] { new Clause(num_wheels, cEquals, "4"),
//     new SensorClause("sensor_var", sensor_var)},
//     new EffectorClause("display", "It's an automobile!!!!")) ;

   rb.addSensor( this, "sensor_var") ;  // test it

//  Fact f1 = new Fact(rb, "f1",
//     new Clause(num_wheels, cEquals, "4")) ;
//  Fact f2 = new Fact(rb, "f2",
//     new SensorClause("sensor_var", sensor_var)) ;
//  Fact f3 = new Fact(rb, "f3",
//     new EffectorClause("display", "Just the facts man !")) ;


  }

  // sample effector always displays its input arg
  // and returns 0
  public long display(String arg) {
     System.out.println("Effector display = " + arg) ;
     return 0 ;
  }

  // sample sensor always return true
  public Boolean sensor_var() {
    return true ;
  }

  public long effector(Object obj, String eName, String args) {
       System.out.println("Effector " + eName + " called with args " + args) ;
       return 1 ;
  }

  public Boolean sensor(Object obj, String sName, RuleVariable lhs) {
      System.out.println("Sensor " + sName + " called with lhs = " + lhs);
      return  true ;
  }
}