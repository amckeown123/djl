package search;


/**
 * The <code>GeneticSearchObj1</code> class is a specialization of
 * the <code>GeneticSearchObject</code>.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents with Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class GeneticSearchObj1 extends GeneticSearchObject {


  /**
   * Creates a <code>GeneticSearchObj1</code> object.
   */
  public GeneticSearchObj1() {
    chromosomeLength = 20;
    vocabulary = "01";
  }


  /**
   * Computes the fitness value by adding up the number of ones in the
   * chromosome.  For early stopping, use a fitness threshold of 20.
   *
   * @return the computed fitness value
   */
  public double computeFitness() {

    // this is deterministic, if already computed,
    // no sense re-doing work
    if (fitnessComputed) {
      return fitness;
    }
    int size = chromosomeLength;
    double sum = 0.0;

    for (int i = 0; i < size; i++) {
      if (((String) chromosome).charAt(i) == '1') {
        sum = sum + 1.0;
      }
    }
    fitness = sum;
    fitnessComputed = true;
    return fitness;
  }
}
