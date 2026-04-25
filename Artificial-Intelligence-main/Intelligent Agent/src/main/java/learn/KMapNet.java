package learn;


import javax.swing.*;
import java.util.*;
import java.lang.Math;
import java.io.*;


/**
 * The <code>KMapNet</code> class provides the support necessary
 * for the Kohonen Map neural network.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class KMapNet extends Object implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String name;

  // data parameters
  private DataSet dataset;
  private Vector<?> data;
  private int recInx = 0;         // current record index
  private int numRecs = 0;        // number of records in data
  private int fieldsPerRec;
  private int numPasses = 0;

  // network architecture parameters
  private int numInputs;
  private int numRows;
  private int numCols;
  private int numOutputs;
  private int numUnits;

  // network control parameters
  private int mode;
  private double learnRate;
  private double initLearnRate = 1.0;
  private double finalLearnRate = 0.05;
  private double sigma;
  private int maxNumPasses = 20;  // default

  // network data
  private int winner;             // index of the winning unit
  private double activations[];
  private double weights[];
  private int distance[];         // used in neighborhood computation
  transient public  JTextArea textArea1;


  /**
   * Creates a Kohonen map neural network with the given name.
   *
   * @param name the String that contains the name of the network
   */
  public KMapNet(String name) {
    this.setName(name);
    data = new Vector<>();
  }


  /**
   * Sets the dataset for the Kohonen map.
   *
   * @param dataset the DataSet for the Kohonen map
   */
  public void setDataSet(DataSet dataset) {
    this.dataset = dataset;
  }

   /**
   * Returns the dataset for the Kohonen map.
   *
   * @param dataset the DataSet for the Kohonen map
   */
  public DataSet getDataSet() {
    return dataset;
  }

  /**
   * Sets the number of data records.
   *
   * @param numRecs the number of data records
   */
  public void setNumRecs(int numRecs) {
    this.numRecs = numRecs;
  }


  /**
   * Retrieves the number of data records.
   *
   * @return the number of data records
   */
  public int getNumRecs() {
    return numRecs;
  }


  /**
   * Sets the number of fields per record.
   *
   * @param fieldsPerRec the number of fields per record
   */
  public void setFieldsPerRec(int fieldsPerRec) {
    this.fieldsPerRec = fieldsPerRec;
  }


  /**
   * Retrieves the number of fields per record.
   *
   * @return the number of fields per record
   */
  public int getFieldsPerRec() {
    return fieldsPerRec;
  }


  /**
   * Sets the vector of data used for training or testing.
   *
   * @param data the Vector of test data
   */
  public void setData(Vector<?> data) {
    this.data = data;
  }


  /**
   * Retrieves the vector of data used for training or testing.
   *
   * @return the Vector of test data
   */
  public Vector<?> getData() {
    return data;
  }


  /**
   * Sets the mode network control parameter.
   *
   * @param mode the mode network control parameter (0=train, 1=run)
   */
  public void setMode(int mode) {
    this.mode = mode;
  }


  /**
   * Retrieves the mode network control parameter.
   *
   * @return the mode network control parameter (0=train, 1=run)
   */
  public int getMode() {
    return mode;
  }


  /**
   * Displays an array of network data.
   *
   * @param name  the String name of the information being displayed
   * @param arr   the array to be displayed
   */
  public void show_array(String name, double[] arr) {
    if (textArea1 != null) {
      textArea1.append("\n" + name + "= ");
      for (int i = 0; i < arr.length; i++) {
        textArea1.append(arr[i] + "  ");
      }
    }
  }


  /**
   * Displays the relevant information for the network.
   */
  public void display_network() {

    //        show_array("weights",weights);
    show_array("activations", activations);
    if (textArea1 != null) textArea1.append("\nWinner = " + winner + "\n");
  }


  /**
   * Reads data from the train or test dataset into the activations of the input
   * units.
   */
  public void readInputs() {
    recInx = recInx % numRecs;  // keep index from 0 to n-1 records
    int inx = 0;
    double[] tempRec = (double[]) data.elementAt(recInx);  // get record

    for (inx = 0; inx < numInputs; inx++) {
      activations[inx] = tempRec[inx];
    }
    if (recInx == 0) {
      numPasses++;  // completed another pass through the data
      adjustNeighborhood();
    }
    recInx++;
  }


  /**
   * Initializes a matrix of distances from each unit to all others in the
   * network.
   */
  public void computeDistances() {
    int i, j, xi, xj, yi, yj;

    distance = new int[numOutputs * numOutputs];
    for (i = 0; i < numOutputs; i++) {
      xi = i % numCols;
      yi = i / numRows;
      for (j = 0; j < numOutputs; j++) {
        xj = j % numCols;
        yj = j / numRows;
        distance[i * numOutputs + j] = (int) Math.pow(((xi - xj) * (xi - xj)), 2.0) + (int) Math.pow(((yi - yj) * (yi - yj)), 2.0);
      }
    }
  }


  /**
   * Adjusts the learn rate and neigborhood width (sigma) as training progresses.
   */
  public void adjustNeighborhood() {
    double ratio = (double) numPasses / maxNumPasses;

    learnRate = initLearnRate * Math.pow(finalLearnRate / initLearnRate, ratio);
    sigma = (double) numCols * Math.pow((0.20 / (double) numCols), ratio);

    // System.out.println("ratio= " + ratio + " learnRate= " + learnRate + " sigma=" + sigma);
  }


  /**
   * Computes the outputs by doing a single forward pass through the network,
   * computing the Euclidean distance from the input vector to all the output
   * units.
   */
  public void computeOutputs() {
    int index, i, j;
    int lastOut = numUnits - 1;
    int firstOut = numInputs;

    //first layer
    for (i = firstOut; i <= lastOut; i++) {
      index = (i - firstOut) * numInputs;
      activations[i] = 0.0;
      for (j = 0; j < numInputs; j++) {  // compute net inputs
        activations[i] += (activations[j] - weights[index + j]) * (activations[j] - weights[index + j]);
      }
    }
  }


  /**
   * Selects the winner by finding the unit with the smallest activation.
   */
  public void selectWinner() {
    winner = 0;
    double min = activations[numInputs];

    for (int i = 0; i < numOutputs; i++) {
      if (activations[i + numInputs] < min) {
        min = activations[i + numInputs];
        winner = i;
      }
    }
  }


  /**
   * Adjusts the weights of the units in the neighborhood of the winner using the
   * neighborhood, the distance from the winning unit, and the learn rate.
   */
  public void adjustWeights() {

    // apply the changes to the weights
    int i, j, inx, base;
    int numOutputs = numRows * numCols;
    double dist, range, sigma_squared;

    sigma_squared = sigma * sigma;
    for (i = 0; i < numOutputs; i++) {
      dist = Math.exp((distance[winner * numOutputs + i] * -1.0) / (2.0 * sigma_squared));
      base = i * numInputs;  // compute the base index
      range = learnRate * dist;
      for (j = 0; j < numInputs; j++) {
        inx = base + j;
        weights[inx] += range * (activations[j] - weights[inx]);
      }
    }
  }


  /**
   * Selects a winner from a single input record.
   *
   * @param inputRec the double array which contains the input data
   *
   * @return the winner (cluster number)
   */
  public int getCluster(double[] inputRec) {

    // set input unit activations
    for (int inx = 0; inx < numInputs; inx++) {
      activations[inx] = inputRec[inx];
    }
    computeOutputs();  // do forward pass through network
    selectWinner();    // find the winning unit
    return winner;
  }


  /**
   * Trains or tests the network by setting the input unit activations, computing
   * the outputs, selecting the winner, and, if in train mode, adjusting the
   * weights.
   */
  public void cluster() {
    readInputs();      // set input unit activations
    computeOutputs();  // do forward pass through network
    selectWinner();    // find the winning unit

    // only adjust if in training mode
    if (mode == 0) {
      adjustWeights();  // apply changes to weights
    }
  }


  /**
   * Resets the network.
   */
  public void reset() {
    int i;

    for (i = 0; i < weights.length; i++) {
      weights[i] = 0.6 - (0.2 * Math.random());  // between 0.4 and 0.6
    }
  }


  /**
   * Creates a Kohonen map network with the specified architecture.
   *
   * @param numIn    the number of input units
   * @param numRows  the number of rows
   * @param numCols  the number of columns
   *
   * @param NumIn the int
   * @param NumRows the int
   * @param NumCols the int
   */
  public void createNetwork(int NumIn, int NumRows, int NumCols) {

    // set the network architecture
    numInputs = NumIn;
    numRows = NumRows;
    numCols = NumCols;
    numOutputs = numRows * numCols;
    numUnits = numInputs + numOutputs;

    // initialize control parameters
    learnRate = 0.1;
    mode = 0;  // 0 = train mode, 1 = run mode

    // create arrays
    activations = new double[numUnits];
    weights = new double[numInputs * numOutputs];

    // fill in the Distance matrix
    computeDistances();
    adjustNeighborhood();  // set the initial learnRate
    reset();               // reset and initialize weight arrays
  }


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}
}
