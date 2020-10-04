import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
* Class name: CustomJComboBox
*
* Extension of JComboBox class to create a custom JComboBox theme
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/
public class CustomJComboBox<E> extends JComboBox {

    private Color foregroundColor = new Color(41, 47, 54);
    private Color backgroundColor = new Color(255, 250, 240);

    /** Constructor */
    public CustomJComboBox(E[] items)
    {
        super(items);

        //Set JComboBox colors
        setBackground(backgroundColor);
        setForeground(foregroundColor);
    }

  }
