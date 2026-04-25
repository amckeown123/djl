package rule;

/**
 * The <code>TriangleFuzzySet</code> class defines a fuzzy set in the
 * shape of the triangle.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class TriangleFuzzySet extends FuzzySet {
  private double ptLeft;
  private double ptCenter;
  private double ptRight;


  /**
   * Creates a new Triangle fuzzy set.
   *
   * @param parentVar the ContinuousFuzzyRuleVariable object that is the parent
   * @param name the String object that contains the name
   * @param alphaCut the double value for the alpha cut
   * @param ptLeft the double value of the beginning point of the fuzzy set
   * @param ptCenter the double value of the peak of the fuzzy set
   * @param ptRight the double value of the end point of the fuzzy set
   *
   */
  TriangleFuzzySet(ContinuousFuzzyRuleVariable parentVar, String name, double alphaCut,
                   double ptLeft, double ptCenter, double ptRight) {
    super(FuzzyDefs.TRIANGLE, name, parentVar, alphaCut);

    // Save the original parameters
    this.ptLeft = ptLeft;
    this.ptCenter = ptCenter;
    this.ptRight = ptRight;

    // Set the domain values in the base class!
    domainLo = parentVar.getDiscourseLo();
    domainHi = parentVar.getDiscourseHi();

    // Working variables
    int numberOfValues = 5;
    double[] lclScalarVector = new double[7];
    double[] lclTruthVector = new double[7];

    // Set up the vectors for a trapezoid:
    lclScalarVector[0] = domainLo;
    lclTruthVector[0] = 0.0;
    lclScalarVector[1] = ptLeft;
    lclTruthVector[1] = 0.0;
    lclScalarVector[2] = ptCenter;
    lclTruthVector[2] = 1.0;
    lclScalarVector[3] = ptRight;
    lclTruthVector[3] = 0.0;
    lclScalarVector[4] = domainHi;
    lclTruthVector[4] = 0.0;

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
    parentVar.addSetTriangle(newName, alphaCut, ptLeft, ptCenter, ptRight);
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
   * Retrieves the peak point of the fuzzy set.
   *
   * @return the double value of the peak point
   *
   */
  public double getCenterPoint() {
    return ptCenter;
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
