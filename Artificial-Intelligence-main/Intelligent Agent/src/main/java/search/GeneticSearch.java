package search;

import java.util.*;
import javax.swing.*;


/**
 * The <code>GeneticSearch</code> class implements the genetic search algorithm
 * as follows:
 * <ol>
 * <li> initialize the population
 * <li> evaluate each chromosome and insert into the population
 * <li> create new chromosomes using crossover/mutation operators
 * <li> replace  population with next generation
 * <li> loop until time is up or other stopping condition is reached
 * </ol>
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class GeneticSearch extends Object {

  // instance Variables
  protected Class<?> geneticObjectClass;
  protected Vector<GeneticSearchObject> population;                     // ordered by fitness
  protected Hashtable<?, ?> operatorFitness = null;  // used for operator selection
  protected int chromosomeLength = 10;             // passed in by geneticObject class
  protected String vocabulary = "01";              // passed in by geneticObject class

  // user parameters
  protected String geneticObjectClassName = "search.GeneticSearchObj1";
  protected int maxNumPasses = 50;                 // stopping condition
  protected double fitnessThreshold = 100000.0;    // alt stopping condition
  protected int populationSize = 20;
  protected int replacementSize = 20;
  protected double crossoverRate = 0.65;
  protected double mutationRate = 0.008;
  protected double maxFitness = Double.MAX_VALUE;  // raw fitness
  protected double minFitness = 0.0;               // raw fitness
  protected double avgFitness = 0.0;               // raw fitness
  protected double totalFitness = 0.0;             // raw fitness
  protected double[] summedFitness;                // used by roulette wheel selection
  protected int numPasses = 0;
  protected JTextArea traceTextArea;


  /**
   * Method setTextArea
   *
   * @param j the JTextArea object
   *
   */
  public void setTextArea(JTextArea j) {
    traceTextArea = j;
  }

  protected boolean debugOn = false;  // enable printlns


  /**
   * Create a genetic search agent.
   */
  public GeneticSearch() {}


  /**
   * Sets the debug flag.
   *
   * @param debugOn the boolean value for the debug flag
   */
  public void setDebugOn(boolean debugOn) {
    this.debugOn = debugOn;
  }


  /**
   * Initalizes the popualation.
   *
   */
  public void init() {

    // reset and initialize counters, performance metrics,  etc.
    numPasses = 0;  // reset generation counter
    maxFitness = 0.0;
    minFitness = 0.0;
    avgFitness = 0.0;
    totalFitness = 0.0;
    population = new Vector<GeneticSearchObject>();
    summedFitness = new double[populationSize + 1];  // used by roulette wheel
    replacementSize = populationSize;
    try {

      // do some special one-time things to get setup
      geneticObjectClass = Class.forName(geneticObjectClassName);  // load the class
      GeneticSearchObject dummy = new GeneticSearchObject();
      
      if (operatorFitness == null) {

        // get default operators and fitness values
        operatorFitness = dummy.getOperatorFitness();
      }
      chromosomeLength = dummy.getChromosomeLength();
      vocabulary = dummy.getVocabulary();

      // now, initialize the population
      while (population.size() < populationSize) {
        String chromosome = dummy.getRandomChromosome();
        GeneticSearchObject newOne = createChild(chromosome);

        if (newOne != null) {
          insertIntoPopulation(newOne, getPopulation());
        }
      }
    } catch (ClassNotFoundException e1) {
      System.out.println("Error:" + e1.toString());
    }


  }


  /**
   * Processes one generation by evaluating each chromosome and insert it
   * into the population, creating new chromosomes and members using
   * crossover/mutation operators, and deleting members of the old population
   * to make room for new members.
   */
  public void processOneGeneration() {
    evaluatePopulation();  // ask each member to compute its fitness
    Vector<GeneticSearchObject> newPopulation = createNewMembers();  // generate new members

    integratePopulation(newPopulation);  // replace members
    numPasses++;
  }


  /**
   * Does a complete genetic search by initializing the population then
   * processing the population, one generation at a time until the maximum
   * number of passes are made or the maximum fitness threshold is reached.
   */
  public void search() {
    init();  // initialize the population
    trace("Starting genetic search\n");
    while ((numPasses < maxNumPasses) && (maxFitness < fitnessThreshold)) {
      processOneGeneration();
      trace("Generation[" + numPasses + "] " + population.elementAt(0).toString() + "\n");
    }
    trace("Genetic search complete\n");
    displayPopulation();
  }


  /**
   * Sets the name of the class of genetic objects to be searched.
   *
   * @param aClassName  the String that represents the fully-specified
   *                    (package prefixed) class name of the genetic
   *                    objects to be searched
   */
  public void setGeneticObjectClassName(String aClassName) {
    geneticObjectClassName = aClassName;
  }


  /**
   * Returns the name of the genetic object class.
   *
   * @return the class name of the genetic object class
   */
  public String getGeneticObjectClassName() {
    return geneticObjectClassName;
  }


  /**
   * Sets the population size.
   *
   * @param size the integer value that represents the size of the population
   */
  public void setPopulationSize(int size) {
    populationSize = size;
  }


  /**
   * Gets the population size.
   *
   * @return the population size
   */
  public int getPopulationSize() {
    return populationSize;
  }


  /**
   * Gets the entire population.
   *
   * @return <code>null</code> if the population does not exist. Otherwise,
   * returns the entire population vector.
   */
  @SuppressWarnings("unchecked")
public Vector<GeneticSearchObject> getPopulation() {
    if (population == null) {
      return null;
    } else {
      return (Vector<GeneticSearchObject>) population.clone();
    }
  }


  /**
   * Sets the number of population members to replace in each generation.
   *
   * @param size the number of members to be replaced
   */
  public void setReplacementSize(int size) {
    replacementSize = size;
  }


  /**
   * Gets the replacement size.
   *
   * @return the number of members created or replaced each cycle
   */
  public int getReplacementSize() {
    return replacementSize;
  }


  /**
   * Get the chromosome vocabulary.
   *
   * @return the bit string that represents chromosome vocabulary
   */
  public String getVocabulary() {
    return vocabulary;
  }


  /**
   * Gets the number of generations that have been searched.
   *
   * @return the current generation number
   */
  public int getNumPasses() {
    return numPasses;
  }


  /**
   * Sets the maximum number of generations to search.
   *
   * @param num the maximum number of generations to search
   */
  public void setMaxNumPasses(int num) {
    maxNumPasses = num;
  }


  /**
   * Gets the maximum number of generations to search.
   *
   * @return the maximum number of generations to search
   */
  public int getMaxNumPasses() {
    return maxNumPasses;
  }


  /**
   * Sets the raw fitness threshold which is used to halt the search if the
   * raw fitness value exceeds this threshold.
   *
   * @param thresh  the fitness threshold
   */
  public void setFitnessThreshold(double thresh) {
    fitnessThreshold = thresh;
  }


  /**
   * Gets the raw fitness threshold value.
   *
   * @return the fitness threshold
   */
  public double getFitnessThreshold() {
    return fitnessThreshold;
  }


  /**
   * Sets the crossoverRate which is used when a crossover operator is
   * selected to determine whether a crossover will actually be performed.
   *
   * @param rate the double value for the crossover rate
   */
  public void setCrossoverRate(double rate) {
    crossoverRate = rate;
  }


  /**
   * Gets the crossover rate.
   *
   * @return the crossover rate
   */
  public double getCrossoverRate() {
    return crossoverRate;
  }


  /**
   * Sets the mutation rate which is used when a mutate operator is selected
   * to determine whether each bit is mutated or not.
   *
   * @param rate the double value for the mutation rate
   */
  public void setMutationRate(double rate) {
    mutationRate = rate;
  }


  /**
   * Get the current mutation rate.
   *
   * @return the mutation rate
   */
  public double getMutationRate() {
    return mutationRate;
  }


  /**
   * Gets the average raw fitness value for the current population.
   *
   * @return the average raw fitness value
   */
  public double getAvgFitness() {
    return avgFitness;
  }


  /**
   * Gets the minimum raw fitness value for the current population.
   *
   * @return the minimum raw fitness value
   */
  public double getMinFitness() {
    return minFitness;
  }


  /**
   * Gets the maximum raw fitness value for the current population.
   *
   * @return the maximum raw fitness value
   */
  public double getMaxFitness() {
    return maxFitness;
  }


  /**
   * Gets the length of the chromosome for the current population.
   *
   * @return the chromosome length
   */
  public int getChromosomeLength() {
    return chromosomeLength;
  }


  /**
   * Sets table of fitness operators and fitness values that are used
   * during reproduction.
   *
   * @param opFitness a Hashtable of fitness operators and fitness values
   *                  (selection probabilities). The sum of the fitness values
   *                  should equal 100.
   */
  public void setOperatorFitness(Hashtable<?, ?> opFitness) {
    operatorFitness = opFitness;
  }


  /**
   * Gets the supported fitness operators and their fitness values.
   *
   * @return the fitness operator table
   */
  public Hashtable<?, ?> getOperatorFitness() {
    return operatorFitness;
  }


  /**
   * Creates and initializes new population members based on newly-derived
   * chromosomes.
   *
   *  @param chromosome a String array of chromosomes
   *
   *  @return an array of <code>GeneticSearchObjects</code>
   */
  public GeneticSearchObject[] createChildren(String[] chromosome) {
    GeneticSearchObject[] children = new GeneticSearchObject[chromosome.length];

    // create the children
    GeneticSearchObject child1 = createChild(chromosome[0]);

    children[0] = child1;
    if (chromosome.length == 2) {
      GeneticSearchObject child2 = createChild(chromosome[1]);

      children[1] = child2;
    }
    return children;
  }


  /**
   * Creates and initializes a child genetic object from a chromosome if
   * it is unique or, if duplicates are allowed, even if it is not unique.
   *
   * @param chromosome the String representation of the child chromosome
   *
   * @return  the fully-initialized child population member or null, if the
   *          child could not be created
   */
  public GeneticSearchObject createChild(String chromosome) {
    GeneticSearchObject child = null;

    // now instantiate the child object
    try {
      child = new GeneticSearchObject();;
      child.setChromosome(chromosome);        // set its chromosome value
      child.setCrossoverRate(crossoverRate);  // pass through params
      child.setMutationRate(mutationRate);    // pass through params
    } catch (Exception e) {
      System.out.println("createChild: exception " + e);
    }
    return child;
  }


  /**
   * Evaluates each population member based on fitness.
   */
  protected void evaluatePopulation() {
    double rawFitness = 0.0;

    totalFitness = 0.0;
    for (int i = 0; i < populationSize; i++) {

      // Note: It is up to the genetic object whether the computeFitness
      // method causes a re-calculation of its fitness, or if it only
      // returns the fitness value computed the first time. For performance
      // it doesn't make sense to keep recomputing a fixed value.
      rawFitness = population.elementAt(i).computeFitness();

      // pre-compute summed fitness values for roulette wheel selection
      if (i == 0) {
        summedFitness[0] = rawFitness;
      } else {
        summedFitness[i] = rawFitness + summedFitness[i - 1];
      }
      if (rawFitness < minFitness) {
        minFitness = rawFitness;
      }
      if (rawFitness > maxFitness) {
        maxFitness = rawFitness;
      }
      totalFitness += rawFitness;
    }
    avgFitness = totalFitness / populationSize;
  }


  /**
   * Generates a number of population members through the application of
   * genetic operators. If the replacement size equals the population size,
   * elitism is used to carry over the member with the highest fitness.
   *
   * @return a new population of members
   */
  protected Vector<GeneticSearchObject> createNewMembers() {
    Vector<GeneticSearchObject> newPopulation = new Vector<GeneticSearchObject>();

    // always copy best member to start
    newPopulation.addElement(population.elementAt(0));
    try {
      while (newPopulation.size() < replacementSize) {

        // select 2 parents from population
        GeneticSearchObject[] parents = selectParents();

        // get genetic material
        String[] childChromosomes = null;
        String[] parentChromosomes = new String[2];

        parentChromosomes[0] = parents[0].getChromosome();
        parentChromosomes[1] = parents[1].getChromosome();

        // select an operator
        String operatorName = selectOperator();

        // apply operator to the genetic material as appropriate
        // could be a no-op or could return 1 or 2 children
        childChromosomes = (String[]) invokeOperator(operatorName, parentChromosomes);
        if (childChromosomes != null) {  // fertile!

          // create 0, 1 or 2 children (depending if duplicates are allowed)
          GeneticSearchObject[] children = createChildren(childChromosomes);
          int count = children.length;

          if ((count >= 1) && (children[0] != null)) {
            insertIntoPopulation(children[0], newPopulation);
          }
          if ((count >= 2) && (children[1] != null)) {
            insertIntoPopulation(children[1], newPopulation);
          }
        }
      }                                  // endwhile
    } catch (Exception e) {
      System.out.println("Error: during creation of next generation " + e);
    }
    return newPopulation;
  }


  /**
   * Adds new members to the population. This implementation simply replaces
   * the previous population with the new population.
   *
   *  @param newPopulation  a Vector of new genetic objects to be added to
   *                        the population
   */
  protected void integratePopulation(Vector<GeneticSearchObject> newPopulation) {
    population = newPopulation;  // just replace
  }


  /**
   * Inserts a member into the population according to its raw fitness value.
   *
   * @param newMember the GeneticSearchObject to insert into the population
   * @param list the Vector that contains the population
   *
   */
  protected void insertIntoPopulation(GeneticSearchObject newMember, Vector<GeneticSearchObject> list) {
    try {
      boolean inserted = false;
      int size = list.size();
      double fitness = newMember.getFitness();

      if (size == 0) {
        list.addElement(newMember);       // add at beginning (only one)
      } else {
        for (int i = 0; i < size; i++) {  // insert in middle
          GeneticSearchObject tmpMember = list.elementAt(i);

          if (fitness > tmpMember.getFitness()) {
            list.insertElementAt(newMember, i);
            inserted = true;
            break;
          }
        }                                 // end for
        if (!inserted) {
          list.addElement(newMember);     // add at end
        }
      }
    } catch (Exception e) {
      System.out.println("insertIntoPopulation exception " + e.toString());
    }
  }


  /**
   * Choose two population members to reproduce using the roulette wheel
   * selection technique.
   *
   * @return the two population members
   */
  public GeneticSearchObject[] selectParents() {
    GeneticSearchObject[] parents = new GeneticSearchObject[2];

    parents[0] = rouletteWheelSelection();
    parents[1] = rouletteWheelSelection();
    return parents;
  }


  /**
   * Randomly selects an operator to be applied to the parents. The total
   * operator fitness must be equal to 100.
   *
   * @return the name of the selected operator
   */
  public String selectOperator() {
  //  int size = operatorFitness.size();

    // total operator fitness must equal 100!!!
    double threshold = Math.random() * 100;
    Enumeration<?> operators = operatorFitness.keys();
    double sum = 0.0;

    while (operators.hasMoreElements()) {
      String operatorName = (String) operators.nextElement();
      Double operator = (Double) operatorFitness.get(operatorName);
      double fitness = operator.doubleValue();

      sum += fitness;
      if (sum >= threshold) {
        return operatorName;
      }
    }
    return null;
  }


  /**
   * Selects a member of the population using the roulette wheel algorithm.
   *
   * @return  a single member of the population chosen at random
   */
  public GeneticSearchObject rouletteWheelSelection() {
    double selectionThreshold = 0.0;
  //  double fitness = 0.0;

    selectionThreshold = Math.random() * totalFitness;  // raw fitness
    for (int i = 0; i < populationSize; i++) {
      if (summedFitness[i] >= selectionThreshold) {
        GeneticSearchObject member = population.elementAt(i);

        return member;
      }
    }
    return null;
  }


  /**
   * Resets the population for another search.
   */
  public void reset() {
    init();
  }


  /**
   * Appies an operator to the parent chromosomes to create the child
   * chromosomes.
   *
   * @param operatorName  the String name of operator to be invoked
   * @param parents       the String array that contains parent chromosomes
   *
   * @return the set of children chromosomes that were created
   */
  public String[] invokeOperator(String operatorName, String[] parents) {
    if (operatorName.equals("onePointCrossoverAndMutate")) {
      return onePointCrossoverAndMutate(parents);
    } else if (operatorName.equals("onePointCrossover")) {
      return onePointCrossover(parents);
    } else if (operatorName.equals("mutate")) {
      return mutate(parents);
    }
    return parents;  // if invalid operator just return parents???
  }


  /**
   * Given two parent chromosomes, does a single-point crossover, creating
   * two children chromosomes and mutating bits at the specified rate
   *
   * @param parents the String array that contains the parent chromosomes
   *                from which two children chromosomes are created
   *
   * @return the set of children chromosomes created from parent chromosomes
   */
  public String[] onePointCrossoverAndMutate(String[] parents) {
    String[] children = new String[2];
    String c1Chromosome = parents[0];
    String c2Chromosome = parents[1];

    // now, see if we should do a crossover on them
    double rand = Math.random();  // roll the dice

    if (rand < crossoverRate) {

      // ok, do a crossover on the parents
      String p1Left = c1Chromosome;
      String p1Right = "";
      String p2Left = c2Chromosome;
      String p2Right = "";
      int bitPosition = (int) (Math.random() * chromosomeLength);

      if ((bitPosition > 0) && (bitPosition <= chromosomeLength)) {
        p1Left = c1Chromosome.substring(0, bitPosition);
        p1Right = c1Chromosome.substring(bitPosition, chromosomeLength);
        p2Left = c2Chromosome.substring(0, bitPosition);
        p2Right = c2Chromosome.substring(bitPosition, chromosomeLength);
      }
      c1Chromosome = p1Left + p2Right;
      c2Chromosome = p2Left + p1Right;
    }

    // now see if we should mutate any bits
    c1Chromosome = mutateChromosome(c1Chromosome);
    c2Chromosome = mutateChromosome(c2Chromosome);
    children[0] = c1Chromosome;
    children[1] = c2Chromosome;
    return children;
  }


  /**
   * Mutates a single chromosome by doing a bitwise test against the
   * mutation rate and then rolling the dice to select a new bit value from
   * the vocabulary.
   *
   * @param  chromosome the String that represents the original chromosome
   *
   * @return the (potentially) mutated chromosome string
   */
  public String mutateChromosome(String chromosome) {
    StringBuffer buf = new StringBuffer(chromosome);
    int size = chromosome.length();
    int vocabSize = vocabulary.length();

    for (int i = 0; i < size; i++) {
      double rand = Math.random();
      int bitPos = 0;

      if (rand < mutationRate) {             // see if we should mutate the bit
        if (debugOn) {
          System.out.println("mutate bit position = " + i);
        }
        double rand2 = Math.random();

        bitPos = (int) (rand2 * vocabSize);  // now see value we should use
        if (bitPos == vocabSize) {
          bitPos = bitPos - 1;
        }
        char newValue = vocabulary.charAt(bitPos);

        buf.setCharAt(i, newValue);
      }
    }
    return buf.toString();
  }


  /**
   * Given two parent chromosomes, does a single-point crossover, creating
   * two children chromosomes.
   *
   * @param parents the String array that contains the parent chromosomes
   * from which the children are created
   *
   * @return the set of children chromosomes created from parent chromosomes
   */
  public String[] onePointCrossover(String[] parents) {
    String[] children = new String[2];
    String c1Chromosome = parents[0];
    String c2Chromosome = parents[1];

    // now, see if we should do a crossover on them
    double rand = Math.random();  // roll the dice

    if (rand < crossoverRate) {

      // ok, do a crossover on the parents
      String p1Left = c1Chromosome;
      String p1Right = "";
      String p2Left = c2Chromosome;
      String p2Right = "";
      int bitPosition = (int) (Math.random() * chromosomeLength);

      if ((bitPosition > 0) && (bitPosition <= chromosomeLength)) {
        p1Left = c1Chromosome.substring(0, bitPosition);
        p1Right = c1Chromosome.substring(bitPosition, chromosomeLength);
        p2Left = c2Chromosome.substring(0, bitPosition);
        p2Right = c2Chromosome.substring(bitPosition, chromosomeLength);
      }
      c1Chromosome = p1Left + p2Right;
      c2Chromosome = p2Left + p1Right;
    }
    children[0] = c1Chromosome;
    children[1] = c2Chromosome;
    return children;
  }


  /**
   * Mutates two parent chromosomes, subject to the mutation rate,
   * to create two children chromosomes.
   *
   * @param parents the String array that represent the parent chromosomes
   *
   * @return the set of potentially mutated children chromosomes
   */
  public String[] mutate(String[] parents) {
    String[] children = new String[2];

    children[0] = mutateChromosome(parents[0]);
    children[1] = mutateChromosome(parents[1]);
    return children;
  }


  /**
   * Writes trace data out to the text window.
   *
   * @param text the String object that contains
   */
  protected void trace(String text) {
    traceTextArea.append(text);
  }


  /**
   * Displays the entire population in the text window.
   */
  protected void displayPopulation() {
    int topN = 2;

    for (int i = 0; i < topN; i++) {
      trace("[" + i + "]" + population.elementAt(i).toString() + "\n");
    }
    traceTextArea.repaint();
  }
}
