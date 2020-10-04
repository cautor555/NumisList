import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
* Class name: CustomJCheckBox
*
* Extension of JCheckBox class to create a custom JCheckBox theme
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/
public class CustomJCheckBox extends JCheckBox {

    /** Constructor */
    public CustomJCheckBox()
    {
        super();

        //Set JCheckBox icons
        setIcon(new ImageIcon("Data\\CheckboxDeselected.png"));
        setSelectedIcon(new ImageIcon("Data\\CheckboxSelected.png"));
        setRolloverIcon(new ImageIcon("Data\\CheckboxDeselectedHighlighted.png"));
        setRolloverSelectedIcon(new ImageIcon("Data\\CheckboxSelectedHighlighted.png"));
        setPressedIcon(new ImageIcon("Data\\CheckboxDeselectedHighlighted.png"));
    }
}
