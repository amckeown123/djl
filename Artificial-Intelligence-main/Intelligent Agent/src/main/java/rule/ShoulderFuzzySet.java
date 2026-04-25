package rule;


/**
 * The <code>ShoulderFuzzySet</code> class defines a fuzzy set that either
 * slopes upward then plateaus or plateaus before sloping downward.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class ShoulderFuzzySet extends FuzzySet {
  private double ptBeg;  // Left point
  private double ptEnd;  // Right point
  private int setDir;    // Left or Right


  /**
   * Creates a new Shoulder fuzzy set.
   *
   * @param parentVar the ContinuousFuzzyRuleVariable object that is the parent
   * @param name the String object that contains the name
   * @param alphaCut the double value for the alpha cut
   * @param ptBeg the double value of the beginning point of the fuzzy set
   * @param ptEnd the double value of the end point of the fuzzy set
   * @param setDirection the integer that represents the direction of the shoulder
   *
   */
  ShoulderFuzzySet(ContinuousFuzzyRuleVariable parentVar, String name, double alphaCut,
                   double ptBeg, double ptEnd, int setDirection) {
    super(FuzzyDefs.SHOULDER, name, parentVar, alphaCut);

    // Save the original parameters
    this.ptBeg = ptBeg;
    this.ptEnd = ptEnd;
    this.setDir = setDirection;

    // Set the domain values in the base class!
    domainLo = parentVar.getDiscourseLo();
    domainHi = parentVar.getDiscourseHi();

    // Working variables
    int     numberOfValues = 4;
    double[] lclScalarVector = new double[5];
    double[] lclTruthVector = new double[5];

    // Set up the vectors for a shoulder
    if (setDirection == FuzzyDefs.LEFT) {
      lclScalarVector[0] = domainLo;
      lclTruthVector[0] = 1.0;
      lclScalarVector[1] = ptBeg;
      lclTruthVector[1] = 1.0;
      lclScalarVector[2] = ptEnd;
      lclTruthVector[2] = 0.0;
      lclScalarVector[3] = domainHi;
      lclTruthVector[3] = 0.0;
    } else {
      lclScalarVector[0] = domainLo;
      lclTruthVector[0] = 0.0;
      lclScalarVector[1] = ptBeg;
      lclTruthVector[1] = 0.0;
      lclScalarVector[2] = ptEnd;
      lclTruthVector[2] = 1.0;
      lclScalarVector[3] = domainHi;
      lclTruthVector[3] = 1.0;
    }

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
    parentVar.addSetShoulder(newName, alphaCut, ptBeg, ptEnd, setDir);
  }


  /**
   * Retrieves the beginning point of the fuzzy set.
   *
   * @return the double value of the beginning point
   *
   */
  public double getLeftPoint() {
    return ptBeg;
  }


  /**
   * Retrieves the end point of the fuzzy set.
   *
   * @return the double value of the end point
   *
   */
  public double getRightPoint() {
    return ptEnd;
  }


  /**
   * Retrieves the direction of the fuzzy set.
   *
   * @return the String object that contains the direction
   *
   */
  public String getSetDirection() {
    return FuzzyDefs.SetDirection(setDir);
  }
}
