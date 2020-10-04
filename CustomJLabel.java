import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
* Class name: CustomJLabel
*
* Extension of JLabel class to create a custom JLabel theme
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/
public class CustomJLabel extends JLabel {

    private Color labelColor = new Color(255, 250, 240);

    /** Constructor */
    public CustomJLabel(String label)
    {
        super(label);

        //Set JLabel colors
        setForeground(labelColor);
    }

    /** Constructor */
    public CustomJLabel()
    {
        this("");
    }

  }
