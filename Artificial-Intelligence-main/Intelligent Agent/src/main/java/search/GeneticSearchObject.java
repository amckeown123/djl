package search;

import java.util.*;


/**
 * The <code>GeneticSearchObject</code> class is used to represent a
 * member of the population being searched or optimized using a
 * genetic search algorithm.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class GeneticSearchObject extends Object {
  protected GeneticSearch searchAgent;        // the parent search agent
  protected String chromosome;                // the chromosome string
  protected String vocabulary = "01";         // the chromosome vocabulary
  protected int chromosomeLength = 10;        // the length of the chromosome
  protected double fitness = 0.0;             // the raw fitness value
  protected boolean fitnessComputed = false;  // true if fitness computed
  protected double crossoverRate = 0.65;      // the prob of doing a crossover
  protected double mutationRate = 0.008;      // the prob of doing a mutation


  /**
   * Creates a <code>GeneticSearchObject</code>.
   */
  public GeneticSearchObject() {}


  /**
   * Retrieves the fitness value for this population member.
   *
   * @return the fitness value
   */
  public double getFitness() {
    return fitness;
  }


  /**
   * Returns the fitness value for this population member.
   *
   * @return the computed fitness value for this population member
   */
  public double computeFitness() {
    return fitness;
  }


  /**
   * Retrieves a random chromosome for this population member.
   *
   * @return the String that represents a chromosome
   */
  public String getRandomChromosome() {
    return generateRandomChromosome();
  }


  /**
   * Sets the chromosome for this population member and computes the
   * member's fitness.
   *
   * @param chromosome  the String that represents the chromosome
   */
  public void setChromosome(String chromosome) {
    this.chromosome = chromosome;
    computeFitness();
  }


  /**
   * Retrieves the chromosome for this population member.
   *
   * @return the String that represents a chromosome
   */
  public String getChromosome() {
    return chromosome;
  }


  /**
   * Retrieves the length of the chromosome.
   *
   * @return the length of the chromosome.
   */
  public int getChromosomeLength() {
    return chromosomeLength;
  }


  /**
   * Retrieve the vocabulary used for the chromosomes, which is usually the
   * alphabet "01" or "abc".
   *
   * @return the String that represents the vocabulary alphabet
   */
  public String getVocabulary() {
    return vocabulary;
  }


  /**
   * Sets the crossover rate for use by any crossover operators.
   *
   * @param rate the double value of the crossover rate
   */
  public void setCrossoverRate(double rate) {
    crossoverRate = rate;
  }


  /**
   * Retrieves the crossover rate used by any crossover operators.
   *
   * @return the crossover rate
   */
  public double getCrossoverRate() {
    return crossoverRate;
  }


  /**
   * Sets the mutation rate for use by any mutation operators.
   *
   * @param rate the double value of the crossover rate
   */
  public void setMutationRate(double rate) {
    mutationRate = rate;
  }


  /**
   * Retrieves the mutation rate used by any mutation operators.
   *
   * @return the mutation rate
   */
  public double getMutationRate() {
    return mutationRate;
  }


  /**
   * Generates a random chromosome for this population member, based on the
   * chromosome length and vocabulary.
   *
   * @return the generated chromosome string
   */
  public String generateRandomChromosome() {
    String chromosome = new String(new char[chromosomeLength]);
    StringBuffer buf = new StringBuffer(chromosome);
    int size = chromosomeLength;
    int vocabSize = vocabulary.length();

    for (int i = 0; i < size; i++) {
      double rand = Math.random();
      int bitPos = (int) (rand * vocabSize);  // get a random value

      if (bitPos == vocabSize) {
        bitPos = bitPos - 1;
      }
      char newValue = vocabulary.charAt(bitPos);

      buf.setCharAt(i, newValue);
    }
    return buf.toString();
  }


  /**
   * Retrieves a table of operator names and fitness values. The sum of the
   * fitness values must equal 100.  This method is only called once from
   * the GeneticSearch object's init() method.
   *
   * @return the hashtable of operators and fitness values
   */
  public Hashtable<String, Number> getOperatorFitness() {
    Hashtable<String, Number> operatorFitness = new Hashtable<String, Number>();

    operatorFitness.put("onePointCrossoverAndMutate", 40);
    operatorFitness.put("onePointCrossover", 30);
    operatorFitness.put("mutate", 30);
    return operatorFitness;
  }


  /**
   * Generates a string from the chromosome and fitness values.
   *
   * @return a string formatted for display purposes
   */
  public String toString() {
    return chromosome + " : " + String.valueOf(fitness);
  }


  /**
   * Converts a binary code into a double integer.
   *
   * @param binCode a String of 1s and 0s with MSB being char[0]
   *                and LSB being char[n-1]
   *
   * @return the value of the string
   */
  public double binaryToInteger(String binCode) {
 //   double temp;
    double value = 0;
    int inLength = binCode.length();
    char bit;

    for (int i = 0; i < inLength; i++) {
      bit = binCode.charAt(i);
      if (bit == '1') {
        value += Math.pow((double) 2, (double) (inLength - 1 - i));
      }
    }
    return value;
  }
}
