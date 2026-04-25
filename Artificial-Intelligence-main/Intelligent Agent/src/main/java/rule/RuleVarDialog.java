package rule;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;


/**
 * The RuleVarDialog class implements a dialog to ask the user to supply
 * the value of a variable during backward chaining.
 *
 * @author Joseph P. Bigus
 * @author Jennifer Bigus
 *
 * @copyright
 * Constructing Intelligent Agents using Java
 * (C) Joseph P. Bigus and Jennifer Bigus 2001
 *
 */
public class RuleVarDialog extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JButton OKButton = new JButton();
  JLabel PromptLabel = new JLabel();
  JLabel jLabel2 = new JLabel();
  JComboBox<String> ValueComboBox = new JComboBox<String>();
  RuleVariable ruleVar;


  /**
   * Creates a <code>RuleVarDialog</code> object with the given parameters.
   *
   * @param frame the Frame object for the dialog
   * @param title the String object that contains the title of the dialog
   * @param modal the boolean flag that indicates modality
   *
   */
  public RuleVarDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  /**
   * Creates a <code>RuleVarDialog</code> object.
   *
   */
  public RuleVarDialog() {
    this(null, "", false);
  }


  /**
   * Initializes the GUI controls.
   *
   * @throws Exception if any error occurs during initialization
   *
   */
  void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    OKButton.setText("OK");
    OKButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        OKButton_actionPerformed(e);
      }
    });
    PromptLabel.setText("What ?");
    PromptLabel.setBounds(new Rectangle(32, 29, 341, 17));
    jPanel2.setLayout(null);
    jLabel2.setText("Value = ");
    jLabel2.setBounds(new Rectangle(36, 79, 67, 17));
    ValueComboBox.setBounds(new Rectangle(104, 79, 226, 24));
    ValueComboBox.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ValueComboBox_actionPerformed(e);
      }
    });
    panel1.setMinimumSize(new Dimension(400, 200));
    panel1.setPreferredSize(new Dimension(400, 200));
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(OKButton, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(PromptLabel, null);
    jPanel2.add(jLabel2, null);
    jPanel2.add(ValueComboBox, null);
  }


  /**
   * Closes the window when OK is pressed.
   *
   * @param e the ActionEvent object generated when OK was pressed
   *
   */
  void OKButton_actionPerformed(ActionEvent e) {
    dispose();
  }


  /**
   * Does nothing.
   *
   * @param e the ActionEvent object
   *
   */
  void ValueComboBox_actionPerformed(ActionEvent e) {}


  /**
   * Sets the GUI using data from the given rule variable.
   *
   * @param ruleVar the RuleVariable object
   *
   */
  public void setData(RuleVariable ruleVar) {
    this.ruleVar = ruleVar;
    PromptLabel.setText(ruleVar.getPromptText());
    Vector<?> labels = ruleVar.getLabels();

    if (labels != null) {
      for (int i = 0; i < labels.size(); i++) {
        ValueComboBox.addItem((String) labels.elementAt(i));
      }
      ValueComboBox.setEditable(false);  // user must select value
    } else {
      ValueComboBox.setEditable(true);   // user must enter value
    }
  }


  /**
   * Retrieves the item selected in the combo box.
   *
   * @return the String object that contains the selected item
   *
   */
  public String getData() {
    return (String) ValueComboBox.getSelectedItem();
  }
}
