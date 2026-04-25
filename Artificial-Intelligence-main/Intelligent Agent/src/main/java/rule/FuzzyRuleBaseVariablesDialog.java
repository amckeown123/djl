package rule;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;


/**
 * The <code>FuzzyRuleBaseVariablesDialog</code> class implements the dialog
 * used to set the fuzzy rule base variables.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P.Bigus and Jennifer Bigus 2001
 *
 */
public class FuzzyRuleBaseVariablesDialog extends JDialog implements ListSelectionListener {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JButton closeButton = new JButton();
  JPanel jPanel2 = new JPanel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JList<Object> VariableList = new JList<Object>();
  FuzzyRuleBase ruleBase = null;
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextField valueTextField = new JTextField();
  JLabel jLabel3 = new JLabel();
  JButton resetVarButton = new JButton();
  JLabel jLabel4 = new JLabel();


  /**
   * Creates a <code>FuzzyRuleBaseVariablesDialog</code> with the given parameters.
   *
   * @param frame the Frame for this dialog
   * @param title the String that is the title of the dialog
   * @param modal the boolean flag that indicates modality
   */
  public FuzzyRuleBaseVariablesDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Creates a <code>FuzzyRuleBaseVariablesDialog</code> object.
   *
   */
  public FuzzyRuleBaseVariablesDialog() {
    this(null, "", false);
  }


  /**
   * Initializes the GUI controls.
   *
   * @throws Exception if any errors occur during initialization
   *
   */
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    closeButton.setText("Close");
    closeButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        closeButton_actionPerformed(e);
      }
    });
    jPanel2.setLayout(null);
    jLabel1.setText("Variable");
    jLabel1.setBounds(new Rectangle(24, 21, 126, 17));
    jLabel2.setText("Value");
    jLabel2.setBounds(new Rectangle(216, 22, 41, 17));
    VariableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    VariableList.addListSelectionListener(this);
    panel1.setMinimumSize(new Dimension(405, 320));
    panel1.setPreferredSize(new Dimension(405, 320));
    jScrollPane1.setBounds(new Rectangle(20, 48, 186, 183));
    valueTextField.setBounds(new Rectangle(220, 52, 160, 26));
    valueTextField.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        valueTextField_actionPerformed(e);
      }
    });
    jLabel3.setText("Press enter to set value");
    jLabel3.setBounds(new Rectangle(220, 85, 160, 22));
    resetVarButton.setText("Reset variable");
    resetVarButton.setBounds(new Rectangle(233, 118, 115, 27));
    resetVarButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        resetVarButton_actionPerformed(e);
      }
    });
    jLabel4.setText("Select variable to display/change value in text field.");
    jLabel4.setBounds(new Rectangle(22, 242, 371, 24));
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(closeButton, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(VariableList, null);
    jPanel2.add(jLabel1, null);
    jPanel2.add(jLabel2, null);
    jPanel2.add(valueTextField, null);
    jPanel2.add(jLabel3, null);
    jPanel2.add(resetVarButton, null);
    jPanel2.add(jLabel4, null);
  }


  /**
   * Initializes the variable list.
   *
   */
  protected void initialize() {
    Vector<Object> vars = new Vector<Object>();
    Enumeration<?> enum1 = ruleBase.getVariables().elements();

    while (enum1.hasMoreElements()) {
      vars.addElement(enum1.nextElement());
    }
    VariableList.setListData(vars);
  }


  /**
   * Closes the dialog window.
   *
   * @param e the ActionEvent object when the close button is pressed
   *
   */
  void closeButton_actionPerformed(ActionEvent e) {
    dispose();
  }


  /**
   * Sets the rule base for the dialog and initializes the variable list.
   *
   * @param ruleBase the FuzzyRuleBase object for the dialog
   *
   */
  public void setRuleBase(FuzzyRuleBase ruleBase) {
    this.ruleBase = ruleBase;
    initialize();  // file variable list
  }


  /**
   *  Fills the value combo box and sets the initial value to match
   *  the variable's current value when the user selects a variable from
   *  the list box.
   *
   * @param e the ListSelectionEvent object generated when a selection was made
   */
  public void valueChanged(ListSelectionEvent e) {
    ContinuousFuzzyRuleVariable ruleVar = (ContinuousFuzzyRuleVariable) VariableList.getSelectedValue();

    valueTextField.setText(ruleVar.getSymbolicValue());
  }


  /**
   *  Sets the variable value when the user pressed the enter key in the text field.
   *
   * @param e the ActionEvent object generated when the user set a text value
   */
  void valueTextField_actionPerformed(ActionEvent e) {
    ContinuousFuzzyRuleVariable ruleVar = (ContinuousFuzzyRuleVariable) VariableList.getSelectedValue();
    String varValue = valueTextField.getText().trim();

    ruleVar.setSymbolicValue(varValue);

    //  System.out.println("\n Fuzzy variable: " + ruleVar.getName() + " set to " + varValue) ;
  }


  /**
   * Resets the variable state to be unknown.
   *
   * @param e the ActionEvent object generated when the reset button was pressed
   */
  void resetVarButton_actionPerformed(ActionEvent e) {
    ContinuousFuzzyRuleVariable ruleVar = (ContinuousFuzzyRuleVariable) VariableList.getSelectedValue();

    ruleVar.reset();
    valueTextField.setText(ruleVar.getSymbolicValue());  // show reset value
  }
}
