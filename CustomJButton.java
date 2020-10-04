import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
* Class name: CustomJButton
*
* Extension of JButton class to create a custom JButton theme
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/

public class CustomJButton extends JButton {

    private Color foregroundColor = new Color(255, 250, 240);
    private Color highlightedForegroundColor = new Color(255, 228, 133);
    private Color backgroundColor = new Color(0, 145, 110);

    /** Constructor */
    public CustomJButton(String label)
    {
        super(label);

        //Turns off default JButton highlight and selection
        setContentAreaFilled(false);
        setOpaque(true);
        setFocusPainted(false);

        buttonSetup(this);
    }

    /** Constructor */
    public CustomJButton()
    {
        this("");
    }

    /** Set JButton colors
    *   @param  J current JButton object
    */
    public void buttonSetup(CustomJButton J)
    {
        setBackground(backgroundColor);
        setForeground(foregroundColor);

        //Set JButton color to highlighted on mouse click
        addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent me) {
            setForeground(highlightedForegroundColor);
         }
         //Reset JButton color when mouse exits button bounds
         public void mouseExited(MouseEvent me) {
            setForeground(foregroundColor);
         }
         //Reset JButton color when mouse click is released
         public void mouseReleased(MouseEvent e) {
           setForeground(foregroundColor);
         }
      });
    }

  }
