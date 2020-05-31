import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
/**
* Class name: GUI
* GUI creates a graphical interface with five main operations
*
* @author  Christian Autor
* @version 1.0
* @since   5/28/2020
*/
public class GUI extends appActions
{
  private static JFrame mainFrame = new JFrame("NumisList");
  private static JTabbedPane tp = new JTabbedPane();
  private static Color colors[] = {new Color(222, 244, 222), new Color(255, 204, 204), new Color(200, 200, 234), new Color(255, 217, 168), new Color(255, 250, 193)};

  private static String files[] = {"Data\\Logo.png", "Data\\pennies.txt", "Data\\nickels.txt", "Data\\dimes.txt", "Data\\quarters.txt", "Data\\halves.txt", "Data\\dollars.txt"};
  private static String denominations[] = {"Choose Denomination", "Pennies", "Nickles", "Dimes", "Quarters", "Half Dollars", "Dollars"};

  private static JComboBox cb1, cb2, cb3, cb4, cb5, cb6;
  private static JTextField tf1, tf2, tf3, tf4, tf5, tf6, tf7;
  private static JLabel l1, l2, l3, l4, l5, l6, l7;


  /** GUI main method
  *   Creates main JFrame and sets its parameters
  *   Calls methods to set up tabs in the JTabbed Pane
  */
  public GUI(){
    mainFrame.setLayout(new FlowLayout());
    mainFrame.setSize(500, 700);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageIcon img = new ImageIcon(files[0]);
    mainFrame.setIconImage(img.getImage());

    Home();
    updateCollection();
    addRemove();
    edit();
    export();

    mainFrame.add(tp);
    mainFrame.setVisible(true);
  }


  private static JPanel homePanel;
  private static JPanel panels1[];
  private static JLabel coinLabels[];
  private static JButton coinButtons[];
  /** Displays each denomination with number of coins in collection, needed, and total
  *   Adds a button for each denomination which displays all coins
  */
  public void Home()
  {
    //Creates homePanel with box layout
    homePanel = new JPanel();
    homePanel.setBackground(colors[0]);
    homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

    panels1 = new JPanel[16];
    JButton coinButtons[] = new JButton[denominations.length-1];

    coinLabels = new JLabel[denominations.length-1];

    /* Generic JButton action listener for each denominations
    *  Gets denomination and calls home redraw to display info
    */
    ActionListener listener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
          int i = 0;
          while(!(denominations[i].equals(((JButton) e.getSource()).getText()) && i<denominations.length))
            i++;
          homeRedraw(i);
        }
      }
    };

    //Create and add panels to homePanel
    for(int i = 0; i<panels1.length; i++)
    {
      panels1[i] = new JPanel();
      panels1[i].setBackground(colors[0]);
      homePanel.add(panels1[i]);
    }

    //Create calculate and add denomination buttons and labels
    for(int i = 0; i<denominations.length-1; i++)
    {
      coinLabels[i] = new JLabel();
      coinButtons[i] = new JButton(denominations[i+1]);
      coinButtons[i].addActionListener(listener);
      panels1[i].add(coinButtons[i]);
      panels1[i].add(coinLabels[i]);
    }

    /* Recalculates number of coins in collection, needed, and total when home tab is selected */
    tp.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if(tp.getSelectedIndex() == 0)
          homeHelper();
      }
    });

    tp.add("Home", homePanel);
  }


  /** Sets text for each denomination button */
  public void homeHelper()
  {
    int k = 0;
    //Iterate through denominations
    for(int i = 1; i<files.length; i++)
    {
      k = 0;
      super.currList.clear();
      super.fileReadAll(files[i]);
      //Iterate through coin objects and count number of coins in collection
      for(int j = 0; j<super.currList.size(); j++)
      {
        if(super.currList.get(j).getinCollection())
          k++;
      }
      //Set text of coin label
      coinLabels[i-1].setText("Total : " + super.currList.size() + "     In Collection: " + k + "     Needed: " + (super.currList.size()-k));
    }
    super.currList.clear();
  }


  private static JScrollPane homeScrlBx;
  private static JPanel exitPanel;
  /** Populates and adds JScrollPane to display all coins in selected denomination
  *   @param  index index of denomination in files[] to be displayed
  */
  public void homeRedraw(int index)
  {
    //Remove pannel array from homePanel
    for(int i = 0; i<panels1.length; i++)
      homePanel.remove(panels1[i]);
    homePanel.revalidate();
    homePanel.repaint();

    exitPanel = new JPanel();
    exitPanel.setBackground(colors[0]);

    /** Action listener to reset homePanel */
    JButton exit = new JButton("Back");
    exit.setAlignmentX(JButton.CENTER_ALIGNMENT);
    exit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        homePanel.removeAll();
        homePanel.revalidate();
        homePanel.repaint();

        for(int i = 0; i<panels1.length; i++)
          homePanel.add(panels1[i]);
      }
    });

    exitPanel.add(exit);
    homePanel.add(exitPanel);

    super.fileReadAll(files[index]);

    //Set up for JScrollPane containing all coins in selected denomination
    Box homeBox = Box.createVerticalBox();
    homeScrlBx = new JScrollPane(homeBox);
    homeScrlBx.getViewport().setBackground(colors[0]);
    JLabel dispLabels[] = new JLabel[super.currList.size()];

    //Initializes and adds labels for each coin in selected denomination
    //+ = in collection, - = not in collection
    for(int i = 0; i<dispLabels.length; i++)
    {
      dispLabels[i] = new JLabel();
      if(super.currList.get(i).getinCollection())
        dispLabels[i].setText("  +  " + super.currList.get(i).LPrintAll());
      else
        dispLabels[i].setText("   -  " + super.currList.get(i).LPrintAll());

      homeBox.add(dispLabels[i]);
    }

    currList.clear();

    homeScrlBx.setPreferredSize(new Dimension(400, 565));
    homePanel.add(homeScrlBx);

  }


  private static JPanel updatePanel;
  private static JCheckBox updateBoxes[];
  private static Box updateBox;
  private static JScrollPane jscrlpBox;
  private static int boolEdit1;
  private static JButton b4;
  /** Sets up update panel
  *   prompts user to chose denomination and add or remove coins from collection
  *   Calls updateHelper to make changes
  */
  public void updateCollection()
  {
    //Creates update panel
    updatePanel = new JPanel();
    updatePanel.setPreferredSize(new Dimension(400, 600));
    updatePanel.setBackground(colors[1]);

    //editButton state checker
    boolEdit1 = -1;

    //Creates a box in a JScrollPane
    updateBox = Box.createVerticalBox();
    jscrlpBox = new JScrollPane(updateBox);
    jscrlpBox.getViewport().setBackground(colors[1]);


    cb1=new JComboBox(denominations);
    updatePanel.add(cb1);

    /** Action listener for editButton to either enable editing or save changes */
    JButton editButton = new JButton("Click to Edit");
    editButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
          //Check for valid denomination and calls updateHelper
          if(cb1.getSelectedIndex() != 0)
          {
            boolEdit1 = boolEdit1*-1;
            updateHelper();
          }
          //Editing enabled
          if(boolEdit1 == 1)
          {
            updatePanel.remove(cb1);
            editButton.setText("Save Changes");
            updatePanel.add(b4);
            updatePanel.add(jscrlpBox);
            updatePanel.revalidate();
            updatePanel.repaint();
          }
          //Saves changes made
          else if(boolEdit1 == -1)
          {
            updatePanel.remove(editButton);
            updatePanel.remove(b4);
            updatePanel.add(cb1);
            updatePanel.add(editButton);
            editButton.setText("Click to Edit");
            updatePanel.remove(jscrlpBox);
            updatePanel.revalidate();
            updatePanel.repaint();
          }
       }
    }); updatePanel.add(editButton);


    /** Action listener for button to cancel changes and reset update pane */
    b4 = new JButton("Cancel");
    b4.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {

         boolEdit1 = boolEdit1*-1;

         //Remove JCheckBox array from box
         for(int i = 0; i<updateBoxes.length; i++)
           updateBox.remove(updateBoxes[i]);
         currList.clear();
         updatePanel.remove(editButton);
         updatePanel.remove(b4);
         updatePanel.add(cb1);
         updatePanel.add(editButton);
         editButton.setText("Click to Edit");
         updatePanel.remove(jscrlpBox);
         updatePanel.revalidate();
         updatePanel.repaint();
       }
    });


    tp.add("Update Collection",updatePanel);
  }


  /** If boolEdit1 == 1 enable editing of selected denomination
  *   If boolEdit1 == -1 save changes and reset the panel elements
  */
  public void updateHelper()
  {
    //Enable editing
    if(boolEdit1 == 1)
    {
      currList.clear();

      super.fileReadAll(files[cb1.getSelectedIndex()]);

      updateBoxes = new JCheckBox[super.currList.size()];

      //Iterate through currList and add each element to array of JCheckBoxes
      for(int i = 0; i<super.currList.size(); i++)
      {
          updateBoxes[i] = new JCheckBox(super.currList.get(i).LPrint());
          //If current coin is in collection set JCheckBox to true
          if(super.currList.get(i).getinCollection())
            updateBoxes[i].setSelected(true);
          updateBoxes[i].setBackground(colors[1]);
          updateBox.add(updateBoxes[i]);
      }
      jscrlpBox.setPreferredSize(new Dimension(400, 565));
    }

    //Save changes
    else
    {
      //Remove array of JCheckBoxes from updateBox
      for(int i = 0; i<updateBoxes.length; i++)
        updateBox.remove(updateBoxes[i]);

      updateBox.revalidate();
      updateBox.repaint();

      //Make no changes if currList has been cleared during update
      //Can happen if user does a different action before saving changes to list
      if(super.currList.size() == 0)
        return;

      //Iterate through list and update inCollection if there is a change
      for(int i = 0; i<super.currList.size(); i++)
      {
        if (updateBoxes[i].isSelected() && !(super.currList.get(i).getinCollection()))
          super.currList.get(i).editgetinCollection(true);
        else if (!(updateBoxes[i].isSelected()) && (super.currList.get(i).getinCollection()))
          super.currList.get(i).editgetinCollection(false);
      }

      super.fileWriteAll(files[cb1.getSelectedIndex()]);

      super.currList.clear();
    }
  }


  private static JButton b2, b3;
  private static JPanel p3;
  private static int boolEdit2;
  public void addRemove()
  {
    JPanel mainPanel = new JPanel();
    mainPanel.setBackground(colors[2]);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    boolEdit2 = -1;

    cb2 = new JComboBox(denominations);

    JPanel p1 = new JPanel();
    p1.setBackground(colors[2]);
    p1.add(cb2);
    JPanel p2 = new JPanel();
    p2.setBackground(colors[2]);

    JButton b1 = new JButton("Add Coin");
    b1.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         if(!(tf1.getText().equals("") || tf2.getText().equals("") || tf3.getText().equals("") ) && cb2.getSelectedIndex() != 0)
         {
           try
           {
             addHelper();
           }
           catch(NumberFormatException ex)
           {
             return;
           }
         }
       }
    });
    p2.add(b1);


    JLabel l1, l2, l3;

    l1 = new JLabel("Date");
    tf1 = new JTextField(3);
    l2 = new JLabel("Mint Mark");
    tf2 = new JTextField(1);
    l3 = new JLabel("Condition");
    tf3 = new JTextField(1);

    p2.add(l1); p2.add(tf1);
    p2.add(l2); p2.add(tf2);
    p2.add(l3); p2.add(tf3);

    p3 = new JPanel();
    p3.setBackground(colors[2]);



    b2 = new JButton("Search By date");
    b2.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {

           if(boolEdit2 == -1 && !(tf4.getText().equals("")) && cb2.getSelectedIndex() != 0)
           {
             try
             {
               removeHelper();
               boolEdit2 = boolEdit2*-1;
             }
             catch(NumberFormatException ex)
             {
               return;
             }
           }

           else if((boolEdit2 == 1 && cb2.getSelectedIndex() != 0) && cb3.getSelectedIndex() != 0)
           {
             delete();
             boolEdit2 = boolEdit2*-1;
           }
       }
    }); p3.add(b2);

    b3 = new JButton("Cancel");
    b3.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
           boolEdit2 = boolEdit2*-1;
           p3.remove(b3);
           p3.remove(cb3);
           b2.setText("Search By Date");
           p3.add(l4);
           p3.add(tf4);
           p3.revalidate();
           p3.repaint();
       }
    });


    l4 = new JLabel("Date");
    tf4 = new JTextField(3);


    p3.add(l4); p3.add(tf4);


    mainPanel.add(p1);
    mainPanel.add(p2);
    mainPanel.add(p3);

    JPanel[] jPA = new JPanel[10];
    for(int i = 0; i<9; i++)
    {
      jPA[i] = new JPanel();
      jPA[i].setBackground(colors[2]);
      mainPanel.add(jPA[i]);
    }


    tp.add("Add/Remove",mainPanel);
  }


  /** Creates and inserts new coin object into currList */
  public void addHelper()
  {
    super.currList.clear();
    super.fileReadAll(files[cb2.getSelectedIndex()]);
    super.listInsert(new coin(Integer.parseInt(tf1.getText()), tf2.getText().toUpperCase(), false, tf3.getText(), "null"));
    super.fileWriteAll(files[cb2.getSelectedIndex()]);
    super.currList.clear();

    tf1.setText("");
    tf2.setText("");
    tf3.setText("");
  }


  private static coin[] coinArray;
  private static String[] displayArray;
  /** Prompts user to pick a coin to delete from a list of coins matching the user inputted date */
  public void removeHelper()
  {

    //Get date from JTextField
    int date = Integer.parseInt(tf4.getText());


    super.currList.clear();

    super.fileReadAll(files[cb2.getSelectedIndex()]);

    //Iterate through currList and count number of coins matching user inputted date
    int count = 0;
    for(int j = 0; j<super.currList.size(); j++)
    {
      if (date == super.currList.get(j).getdate())
        count++;
    }

    coinArray = new coin[count+1];
    displayArray = new String[count+1];
    displayArray[0] = "Choose coin to delete";

    //Iterate through currList and add coins matching user inputted date to coin object and String arrays
    count = 1;
    for(int j = 0; j<super.currList.size(); j++)
    {
      if (date == super.currList.get(j).getdate())
      {
        coinArray[count] = super.currList.get(j);
        displayArray[count] = super.currList.get(j).LPrintAll();
        count++;
      }
    }

    cb3 = new JComboBox(displayArray);

    p3.remove(b2);
    p3.add(b3);
    p3.add(b2);

    b2.setText("Delete");

    p3.add(cb3);
    p3.remove(tf4);
    p3.remove(l4);
    p3.revalidate();
    p3.repaint();

    tf4.setText("");
  }


  /** Removes selected coin and resets addRemove to default state */
  public void delete()
  {
    p3.remove(b3);
    p3.remove(cb3);
    b2.setText("Search By date");
    p3.add(l4);
    p3.add(tf4);
    p3.revalidate();
    p3.repaint();


    //Remove selected coin from currList
    for(int i = 1; i < displayArray.length; i++)
    {
      if(displayArray[i].equals(cb3.getItemAt(cb3.getSelectedIndex()) + ""))
        super.listRemove(coinArray[i]);
    }

    super.fileWriteAll(files[cb2.getSelectedIndex()]);
    super.currList.clear();
  }


  JPanel editPanel;
  JPanel panels2[];
  JButton search;
  /** Creates Edit tab which prompts user for a date to search for
  *   Calls editHelper when a date is inputted
  */
  public void edit()
  {
    //Set up JPanel with box layout
    editPanel = new JPanel();
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
    editPanel.setBackground(colors[3]);

    //Create and add array of JPannels to editPanel
    panels2 = new JPanel[12];
    for(int i = 0; i<panels2.length; i++)
    {
      panels2[i] = new JPanel();
      panels2[i].setBackground(colors[3]);
      editPanel.add(panels2[i]);
    }

    cb4 = new JComboBox(denominations);
    panels2[0].add(cb4);

    search = new JButton("Search By Date");
    panels2[1].add(search);

    l5 = new JLabel("Date");
    panels2[1].add(l5);

    tf5 = new JTextField(3);
    panels2[1].add(tf5);

    /** Takes date input from user and catches invalid input */
    search.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //Check for valid denomination
        if(!(tf5.getText().equals("")) && cb4.getSelectedIndex() != 0)
        {
          try
          { editHelper(); }
          catch(NumberFormatException ex)
          { return; }
        }
      }
    });


    tp.add("Edit", editPanel);
  }


  private static coin[] coinArray2;
  private static String[] displayArray2;
  private static JButton submit;
  private static int currFile;
  /** editHelper is called when a date and denomination are inputted to search and editPanel
  *   Reformats the panel, prompts user to select and edit attributes and condition of a coin
  */
  public void editHelper()
  {
    JButton cancel = new JButton("Cancel");
    JButton select = new JButton("Select");
    submit = new JButton("Submit");

    //Get selected coin
    currFile = cb4.getSelectedIndex();

    //Get date input and reset text field
    int date = Integer.parseInt(tf5.getText());
    tf5.setText("");

    super.currList.clear();

    super.fileReadAll(files[currFile]);

    //Counts number of coins matching the user inputted date
    int count = 0;
    for(int j = 0; j<super.currList.size(); j++)
    {
      if (date == super.currList.get(j).getdate())
        count++;
    }

    //Text and coin object arrays of coins matching the user inputted date
    coinArray2 = new coin[count+1];
    displayArray2 = new String[count+1];
    displayArray2[0] = "Choose Coin To Edit";
    count = 1;

    //Populate arrays with coins matching the user inputted date
    for(int j = 0; j<super.currList.size(); j++)
    {
      if (date == super.currList.get(j).getdate())
      {
        coinArray2[count] = super.currList.get(j);
        displayArray2[count] = super.currList.get(j).LPrintAll();
        count++;
      }
    }

    cb5 = new JComboBox(displayArray2);

    panels2[1].removeAll();
    panels2[1].repaint();
    panels2[1].revalidate();
    panels2[1].add(cancel);
    panels2[1].add(select);
    panels2[1].add(cb5);


    tf6 = new JTextField(2);
    l6 = new JLabel("Condition");
    tf7 = new JTextField(4);
    l7 = new JLabel("Attributes");

    /** Cancel button action listener resets panels2[1] to its default state */
    cancel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panels2[1].removeAll();
        panels2[1].repaint();
        panels2[1].revalidate();
        panels2[1].add(search);
        panels2[1].add(l5);
        panels2[1].add(tf5);
      }
    });

    /** Select action listener
    *   Gets selected coin and sets text fields to attributes and condition respectively
    */
    select.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //Check for valid denomination selection
        if(!(displayArray2[0].equals(cb5.getItemAt(cb5.getSelectedIndex()) + "")))
        {
          coin selectedCoin = coinArray2[cb5.getSelectedIndex()];

          panels2[1].remove(select);
          panels2[1].remove(cb5);
          panels2[1].repaint();
          panels2[1].revalidate();

          panels2[1].add(submit);
          panels2[1].add(l6);
          panels2[1].add(tf6);
          if(!(selectedCoin.getcondition().equals("null")))
            tf6.setText(selectedCoin.getcondition());
          panels2[1].add(l7);
          panels2[1].add(tf7);
          if(!(selectedCoin.getattributes().equals("null")))
            tf7.setText(selectedCoin.getattributes());
        }
      }
    });

    /** Submit action listener
    *   Modifies and submits attributes and condition of selected coin
    */
    submit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //Get coin selection from JComboBox
        coin selectedCoin = coinArray2[cb5.getSelectedIndex()];

        //Get and set condition
        if(!(tf6.getText().equals("")))
          selectedCoin.editcondition(tf6.getText());
        else
          selectedCoin.editcondition("null");

        //Get and set attributes
        if(!(tf7.getText().equals("")))
          selectedCoin.editattributes(tf7.getText());
        else
          selectedCoin.editattributes("null");

        tf6.setText("");
        tf7.setText("");
        fileWriteAll(files[currFile]);
       }
    });
  }


  /** Creates Export tab which exports selected file to export.txt file in a human readable format */
  public void export()
  {
    JPanel exportPanel = new JPanel();
    exportPanel.setBackground(colors[4]);

    JButton exportButton = new JButton("Export List");
    exportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(cb6.getSelectedIndex() != 0)
        {
           fileReadAll(files[cb6.getSelectedIndex()]);
           fileExport("Data\\export.txt", cb6.getItemAt(cb6.getSelectedIndex()) + "");
           currList.clear();
        }
      }
    });

    exportPanel.add(exportButton);
    cb6 = new JComboBox(denominations);
    exportPanel.add(cb6);

    tp.add("Export", exportPanel);
  }

}
