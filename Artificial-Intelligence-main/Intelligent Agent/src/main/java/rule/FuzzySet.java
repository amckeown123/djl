package rule;




/**
 * The <code>FuzzySet</code> class is a abstract class that defines
 * the attributes and methods for a fuzzy set.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public abstract class FuzzySet {
  protected int setType;                            // Set type
  protected String setName;                         // Set name
  protected double alphaCut;                        // AlphaCut
  protected double domainLo;                        // Domain, low end
  protected double domainHi;                        // Domain, high end
  protected double[] truthVector;                   // Truth values
  protected ContinuousFuzzyRuleVariable parentVar;  // Containing variable


  /**
   * Creates a new fuzzy set with the given parameters.
   *
   * @param setType the integer that represents the set type
   * @param setName the String object that contains the name of the set
   * @param parentVar the ContinuousFuzzyRuleVariable that is the parent of this set
   * @param alphaCut the double value for the alpha cut
   * @param domainLo the double value for the low end of the domain
   * @param domainHi the double value for the high end of the domain
   */
  protected FuzzySet(int setType, String setName, ContinuousFuzzyRuleVariable parentVar, double alphaCut, double domainLo, double domainHi) {
    this.setType = setType;
    this.setName = setName;
    this.parentVar = parentVar;
    this.alphaCut = alphaCut;
    this.domainLo = domainLo;
    this.domainHi = domainHi;
    this.truthVector = new double[FuzzyDefs.MAXVALUES];
    for (int i = 0; i < FuzzyDefs.MAXVALUES; i++) {
      truthVector[i] = 0.0;
    }
  }


  /**
   * Creates a new fuzzy set with the given parameters.
   *
   * @param setType the integer that represents the set type
   * @param setName the String object that contains the name of the set
   * @param parentVar the ContinuousFuzzyRuleVariable that is the parent of this set
   * @param alphaCut the double value for the alpha cut
   */
  protected FuzzySet(int setType, String setName, ContinuousFuzzyRuleVariable parentVar, double alphaCut) {
    this(setType, setName, parentVar, alphaCut, 0.0, 0.0);
  }


  /**
   * Retrieve the value of this object as a numeric value.
   *
   * @return   a double, if the value of this object can be represented as a
   *            numeric value
   */
  public double getNumericValue() {
    return defuzzify((parentVar.getRuleBase()).getDefuzzifyMethod());
  }

  /**
   * Adds a clone of the given set.
   *
   * @param setName the String object that contains the name of the set to be cloned
   */
  abstract void addClone(String setName);

   /**
   * Retrieve the id of the variable to which this object refers.
   *
   * @return   the Id of the variable to which this value refers
   */
  public int getReferent() {
    return FuzzyDefs.VarIdNull;
  }


  /**
   * Retrieves the type of this fuzzy set.
   *
   * @return the integer that represent the set type
   *
   */
  public int getSetType() {
    return setType;
  }


  /**
   * Retrieves the name of this fuzzy set.
   *
   * @return the String object that contains the name
   *
   */
  public String getSetName() {
    return setName;
  }


  /**
   * Retrieves the alpha cut value.
   *
   * @return the double value for the alpha cut
   *
   */
  public double getAlphaCut() {
    return alphaCut;
  }


  /**
   * Retrieves the low domain value.
   *
   * @return the double value for the low end of the domain
   *
   */
  public double getDomainLo() {
    return domainLo;
  }


  /**
   * Retrieves the high domain value.
   *
   * @return the double value for the high end of the domain
   *
   */
  public double getDomainHi() {
    return domainHi;
  }


  /**
   * Retrieves the truth values for this fuzzy set.
   *
   * @return the double[] of truth values
   *
   */
  public double[] getTruthValues() {
    return truthVector;
  }


  /**
   * Retrieves a single truth value at the given index.
   *
   * @param index the integer value for the index of the truth value
   *
   * @return the double truth value at the index
   *
   */
  double getTruthValue(int index) {
    return truthVector[index];
  }


  /**
   * Zeroes out truthValues if they are below alphaCut threshold.
   */
  void applyAlphaCut() {
    for (int i = 1; i < FuzzyDefs.MAXVALUES; i++) {
      if (!aboveAlphaCut(truthVector[i])) {
        truthVector[i] = 0.0;
      }
    }
  }


  /**
   * Uses linguistic hedges to modify fuzzy set.
   *
   * @param hedges the String object that contains the set of hedges
   *
   */
  void applyHedges(String hedges) {

    // Apply the hedges in reverse order, ignoring null hedges.
    for (int i = (hedges.length()) - 1; i >= 0; --i) {
      char hedge = hedges.charAt(i);

      switch (hedge) {
        case FuzzyDefs.HedgeNull :
          break;
        case FuzzyDefs.HedgeExtremely :
          applyHedgeConDil(3.0);
          break;
        case FuzzyDefs.HedgeSlightly :
          applyHedgeConDil(0.3);
          break;
        case FuzzyDefs.HedgeSomewhat :
          applyHedgeConDil(0.5);
          break;
        case FuzzyDefs.HedgeVery :
          applyHedgeConDil(2.0);
          break;
      }
    }
  }

   /**
   * Concentrates or dilutes the fuzzy set.
   *
   * @param exp the double value used to concentrate or dilute the fuzzy set
   *
   */
  void applyHedgeConDil(double exp) {
    for (int i = 0; i < FuzzyDefs.MAXVALUES; i++) {
      truthVector[i] = (double) Math.pow(truthVector[i], exp);
    }
  }


  /**
   * Defuzzifies the set using the given defuzzification method.
   *
   * @param defuzzMethod the integer that represent the defuzzification method
   *
   * @return the double value that is the crisp number
   *
   */
  double defuzzify(int defuzzMethod) {

    // Set up local working variables:
    double defuzzedNumber = 0.0;  // The returned number.
    int j = 0;
    int k = 0;
    int m = 0;
    double truthValue1;
    double truthValue2;

    // Apply the alpha cut to the fuzzy set;
    // If the resulting set has a maximum height of zero, there is no
    // point in defuzzing it.  Set the crisp number to zero.
    applyAlphaCut();
    if (getSetHeight() == 0) {
      defuzzedNumber = 0.0;
      return defuzzedNumber;
    }

    // The alpha cut set is non-zero; apply the specified defuzz method:
    switch (defuzzMethod) {

      // Centroid Method
      case FuzzyDefs.CENTROID :
        truthValue1 = 0.0;
        truthValue2 = 0.0;
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          truthValue1 = truthValue1 + truthVector[i] * (double) i;
          truthValue2 = truthValue2 + truthVector[i];
        }
        if (truthValue2 == 0.0) {
          defuzzedNumber = 0.0;
        } else {
          j = (int) (truthValue1 / truthValue2);
          defuzzedNumber = getScalar(j);
        }
        break;

      // Max Height Method
      case FuzzyDefs.MAXHEIGHT :
        truthValue1 = truthVector[0];
        j = 0;
        for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
          if (truthVector[i] > truthValue1) {
            truthValue1 = truthVector[i];
            k = i;
          }
        }
        for (m = k + 1; m < FuzzyDefs.MAXVALUES; ++m) {
          if (truthVector[m] != truthValue1) {
            break;
          }
        }
        j = (int) (((double) m - (double) k) / 2);
        if (j == 0) {
          j = k;
        }
        defuzzedNumber = getScalar(j);
        break;

      // Unknown defuzz method; return zero
      default :
        defuzzedNumber = 0.0;
    }
    return defuzzedNumber;
  }


  /**
   * Retrieves the membership value for the given scalar value.
   *
   * @param scalar the double scalar value
   *
   * @return the double truth value
   *
   */
  double membership(double scalar) {
    double truthValue = 0.0;
    int index;
    double range;

    // If the scalar is lower than this set's low domain value, set the
    // truth value to be the same as the left-edge truth value.
    if (scalar < domainLo) {
      truthValue = truthVector[0];
    } else

    // If the scalar is higher than this set's high domain value, set the
    // truth value to be the same as the right-edge truth value.
    if (scalar > domainHi) {
      truthValue = truthVector[FuzzyDefs.MAXVALUES];
    } else {

      // Calcualte an index into the membership vector and then use the
      // index to pull out the membership value.
      range = domainHi - domainLo;
      index = (int) (((scalar - domainLo) / range) * (FuzzyDefs.MAXVALUES - 1));
      truthValue = truthVector[index];
    }
    return truthValue;
  }


  /**
   * Checks if the truth values is above the alpha cut value.
   *
   * @param truthValue the double truth value to be checked
   *
   * @return the boolean true if truthValue is above alphaCut
   *
   */
  boolean aboveAlphaCut(double truthValue) {
        if (truthValue > alphaCut) {
          return  true;
        } else {
          return  false;
        }
  }



  /**
   * Adjusts the truthValues so that the max is 1.0.
   *
   */
  void normalise() {
    double maxTruthValue = getSetHeight();

    // If the set is entirely false, just return
    // (and prevent divide by zero!)
    if (maxTruthValue == 0.0) {
      return;
    }

    // If the set is already normalised, just return.
    if (maxTruthValue == 1.0) {
      return;
    }
    for (int i = 0; i < FuzzyDefs.MAXVALUES; i++) {
      truthVector[i] = truthVector[i] / maxTruthValue;
    }
  }

   /**
   * Finds the truthValue with maximum value between 0.0 and 1.0.
   *
   * @return the double truth value
   *
   */
  double getSetHeight() {
    double truthValue = truthVector[0];

    for (int i = 0; i < FuzzyDefs.MAXVALUES; i++) {
      if (truthVector[i] > truthValue) {
        truthValue = truthVector[i];
      }
    }
    return truthValue;
  }

  /**
   * Retrives the scalar for the given index within the domain range.
   *
   * @param index the integer value of the index
   *
   * @return the double scalar value
   *
   */
  double getScalar(int index) {
    double range = domainHi - domainLo;
    double width = range / FuzzyDefs.MAXVALUES;
    double scalar = (index * width) + domainLo;

    return scalar;
  }

  /**
   * Retrieves the first and last points in a segment curve interpolated
   * from the working truth vector.
   *
   * @param numberOfValues the integer value for the number of truth values
   * @param scalarVector the double[] scalar vector
   * @param aTruthVector the double[] truth vector
   *
   * @return the int[] that contains the first and last points of the segment
   *
   */
  int[] segmentCurve(int numberOfValues, double[] scalarVector, double[] aTruthVector) {

    
	@SuppressWarnings("unused")
	boolean normalSet = false;  // Is set normal?
    int point = 256;            // Working curve point
    double[] tmpTruthVector = new double[FuzzyDefs.MAXVALUES + 1];  // Working truth vector
    double widthDomain = domainHi - domainLo;
    int[] tmpSegs = new int[]{ -1, FuzzyDefs.MAXVALUES };  // First & last user points

    // Fill in the truth vector for this set:
    // Initialise the working truth vector
    for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
      tmpTruthVector[i] = -1;
    }

    // Determine where in the working truth vector to place the
    // user's designated truth value given the truth value's
    // associated scalar.
    for (int i = 0; i < numberOfValues; ++i) {
      point = (int) (((scalarVector[i] - domainLo) / widthDomain) * FuzzyDefs.MAXVALUES);
      tmpTruthVector[point] = aTruthVector[i];
      if (tmpSegs[0] == -1) {
        tmpSegs[0] = point;  // The first user point
      }
      if (aTruthVector[i] == 1.0) {
        normalSet = true;
      }
    }
    tmpSegs[1] = point;  // The last user point

    // Interpolate the curve in the working truth vector between
    // the user's specified points.
    if (tmpTruthVector[0] == -1) {
      tmpTruthVector[0] = 0.0;
    }
    vectorInterpret(tmpTruthVector);

    // Now update this set's actual truth vector by copying the
    // working truth vector into it.
    for (int i = 0; i < FuzzyDefs.MAXVALUES; ++i) {
      truthVector[i] = tmpTruthVector[i];
    }
    return tmpSegs;  // Return the first & last user points
  }


  /**
   * Fills in the bits in the truth vector.
   *
   * @param aTruthVector the double[] truth vector
   *
   */
  void vectorInterpret(double[] aTruthVector) {

    int i, j, k;
    int point1, point2;
    double factor;
    double seg1, seg2;

    // Scan the input truth vector, looking for values that the user
    // has entered.  When a user value is found, fill in the bits of
    // the vector between that point and the user's previous point (or
    // the start of the vector).
    i = 0;
    j = 0;
    skipIt:
    for (;;) {
      j++;

      // If we have looked at all values, return to caller.
      if (j > FuzzyDefs.MAXVALUES) {
        return;
      }

      // Skip over all values pre-initialized to -1, looking for
      // a value that the user has entered (which must, of course,
      // be between 0.0 and 1.0)
      if (aTruthVector[j] == -1) {
        continue skipIt;
      }

      // We found a value other than -1;
      // we now want to fill in the truth vector from where we
      // started the scan up to here.
      point1 = i + 1;
      point2 = j;
      for (k = point1; k < point2 + 1; ++k) {
        seg1 = k - i;
        seg2 = j - i;
        factor = seg1 / seg2;
        if (k > FuzzyDefs.MAXVALUES) {
          return;
        }
        aTruthVector[k] = aTruthVector[i] + (factor * (aTruthVector[j] - aTruthVector[i]));
      }

      // Ok, we've filled in that bit.
      // See if we have scanned the entire vector.
      i = j;
      if (i >= FuzzyDefs.MAXVALUES) {
        return;
      }

      // Got some vector left.
      // Go scan past -1 values, looking for valid truth values
      // or the end of the vector.
      continue skipIt;
    }
  }


  /**
   * Retrieves a string describing the contents of the object.
   *
   * @return   a String containing the current contents of the object
   */
  public String toString() {
    return parentVar.getName() + "." + setName + "(" + getNumericValue() + ") ";
  }
}
