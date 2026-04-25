package rule;


/**
 * The <code>TrapezoidFuzzySet</code> class defines a fuzzy set in the
 * shape of the trapezoid.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class TrapezoidFuzzySet extends FuzzySet {
  private double ptLeft;
  private double ptLeftCore;
  private double ptRightCore;
  private double ptRight;


  /**
   * Creates a new Trapezoid fuzzy set.
   *
   * @param parentVar the ContinuousFuzzyRuleVariable object that is the parent
   * @param name the String object that contains the name
   * @param alphaCut the double value for the alpha cut
   * @param ptLeft the double value of the beginning point of the fuzzy set
   * @param ptLeftCore the double value of the beginning point of the plateau
   * @param ptLeftCore the double value of the end point of the plateau
   * @param ptRight the double value of the end point of the fuzzy set
   *
   */
  TrapezoidFuzzySet(ContinuousFuzzyRuleVariable parentVar, String name, double alphaCut,
                    double ptLeft, double ptLeftCore, double ptRightCore, double ptRight) {
    super(FuzzyDefs.TRAPEZOID, name, parentVar, alphaCut);

    // Save the original parameters
    this.ptLeft = ptLeft;
    this.ptLeftCore = ptLeftCore;
    this.ptRightCore = ptRightCore;
    this.ptRight = ptRight;

    // Set the domain values in the base class!
    domainLo = parentVar.getDiscourseLo();
    domainHi = parentVar.getDiscourseHi();

    // Working variables
    int numberOfValues = 6;
    double[] lclScalarVector = new double[7];
    double[] lclTruthVector = new double[7];

    // Set up the vectors for a trapezoid:
    lclScalarVector[0] = domainLo;
    lclTruthVector[0] = 0.0;
    lclScalarVector[1] = ptLeft;
    lclTruthVector[1] = 0.0;
    lclScalarVector[2] = ptLeftCore;
    lclTruthVector[2] = 1.0;
    lclScalarVector[3] = ptRightCore;
    lclTruthVector[3] = 1.0;
    lclScalarVector[4] = ptRight;
    lclTruthVector[4] = 0.0;
    lclScalarVector[5] = domainHi;
    lclTruthVector[5] = 0.0;

    // Fill in the truth vector for this set:
    segmentCurve(numberOfValues, lclScalarVector, lclTruthVector);
  }


  /**
   * Creates a clone of this fuzzy set and adds it to the parent variable.
   *
   * @param newName the String object that contains the name of the clone
   *
   */
  void addClone(String newName) {

    // Using the original parameters, create another set like this one and
    // add it to the containing variable's set list.
    parentVar.addSetTrapezoid(newName, alphaCut, ptLeft, ptLeftCore, ptRightCore, ptRight);
  }


  /**
   * Retrieves the beginning point of the fuzzy set.
   *
   * @return the double value of the beginning point
   *
   */
  public double getLeftPoint() {
    return ptLeft;
  }


  /**
   * Retrieves the beginning point of the plateau.
   *
   * @return the double value of the beginning point of the plateau
   *
   */
  public double getLeftCorePoint() {
    return ptLeftCore;
  }


  /**
   * Retrieves the end point of the plateau.
   *
   * @return the double value of the end point of the plateau
   *
   */
  public double getRightCorePoint() {
    return ptRightCore;
  }


  /**
   * Retrieves the end point of the fuzzy set.
   *
   * @return the double value of the end point
   *
   */
  public double getRightPoint() {
    return ptRight;
  }
}
