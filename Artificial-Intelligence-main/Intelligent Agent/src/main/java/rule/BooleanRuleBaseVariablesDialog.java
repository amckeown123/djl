package rule;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;


/**
 * The <code>BooleanRuleBaseVariablesDialog</code> class implements the
 * dialog for the boolean rule base variables.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 1997, 2001
 *
 */
public class BooleanRuleBaseVariablesDialog extends JDialog implements ListSelectionListener {
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
  JList<Object> variableList = new JList<Object>();
  BooleanRuleBase ruleBase = null;
  JScrollPane jScrollPane1 = new JScrollPane();
  boolean fillingComboBox = false;
  JComboBox<String> valueComboBox = new JComboBox<String>();
  JLabel jLabel3 = new JLabel();


  /**
   * Creates a <code>BooleanRuleBaseVariablesDialog</code> instance
   * with the given frame, title, and modal setting.
   *
   * @param frame the Frame object for this dialog
   * @param title the String that is the title of this dialog
   * @param modal the boolean modal flag
   */
  public BooleanRuleBaseVariablesDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Creates a <code>BooleanRuleBaseVariablesDialog</code> object.
   *
   */
  public BooleanRuleBaseVariablesDialog() {
    this(null, "", false);
  }


  /**
   * Initializes the dialog.
   *
   * @throws Exception if any errors occur
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
    variableList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    variableList.addListSelectionListener(this);
    panel1.setMinimumSize(new Dimension(405, 320));
    panel1.setPreferredSize(new Dimension(405, 320));
    jScrollPane1.setBounds(new Rectangle(20, 48, 186, 183));
    valueComboBox.setBounds(new Rectangle(224, 50, 157, 27));
    valueComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        valueComboBox_actionPerformed(e);
      }
    });
    jLabel3.setText("Select variable to display/change value in combo box.");
    jLabel3.setBounds(new Rectangle(25, 241, 364, 16));
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(closeButton, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jScrollPane1, null);
    jScrollPane1.getViewport().add(variableList, null);
    jPanel2.add(jLabel1, null);
    jPanel2.add(jLabel2, null);
    jPanel2.add(valueComboBox, null);
    jPanel2.add(jLabel3, null);
  }


  /**
   * Initializes the list of variables for this dialog.
   *
   */
  protected void initialize() {
    Vector<Object> vars = new Vector<Object>();
    Enumeration<?> enum1= ruleBase.variableList.elements();

    while (enum1.hasMoreElements()) {
      vars.addElement(enum1.nextElement());
    }
    variableList.setListData(vars);
  }


  /**
   * Performs the action for the close button
   *
   * @param e the ActionEvent object that was generated for the close
   *
   */
  void closeButton_actionPerformed(ActionEvent e) {
    dispose();
  }


  /**
   * Sets the rule base for this dialog.
   *
   * @param ruleBase the BooleanRuleBase object for this dialog
   *
   */
  public void setRuleBase(BooleanRuleBase ruleBase) {
    this.ruleBase = ruleBase;
    initialize();  // file variable list
  }


  /**
   * Fills the value combo box and set initial value to match
   * the variable's current value, depending on the user selection.
   *
   * @param e the ListSelectionEvent object for the value combo box
   *          selection
   */
  public void valueChanged(ListSelectionEvent e) {
    RuleVariable ruleVar = (RuleVariable) variableList.getSelectedValue();

    // fill the combo box with valid values
    fillingComboBox = true;  // tell valuecombobox to ignore events
    if (valueComboBox.getItemCount() > 0) {
      valueComboBox.removeAllItems();
    }
    valueComboBox.addItem("<null>");
    Enumeration<?> labels = ruleVar.labels.elements();

    while (labels.hasMoreElements()) {
      valueComboBox.addItem(((String) labels.nextElement()));
    }

    // select the current value
    String value = ruleVar.getValue();

    if (value == null) {
      valueComboBox.setSelectedItem("<null>");
    } else {
      valueComboBox.setSelectedItem(value);
    }
    fillingComboBox = false;
  }


  /**
   * Peforms the action for the value combo box.
   *
   * @param e the ActionEvent object for the value combo box
   *
   */
  void valueComboBox_actionPerformed(ActionEvent e) {
    if (fillingComboBox) {
      return;  // ignore events if just adding items
    }
    RuleVariable ruleVar = (RuleVariable) variableList.getSelectedValue();
    String varValue = (String) valueComboBox.getSelectedItem();

    if ((varValue == null) || varValue.equals("<null>")) {
      ruleVar.setValue(null);  // set value to null, undefined
    } else {
      ruleVar.setValue(varValue);
    }
  }
}
