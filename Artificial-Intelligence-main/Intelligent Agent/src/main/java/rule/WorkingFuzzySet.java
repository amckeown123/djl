package rule;

/**
 * The <code>WorkingFuzzySet</code> class defines a fuzzy set where the
 * solution for a continous variable is developed.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class WorkingFuzzySet extends FuzzySet {
  boolean setEmpty;


  /**
   * Creates a fuzzy work set with the given parameters.
   *
   * @param parentVar the ContinuousFuzzyRuleVariable object that is the parent
   *                   of this fuzzy set
   * @param setName the String object that contains the set name
   * @param alphaCut the double value for the alphacut threshold
   * @param discourseLo the double value for the low end of the discourse
   * @param discourseHi the double value for the high end of the discourse
   */
  WorkingFuzzySet(ContinuousFuzzyRuleVariable parentVar, String setName, double alphaCut,
                  double discourseLo, double discourseHi) {
    super(FuzzyDefs.WORK, setName, parentVar, alphaCut, discourseLo, discourseHi);
    setEmpty = true;
  }


  /**
   * Does nothing.
   *
   * @param cloneName the String object that contains the cloned set name
   *
   */
  void addClone(String cloneName) {

    // Work sets are not cloneable; do nothing!
  }


  /**
   * Checks if the working set is empty.
   *
   * @return the boolean value true if set is empty and false if it is not
   */
  public boolean isEmpty() {
    return setEmpty;
  }


  /**
   * Copies the input set if the current working set is empty, otherwise
   * asserts the given set.
   *
   * @param inputSet the FuzzySet object to be copied or asserted
   */
  void copyOrAssertFzy(FuzzySet inputSet) {
    if (setEmpty) {
      copy(inputSet);
    } else {
      asserting(inputSet);
    }
  }


  /**
   * Correlates the working set with the given input set using the given
   * correlation method and truth value.
   *
   * @param inputSet the FuzzySet object that contains the fuzzy set to be
   *                 correlated with
   * @param corrMethod the integer that represents the correlation method
   * @param truthValue the double truth value
   *
   */
  void correlateWith(FuzzySet inputSet, int corrMethod, double truthValue) {

    switch (corrMethod) {
      case FuzzyDefs.MINIMISE :
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          if (truthValue <= inputSet.getTruthValue(i)) {
            truthVector[i] = truthValue;
          } else {
            truthVector[i] = inputSet.getTruthValue(i);
          }
        }
        break;
      case FuzzyDefs.PRODUCT :
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          truthVector[i] = (inputSet.getTruthValue(i) * truthValue);
        }
        break;
    }
  }


  /**
   * Implicates the current working set to the given fuzzy set using the
   * given infer method.
   *
   * @param inputSet the WorkingFuzzySet object to implicate to
   * @param inferMethod the integer that represents the infer method
   *
   */
  void implicateTo(WorkingFuzzySet inputSet, int inferMethod) {

    switch (inferMethod) {
      case FuzzyDefs.FUZZYADD :
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          double sum = truthVector[i] + inputSet.getTruthValue(i);

          if (sum < 1.0) {
            truthVector[i] = sum;
          } else {
            truthVector[i] = 1.0;
          }
        }
        break;
      case FuzzyDefs.MINMAX :
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          if (truthVector[i] < inputSet.getTruthValue(i)) {
            truthVector[i] = inputSet.getTruthValue(i);
          }
        }
        break;
       }
  }


  /**
   * Resets the working fuzzy set by setting the empty flag to true and setting
   * the truth vector values to 0.0.
   *
   */
  void reset() {
    setEmpty = true;
    for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
      truthVector[i] = 0.0;
    }
  }


  /**
   * Asserts the truth values from the given fuzzy set.
   *
   * @param inputSet the FuzzySet object to be used for the assertion
   *
   */
  void asserting(FuzzySet inputSet) {
    for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
      if (truthVector[i] > inputSet.getTruthValue(i)) {
        truthVector[i] = inputSet.getTruthValue(i);
      }
    }
  }


  /**
   * Copies the truth values from the given fuzzy set.
   *
   * @param inputSet the FuzzySet object to be copied
   *
   */
  void copy(FuzzySet inputSet) {
    for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
      truthVector[i] = inputSet.getTruthValue(i);
    }
    setEmpty = false;
  }
}
