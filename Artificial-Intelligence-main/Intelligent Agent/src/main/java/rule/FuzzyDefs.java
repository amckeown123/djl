package rule;




/**
 * The <code>FuzzyDefs</code> class contains constants for the FuzzyRule
 * classes.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */

 public class FuzzyDefs extends Object {


  /**
   * The maximum number of scalars and truth values that can be held
   * in a fuzzy set.
   */
  final static public int MAXVALUES = 256;


  /**
   * This is the default value used for alphacut when no explicit
   * alphacut has been set.
   *
   * In this implementation, the default is set to 0.10.
   */
  final static public double AlphaCutDefault = .10;


  /**
   * Correlation method: Product: <i>scale</i> the consequent fuzzy
   * region using the truth of the predicate (the shape of the fuzzy
   * region is preserved):
   * <pre>
   *     a[i] = a[i] * PredicateTruth
   * </pre>
   * <p> Usually used with <i>ProductOr</i> or <i>FuzzyAdd</i>
   * Inference method.
   */
  final static public int PRODUCT = 1;           // Correlation Method


  /**
   * Correlation method: Minimise: <i>truncate</i> the consequent
   * fuzzy region at the truth of the premise (creating a plateau):
   * <pre>
   *     a[i] = minimum( a[i], PredicateTruth )
   * </pre>
   * <p> Usually used with <i>MinMax</i> Inference method.
   */
  final static public int MINIMISE = 2;          // Correlation Method


  /**
   * Correlation method.
   * The value of this constant specifies the correlation method used
   * when the correlation method is not explicitly set by the user.
   *
   * <p> In this implementation, the default is set to Product.
   */
  final static public int CorrelationMethodDefault = PRODUCT;


  /**
   * Defuzzification method.
   */
  final static public int CENTROID = 1;          // Defuzzify Method


  /**
   * Defuzzification method.
   */
  final static public int MAXHEIGHT = 2;         // Defuzzify Method


  /**
   * Defuzzification method.
   * The value of this constant specifies the defuzzification method used
   * when the defuzzification method is not explicitly set by the user.
   *
   * <p> In this implementation, the default is set to Centroid.
   */
  final static public int DefuzzifyMethodDefault = CENTROID;


  /**
   * Inference (implication) method: FuzzyAdd: also known as bounded
   * add; the minimum of 1 and the sum of the membership values:
   * <pre>
   *     minimum( 1, a[i]+b[i] )
   * </pre>
   * <p> Usually used with <i>Product</i> correlation method.
   */
  final static public int FUZZYADD = 1;          // Inference Method


  /**
   * Inference (implication) method: MinMax: the maximum (of the
   * minimum) membership values:
   * <pre>
   *     maximum( a[i], b[i] )
   * </pre>
   * <p> Almost always used with <i>Minimise</i> correlation method.
   */
  final static public int MINMAX = 2;            // Inference Method

  /**
   * Inference method.
   * The value of this constant specifies the inference method used
   * when the inference method is not explicitly set by the user.
   *
   * <p> In this implementation, the default is set to FuzzyAdd.
   */
  final static public int InferenceMethodDefault = FUZZYADD;


  /**
   * Fuzzy set direction for shoulder sets.
   */
  final static public int LEFT = 1;              // Set direction


  /**
   * Fuzzy set direction for shoulder sets.
   */
  final static public int RIGHT = 2;             // Set direction

   /**
   * Ignored hedge, can be used as a place holder in a hedge string.
   */
  final static public char HedgeNull = '.';


  /**
   * Concentrate (3) any fuzzy set.
   */
  final static public char HedgeExtremely = 'E';


  /**
   * Dilute (0.3) any fuzzy set.
   */
  final static public char HedgeSlightly = 'S';


  /**
   * Dilute (0.5) any fuzzy set.
   */
  final static public char HedgeSomewhat = 'M';


  /**
   * Concentrate (2) any fuzzy set.
   */
  final static public char HedgeVery = 'V';

  // fuzzy set types
  final static int SHOULDER = 1;
  final static int TRAPEZOID = 2;
  final static int TRIANGLE = 3;
  final static int WORK = 4;

  // miscellaneous constants
  final static int ContinuousVariable = 1;
  final static int FuzzySet = 2;
  final static int RuleIdInitial = 1;
  final static int VarIdInitial = 1;
  final static int VarIdNull = 0;
  final static String SymbolNull = "Fuzzy_NULL";


  /**
   * Creates a <code>FuzzyDefs</code> object.
   *
   */
  private FuzzyDefs() {}


  /**
   * Retrieves the CorrelationMethod string.
   *
   * @param item the integer tha represents the correlation method
   *
   * @return the String object that represents the correlation method
   *
   */
  final static String CorrelationMethod(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case PRODUCT :
        return tmpItem + ":Product";
      case MINIMISE :
        return tmpItem + ":Minimise";
      default :
        return tmpItem + ":Unrecognized";
    }
  }


  /**
   * Retrieves the DefuzzifyMethod string.
   *
   * @param item the integer that represents the defuzzify method
   *
   * @return the String object that represents the defuzzify method
   *
   */
  final static String DefuzzifyMethod(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case CENTROID :
        return tmpItem + ":Centroid";
      case MAXHEIGHT :
        return tmpItem + ":MaxHeight";
      default :
        return tmpItem + ":Unrecognized";
    }
  }


  /**
   * Retrieves the InferenceMethod string.
   *
   * @param item the integer that represents the inference method
   *
   * @return the String object that represents the inference method
   *
   */
  final static String InferenceMethod(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case FUZZYADD :
        return tmpItem + ":FuzzyAdd";
      case MINMAX :
        return tmpItem + ":MinMax";
      default :
        return tmpItem + ":Unrecognized";
    }
  }


  /**
   * Retrieves the Hedge string.
   *
   * @param item the char that represents the hedge
   *
   * @return the String object that represents the hedge value
   *
   */
  final static String Hedge(char item) {
   // String tmpItem = String.valueOf(item);

    switch (item) {
      case HedgeNull :
        return "";
      case HedgeExtremely :
        return "extremely";
      case HedgeSlightly :
        return "slightly";
      case HedgeSomewhat :
        return "somewhat";
      case HedgeVery :
        return "very";
      default :
        return "UNKNOWN";
    }
  }


  /**
   * Retrieves the Set direction  string.
   *
   * @param item the integer that represents the set direction
   *
   * @return the String object that represents the set direction
   *
   */
  final static String SetDirection(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case LEFT :
        return tmpItem + ":Left";
      case RIGHT :
        return tmpItem + ":Right";
      default :
        return tmpItem + ":Unrecognized";
    }
  }


  /**
   * Retrieves the SetType string.
   *
   * @param item the integer that represents the set type
   *
   * @return the String object that represents the set type
   *
   */
  final static String SetType(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case SHOULDER :
        return tmpItem + ":Shoulder";
      case TRAPEZOID :
        return tmpItem + ":Trapezoid";
      case TRIANGLE :
        return tmpItem + ":Triangle";
      case WORK :
        return tmpItem + ":Work";
      default :
        return tmpItem + ":Unrecognized";
    }
  }


  /**
   * Retrieves the DataType string.
   *
   * @param item the integer that represents the data type
   *
   * @return the String object that represents the data type
   *
   */
  final static String DataType(int item) {
    String tmpItem = Integer.toString(item);

    switch (item) {
      case ContinuousVariable :
        return tmpItem + ":ContinuousVariable";
      case FuzzySet :
        return tmpItem + ":FuzzySet";
      default :
        return tmpItem + ":Unrecognized";
    }
  }
}
