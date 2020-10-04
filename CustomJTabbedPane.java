import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
* Class name: CustomJTabbedPane
*
* Extension of JTabbedPane class to create a custom JTabbedPane theme
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/
public class CustomJTabbedPane extends JTabbedPane {

    private Color primaryForegroundColor = new Color(255, 250, 240);
    private Color secondaryForegroundColor = new Color(255, 228, 133);
    private Color backgroundColor = new Color(0, 145, 110);

    /** Constructor */
    public CustomJTabbedPane()
    {
        super();

        //Set JTabbedPane colors
        setForeground(primaryForegroundColor);
        setBackground(backgroundColor);
    }

    /** Set color of active tab in JTabbedPane
    *   @param  index of tab in JTabbedPane
    *   @return new foreground color of active tab
    */
    public Color getForegroundAt(int index)
    {
        if(getSelectedIndex() == index) return secondaryForegroundColor;
          return primaryForegroundColor;
    }
}
