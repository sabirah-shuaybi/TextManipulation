import java.awt.*;
import objectdraw.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * TextPlay facilitates the manipulation of text through swing components such as:
 * a JSlider, a JComboBox, a JTextField, and a JButton.
 * The JSlider allows user to increase or decrease font size.
 * The JComboBox allows user to pick a font style from menu.
 * The JTextField is the field in which the user types the text they want displayed
 * The JButton allows user to remove the last text object created
 * The onMouseClick method intercepts mouse clicks and allows for creation of a text object
 * at the location of the user click.
 *
 * @author Sabirah Shuaybi
 * @version 11/08/16
 */

//TextPlay implements two interfaces: ActionListener and ChangeListener
public class TextPlay extends WindowController implements ActionListener, ChangeListener
{
    //Constants for resizing the window frame/canvas
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 420;

    //All swing components to be used throughout program
    private JTextField textField;
    private JButton removeButton;
    private JComboBox pickFont;
    private JSlider fontSizeGradient;

    //To hold the last text object created
    //Needed to be able to manipulate text across methods (ex: increase font size, or change font)
    private Text currentText;

    //Initialize fontSize and fontStyle
    //Significance: to prevent null pointer exception
    private int fontSize = 10;
    private String fontStyle = "Courier";


    public void begin() {

        //Resize window
        resize(WINDOW_WIDTH, WINDOW_HEIGHT);

        createMainPanel();
    }

    /* Positions swing components in an aesthetically pleasing manner
    by nesting panels with mini grid layouts for increasing organization */
    private void createMainPanel() {

        //Construct a panel that will hold all swing components
        JPanel componentPanel = new JPanel();

        //Set grid layout of 2 rows and 1 column within component panel
        //Significance: to allow swing components to be configured in a more pleasing display
        componentPanel.setLayout(new GridLayout(2, 1));

        //Add componentPanel to window
        //Position panel to SOUTH of window frame
        add(componentPanel, BorderLayout.SOUTH);

        //Construct a panel within component panel for the 3 other swing compnents (minus the text field)
        JPanel controlPanel = new JPanel();
        //Set grid layout, 1 row and 3 columns. One column per swing component
        controlPanel.setLayout(new GridLayout(1, 3));
        //Add the nested controlPanel to the bigger componentPanel
        componentPanel.add(controlPanel);

        //Construct a JTextField, a JButton. a JComboBox and a JSlider
        //Pass in corresponding panel so swing components know exactly where to go
        createTextField(componentPanel);
        createRemoveButton(controlPanel);
        createFontMenu(controlPanel);
        createSlider(controlPanel);
    }

    /* Each of the methods below is responsible for constructing a swing component
     * They have JPanel panel as a parameter in their method declaration
     * Significane: so each component knows which panel to be added to
     */

    private void createTextField(JPanel panel) {
        //Constructing a JTextField of 20 characters
        textField = new JTextField(20);

        //Adding textField to corresponding panel, in this case the componentPanel
        panel.add(textField);

    }

    private void createRemoveButton(JPanel panel) {

        removeButton = new JButton("Remove last text");
        panel.add(removeButton);

        //Add action listener to button to "listen" for an action aka a mouse click
        //Pass running instance of TextPlay class
        removeButton.addActionListener(this);
    }

    private void createFontMenu(JPanel panel) {

        pickFont = new JComboBox();

        //Add the menu items aka Font styles to JComboBox
        pickFont.addItem("Courier");
        pickFont.addItem("Helvetica");
        pickFont.addItem("Times Roman");
        pickFont.addItem("Zapfino");
        pickFont.addItem("Geneva");
        pickFont.addItem("Arial");
        pickFont.addItem("Futura");

        panel.add(pickFont);

        //Add action listener and pass running instance of TextPlay class
        pickFont.addActionListener(this);
    }

    private void createSlider(JPanel panel) {

        //Set JSlider to horizontal orientation with min value of 10 and max value of 48
        fontSizeGradient = new JSlider(JSlider.HORIZONTAL, 10, 48, 10);
        panel.add(fontSizeGradient);

        //Add change listener and pass running instance of TextPlay class
        fontSizeGradient.addChangeListener(this);
    }

    /* Creates a text object at point of mouse click */
    public void onMouseClick (Location point) {

        createText(point);
    }

    /* Converts what user types into text field into a text object located at user click point */
    private void createText(Location point) {

        currentText = new Text (textField.getText(), point, canvas);

        //Set font size and font style according to user's selection/manipulation of swing components
        setFontSize();
        setFontStyle();
    }

    /* Sets font size to the corresponding value on JSlider (aka fontSizeGradient) */
    private void setFontSize() {

        fontSize = fontSizeGradient.getValue();
        currentText.setFontSize(fontSize);
    }

    /* Sets font to whichever font/item was selected from combo box (aka PickFont) */
    private void setFontStyle() {

        //Calling toString allows selected item to be converted into a string..
        //which the font can then be set to
        fontStyle = pickFont.getSelectedItem().toString();
        currentText.setFont(fontStyle);
    }

    /* Required declaration of method for the implementation of action listener interface
       Carries out a certain action based on the source of the event */
    public void actionPerformed (ActionEvent e) {

        //Short-circuit the method to avoid having to insert multiple null checks
        //If currentText has not been initialized, thread will exit method here and ignore rest of code
        if (currentText == null)
            return;

        //If removeButton was clicked, remove text from canvas
        //Set text object back to null
        //Significance: to prevent runtime error if user clicks on JButton w/o typing any text
        if(e.getSource() == removeButton) {
            currentText.removeFromCanvas();
            currentText = null;
        }
        //Or, if event source is JComboBox, set font to selected item on menu
        else if(e.getSource() == pickFont) {
            setFontStyle();
        }
    }

    /* Required declaration of method for the implementation of change listener interface */
    public void stateChanged (ChangeEvent e) {

        //Short-circuit so only need to check for null once in the method
        if (currentText == null)
            return;

        //If source of change event is JSlider, set font size of text accordingly
        if(e.getSource() == fontSizeGradient) {
            setFontSize();
        }
    }


}
