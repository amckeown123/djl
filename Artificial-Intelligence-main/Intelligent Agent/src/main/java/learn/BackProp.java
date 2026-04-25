package learn;


import javax.swing.*;
import java.util.*;
import java.lang.Math;
import java.io.*;


/**
 * The <code>BackProp</code> class implements the standard backward propagation
 * algorithm with momentum.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1998, 2001
 *
 */
public class BackProp extends Object implements Serializable {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

private String name;

  // data parameters
  private DataSet dataset;
  private Vector<Object> data;             // train/test data from file
  private int recInx = 0;          // current record index
  private int numRecs = 0;         // number of records in data
  private int fieldsPerRec = 0;

  // error measures
  private double sumSquaredError;  // total SSE for an epoch
  private double aveRMSError;      // average root-mean-square error
  private int numPasses;           // number of passes over the data set

  // network architecture parameters
  private int numInputs;
  private int numHid1;
  private int numOutputs;
  private int numUnits;
  private int numWeights;

  // network control parameters
  private int mode;
  private double learnRate;
  private double momentum;
  private double tolerance;

  // network data
  private double activations[];
  private double weights[];
  private double wDerivs[];
  private double thresholds[];
  private double tDerivs[];
  private double tDeltas[];
  private double teach[];          // target output values
  private double error[];
  private double deltas[];         // the error deltas
  private double wDeltas[];
  transient public JTextArea textArea1;


  /**
   * Creates a back propagation object with the given name.
   *
   * @param name the String object that contains the name of the back prop object
   */
  public BackProp(String name) {
    this.setName(name);
    data = new Vector<>();
  }


  /**
   * Sets the dataset for the back prop object.
   *
   * @param dataset the DataSet for this object
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
  public void setData(Vector<Object> data) {
    this.data = data;
  }


  /**
   * Retrieves the vector of data used for training or testing.
   *
   * @return the Vector of test data
   */
  public Vector<Object> getData() {
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
   * Retrieves the average RMS error.
   *
   * @return the average RMS error for the network
   */
  public double getAveRMSError() {
    return aveRMSError;
  }


  /**
   * Retrieves the learn rate parameter.
   *
   * @return the learn rate
   */
  public double getLearnRate() {
    return learnRate;
  }


  /**
   * Retrieves the momentum parameter.
   *
   * @return the momentum
   */
  public double getMomentum() {
    return momentum;
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
   * Displays the relevant information for the network.  Default is to show only
   * the activations array values.
   */
  public void display_network() {
    show_array("activations", activations);

    //  uncomment these lines to see more data on network state
    //    show_array("weights",weights);
    //    show_array("thresholds", thresholds);
    //    show_array("teach",teach);
    String desired = dataset.getClassFieldValue(recInx - 1);
    String actual = dataset.getClassFieldValue(activations, numInputs + numHid1);
    if (textArea1 != null) {
     textArea1.append("\n Desired: " + desired + " Actual: " + actual);
    }
  }


  /**
   * Computes the activation value based on the given sum.
   *
   * @param sum the double value which is the sum of the threshold and each
   *            input activation multiplied by the corresponding input weight
   *
   * @return the computed activation value
   */
  public double logistic(double sum) {
    return 1.0 / (1 + Math.exp(-1.0 * sum));
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
    for (int i = 0; i < numOutputs; i++) {
      teach[i] = tempRec[inx++];
    }
    recInx++;
  }


  /**
   * Computes the outputs by doing a single forward pass through the network.
   */
  public void computeOutputs() {
    int i, j;
    int firstHid1 = numInputs;
    int firstOut = numInputs + numHid1;

    // first layer
    int inx = 0;

    for (i = firstHid1; i < firstOut; i++) {
      double sum = thresholds[i];

      for (j = 0; j < numInputs; j++) {  // compute net inputs
        sum += activations[j] * weights[inx++];
      }
      activations[i] = logistic(sum);    // compute activation
    }

    // second layer
    for (i = firstOut; i < numUnits; i++) {
      double sum = thresholds[i];

      for (j = firstHid1; j < firstOut; j++) {  // compute net inputs
        sum += activations[j] * weights[inx++];
      }
      activations[i] = logistic(sum);           // compute activation
    }
  }


  /**
   * Starting at the output layer and working backward to the input layer,
   * computes the following:
   * <ol>
   * <li> the output layer errors and deltas based on the difference between the
   * activations and the target values
   * <li> the squared errors used to calculate the average RMS error
   * <li> the hidden layer errors and deltas
   * <li> the input layer errors
   * </ol>
   */
  public void computeError() {
    int i, j;
    int firstHid1 = numInputs;
    int firstOut = numInputs + numHid1;

    // clear hidden unit errors
    for (i = numInputs; i < numUnits; i++) {
      error[i] = 0.0;
    }

    // compute output layer errors and deltas
    for (i = firstOut; i < numUnits; i++) {
      error[i] = teach[i - firstOut] - activations[i];  // compute output errors
      sumSquaredError += error[i] * error[i];  // accumulate squared errors
      if (Math.abs(error[i]) < tolerance) {
        error[i] = 0.0;                        // close enough
      }
      deltas[i] = error[i] * activations[i] * (1 - activations[i]);
    }

    // compute hidden layer errors
    int winx = numInputs * numHid1;  // offset into weight array

    for (i = firstOut; i < numUnits; i++) {
      for (j = firstHid1; j < firstOut; j++) {
        wDerivs[winx] += deltas[i] * activations[j];
        error[j] += weights[winx] * deltas[i];
        winx++;
      }
      tDerivs[i] += deltas[i];
    }

    // compute hidden layer deltas
    for (i = firstHid1; i < firstOut; i++) {
      deltas[i] = error[i] * activations[i] * (1 - activations[i]);
    }

    // compute input layer errors
    winx = 0;  // offset into weight array
    for (i = firstHid1; i < firstOut; i++) {
      for (j = 0; j < firstHid1; j++) {
        wDerivs[winx] += deltas[i] * activations[j];
        error[j] += weights[winx] * deltas[i];
        winx++;
      }
      tDerivs[i] += deltas[i];
    }
  }


  /**
   * Adjusts the weights and thresholds by computing and adding the delta for
   * each.
   */
  public void adjustWeights() {
    int i;

    // first walk through the weights array
    for (i = 0; i < weights.length; i++) {
      wDeltas[i] = (learnRate * wDerivs[i]) + (momentum * wDeltas[i]);
      weights[i] += wDeltas[i];  // modify the weight
      wDerivs[i] = 0.0;
    }

    // then walk through the threshold array
    for (i = numInputs; i < numUnits; i++) {
      tDeltas[i] = learnRate * tDerivs[i] + (momentum * tDeltas[i]);
      thresholds[i] += tDeltas[i];  // modify the threshold
      tDerivs[i] = 0.0;
    }

    // if at the end of an epoch, compute average RMS Error
    if (recInx == numRecs) {
      setNumPasses(getNumPasses() + 1);            // increment pass counter
      aveRMSError = Math.sqrt(sumSquaredError / (numRecs * numOutputs));
      sumSquaredError = 0.0;  // clear the accumulator
    }
  }


  /**
   * For networks with single continuous outputs only, retrieve the prediction.
   * Do a single forward pass through the network and return the output value
   *
   * @param inputRec the double array of input record
   *
   * @return the prediction
   */
  public double getPrediction(double[] inputRec) {
    int firstOut = numInputs + numHid1;

    // set input unit activations
    for (int inx = 0; inx < numInputs; inx++) {
      activations[inx] = inputRec[inx];
    }
    computeOutputs();  // do forward pass through network
    return activations[firstOut];
  }


  /**
   * Processes the network by reading the input values, computing the output and error values,
   * and, if training, adjusting the weights.
   */
  public void process() {
    readInputs();      // set input unit activations
    computeOutputs();  // do forward pass through network
    computeError();    // compute error and deltas

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
      weights[i] = 0.5 - (Math.random());  // between -0.5 and +0.5
      wDeltas[i] = 0.0;
      wDerivs[i] = 0.0;
    }
    for (i = 0; i < numUnits; i++) {
      thresholds[i] = 0.5 - (Math.random());  // between -0.5 and +0.5
      tDeltas[i] = 0.0;
      tDerivs[i] = 0.0;
    }
  }


  /**
   * Creates a backward propagation network with the specified architecture.
   *
   * @param numIn     the number of input units
   * @param numHidden the number of hidden units
   * @param numOut    the number of output units
   */
  public void createNetwork(int numIn, int numHidden, int numOut) {

    // set the network architecture
    numInputs = numIn;
    numHid1 = numHidden;
    numOutputs = numOut;
    numUnits = numInputs + numHid1 + numOutputs;
    numWeights = (numInputs * numHid1) + (numHid1 * numOutputs);

    // initialize control parameters
    learnRate = 0.2;
    momentum = 0.7;
    tolerance = 0.1;
    mode = 0;                            // 0 = train mode, 1 = run mode
    aveRMSError = 0.0;
    setNumPasses(0);

    // create weight and error arrays
    activations = new double[numUnits];  // unit activations
    weights = new double[numWeights];
    wDerivs = new double[numWeights];    // accumulated wDeltas
    wDeltas = new double[numWeights];    // weight changes
    thresholds = new double[numUnits];
    tDerivs = new double[numUnits];      // accumulated tDeltas
    tDeltas = new double[numUnits];      // threshold changes
    teach = new double[numOutputs];      // desired outputs
    deltas = new double[numUnits];
    error = new double[numUnits];
    reset();  // reset and initialize the weight arrays
    return;
  }


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


public int getNumPasses() {
	return numPasses;
}


public void setNumPasses(int numPasses) {
	this.numPasses = numPasses;
}
}
