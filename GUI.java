import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.io.File;
/**
* Class name: GUI
* GUI creates a graphical interface with four tabbed panes
*
* @author  Christian Autor
* @version 2.1
* @since   10/4/2020
*/
public class GUI extends appActions
{

  /** Variable declaration */

  private static JFrame mainFrame;
  private static CustomJTabbedPane tp;

  private static Color backgroundColor = new Color(41, 47, 54);
  private static Color primaryColor = new Color(0, 145, 110);
  private static Color textColor = new Color(255, 250, 240);
  private static Color highlightColor = new Color(255, 228, 133);


  private static String files[] = {"Data\\Logo.png", "Data\\cents.txt", "Data\\nickels.txt", "Data\\dimes.txt", "Data\\quarters.txt", "Data\\halves.txt", "Data\\dollars.txt"};
  private static String denominations[] = {"Choose Denomination", "Cents", "Nickles", "Dimes", "Quarters", "Half Dollars", "Dollars"};

  private static CustomJComboBox updateCB, addCoinCB, exportCB;
  private static JTextField addCoinDateTF, addCoinmMarkTF, editConditionTF, editAttributesTF;
  private static CustomJLabel addCoinDateLabel, addCoinmMarkLabel, editConditionLabel, editAttributesLabel;


  /** GUI main method
  *   creates main JFrame and sets its parameters
  *   calls methods to set up tabs in the JTabbed Pane
  */
  public GUI(){
    mainFrame = new JFrame("NumisList");
    mainFrame.setLayout(new FlowLayout());
    mainFrame.setSize(500, 700);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    ImageIcon img = new ImageIcon(files[0]);
    mainFrame.setIconImage(img.getImage());


    UIManager.put("TabbedPane.contentAreaColor", primaryColor);
    UIManager.put("TabbedPane.selected", primaryColor);

    tp = new CustomJTabbedPane();


    /* Recalculates number of coins in collection, needed, and total when home tab is selected */
    tp.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent e) {
        if(tp.getSelectedIndex() == 0)
          homeHelper();
      }
    });


    home();
    updateCollection();
    addCoin();
    export();

    mainFrame.add(tp);
    mainFrame.setVisible(true);
  }


  private static JPanel homePanel;
  private static JPanel homepanelarray[];
  private static CustomJLabel homeCoinLabels[];
  private static CustomJButton homeCoinButtons[];
  /** Displays each denomination with number of coins in collection, number of coins needed, and total number of coins
  *   Adds a button for each denomination which displays all coins when selected
  */
  public void home()
  {
    //Creates homePanel with box layout
    homePanel = new JPanel();
    homePanel.setBackground(backgroundColor);
    homePanel.setLayout(new BoxLayout(homePanel, BoxLayout.Y_AXIS));

    homepanelarray = new JPanel[12];
    CustomJButton homeCoinButtons[] = new CustomJButton[denominations.length-1];

    homeCoinLabels = new CustomJLabel[denominations.length-1];

    /* Generic JButton action listener for each denomination button
    *  Gets denomination and calls home redraw to display info
    */
    ActionListener homeListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
          //Get denomination
          int i = 0;
          while(!(denominations[i].equals(((JButton) e.getSource()).getText()) && i<denominations.length))
            i++;
          homeRedraw(i);
        }
      }
    };

    //Create and add panels to homePanel
    for(int i = 0; i<homepanelarray.length; i++)
    {
      homepanelarray[i] = new JPanel(new FlowLayout(FlowLayout.LEFT));
      homepanelarray[i].setBackground(backgroundColor);
      homePanel.add(homepanelarray[i]);
    }

    //Create calculate and add denomination buttons and labels
    for(int i = 0; i<denominations.length-1; i++)
    {
      homeCoinLabels[i] = new CustomJLabel();

      homeCoinButtons[i] = new CustomJButton(denominations[i+1]);
      homeCoinButtons[i].setPreferredSize(new Dimension(100, 30));
      homeCoinButtons[i].addActionListener(homeListener);

      homepanelarray[i].add(new JLabel("  "));
      homepanelarray[i].add(homeCoinButtons[i]);
      homepanelarray[i].add(homeCoinLabels[i]);
    }

    tp.add("Home", homePanel);
  }


  /** Sets text for each denomination button */
  public void homeHelper()
  {
    int k;
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
      homeCoinLabels[i-1].setText("Total : " + super.currList.size() + "     In Collection: " + k + "     Needed: " + (super.currList.size()-k));
    }
    super.currList.clear();
  }


  private static JScrollPane homeScrlBx;
  private static JPanel homeExitPanel;
  /** Populates and adds JScrollPane to display all coins in selected denomination
  *   @param  index index of denomination in files[] to be displayed
  */
  public void homeRedraw(int index)
  {
    //Remove pannel array from homePanel
    for(int i = 0; i<homepanelarray.length; i++)
      homePanel.remove(homepanelarray[i]);
    homePanel.revalidate();
    homePanel.repaint();

    homeExitPanel = new JPanel();
    homeExitPanel.setBackground(backgroundColor);

    /** Action listener to reset homePanel */
    CustomJButton homeExit = new CustomJButton("Back");
    homeExit.setPreferredSize(new Dimension(100, 30));
    homeExit.setAlignmentX(JButton.CENTER_ALIGNMENT);
    homeExit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        homePanel.removeAll();
        homePanel.revalidate();
        homePanel.repaint();

        for(int i = 0; i<homepanelarray.length; i++)
          homePanel.add(homepanelarray[i]);
      }
    });

    homeExitPanel.add(homeExit);
    homePanel.add(homeExitPanel);

    super.currList.clear();
    super.fileReadAll(files[index]);

    //Set up for JScrollPane containing all coins in selected denomination
    Box homeBox = Box.createVerticalBox();
    homeScrlBx = new JScrollPane(homeBox);
    homeScrlBx.getViewport().setBackground(backgroundColor);
    homeScrlBx.getVerticalScrollBar().setUnitIncrement(20);
    CustomJLabel homeDispLabels[] = new CustomJLabel[super.currList.size()];

    //Initializes and adds labels for each coin in selected denomination
    //If a coin is in collection prepend it with a "+" otherwise prepend it with a "-"
    for(int i = 0; i<homeDispLabels.length; i++)
    {
      homeDispLabels[i] = new CustomJLabel();
      if(super.currList.get(i).getinCollection())
        homeDispLabels[i].setText("  +  " + super.currList.get(i).LPrintAll());
      else
        homeDispLabels[i].setText("   -  " + super.currList.get(i).LPrintAll());

      homeBox.add(homeDispLabels[i]);
    }

    currList.clear();

    homeScrlBx.setPreferredSize(new Dimension(400, 565));
    homePanel.add(homeScrlBx);

  }


  private static JPanel updatePanel;
  private static JPanel updatePanels[];
  private static CustomJCheckBox updateBoxes[];
  private static CustomJButton updateEditButton, updateSaveButton, updateCancelButton;
  private static CustomJButton updateButtons[];
  private static CustomJLabel updateCoinLabels[];
  private static Box updateBox;
  private static JScrollPane updateScrlBox;
  private static ActionListener updateListener;
  /** Sets up update panel
  *   Prompts user to chose denomination mark coins as in or not in collection
  *   Creates an edit button for each coin
  */
  public void updateCollection()
  {
    //Creates update panel
    updatePanel = new JPanel();
    updatePanel.setPreferredSize(new Dimension(400, 600));
    updatePanel.setBackground(backgroundColor);

    //Creates a box in a JScrollPane
    updateBox = Box.createVerticalBox();
    updateScrlBox = new JScrollPane(updateBox);
    updateScrlBox.getViewport().setBackground(backgroundColor);
    updateScrlBox.getVerticalScrollBar().setUnitIncrement(20);

    //Creates a JComboBox to display coin denominations
    updateCB = new CustomJComboBox(denominations);
    updatePanel.add(updateCB);

    /** Action listener triggered by choosing to edit a coin */
    updateListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        //Get instance of edit by looping through updateButtons and comparing to the ActionEvent
        if (e.getSource() instanceof JButton) {
          int i = 0;
          while(i+1<currList.size() && updateButtons[i] != e.getSource())
            i++;
          //Clear update panel
          updatePanel.removeAll();
          updatePanel.revalidate();
          updatePanel.repaint();

          edit(i);  //pass the coin to be modified to edit()
        }
      }
    };

    /** Action listener for updateEditButton to edit a coin */
    updateEditButton = new CustomJButton("Click to Edit");
    updateEditButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {

          if(updateCB.getSelectedIndex() != 0)
          {
            currList.clear();
            fileReadAll(files[updateCB.getSelectedIndex()]);
            updateReset();

            updateScrlBox.setPreferredSize(new Dimension(400, 565));

            //Clears updatePanel and adds the JScrollPane
            updatePanel.remove(updateCB);
            updatePanel.remove(updateEditButton);
            updatePanel.add(updateSaveButton);
            updatePanel.add(updateCancelButton);
            updatePanel.add(updateScrlBox);
            updatePanel.revalidate();
            updatePanel.repaint();
          }
       }
    });

    updatePanel.add(updateEditButton);


    /** Action listener for updateSaveButton to save changes made by selecting or deselecting coins */
    updateSaveButton = new CustomJButton("Save Changes");
    updateSaveButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {

         //Remove JCheckBox array from box
        for(int i = 0; i<updateBoxes.length; i++)
          updateBox.remove(updatePanels[i]);

        updateBox.revalidate();
        updateBox.repaint();

        //Make no changes if currList has been cleared during update
        //Can happen if user does a different action before saving changes to list
        if(currList.size() == 0)
          return;

        //Iterate through list and update inCollection if there is a change
        for(int i = 0; i<currList.size(); i++)
        {
          if (updateBoxes[i].isSelected() && !(currList.get(i).getinCollection()))
            currList.get(i).editgetinCollection(true);
          else if (!(updateBoxes[i].isSelected()) && (currList.get(i).getinCollection()))
            currList.get(i).editgetinCollection(false);
        }

        fileWriteAll(files[updateCB.getSelectedIndex()]);

        currList.clear();

        //Resets updatePanel
        updatePanel.removeAll();
        updatePanel.add(updateCB);
        updatePanel.add(updateEditButton);
        updatePanel.revalidate();
        updatePanel.repaint();
      }
    });


    /** Action listener for button to cancel changes and reset update pane */
    updateCancelButton = new CustomJButton("Cancel");
    updateCancelButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {

         //Remove JCheckBox array from box
         for(int i = 0; i<updateBoxes.length; i++)
           updateBox.remove(updatePanels[i]);


         currList.clear();
         updatePanel.removeAll();
         updatePanel.add(updateCB);
         updatePanel.add(updateEditButton);
         updatePanel.revalidate();
         updatePanel.repaint();
       }
    });

    tp.add("Update Collection",updatePanel);

  }


  JPanel editPanel;
  CustomJButton deletePictureButtons[];
  /** Sets up editPanel and allows user to add images, delete selected coin, or change attributes and condition of the selected coin
  *   @param  coinIndex coin selected to edit
  */
  public void edit(int coinIndex)
  {
    //Creates editPanel with a box layout
    editPanel = new JPanel();
    editPanel.setBackground(backgroundColor);
    editPanel.setLayout(new BoxLayout(editPanel, BoxLayout.Y_AXIS));
    updatePanel.add(editPanel);

    JPanel editPanels[] = new JPanel[12];

    for(int k = 0; k<editPanels.length; k++)
    {
      editPanels[k] = new JPanel();
      editPanels[k].setBackground(backgroundColor);
      editPanel.add(editPanels[k]);
    }

    CustomJLabel coin = new CustomJLabel(currList.get(coinIndex).getdate() + " " + currList.get(coinIndex).getmMark() + "  ");
    editPanels[1].add(coin);

    /** Button to save changes made to a coin */
    CustomJButton submit = new CustomJButton("Save Changes");
    submit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        currList.clear();
        fileReadAll(files[updateCB.getSelectedIndex()]);

        //Get and set condition
        if(!(editConditionTF.getText().equals("")))
          currList.get(coinIndex).editcondition(editConditionTF.getText());
        else
          currList.get(coinIndex).editcondition("null");

        //Get and set attributes
        if(!(editAttributesTF.getText().equals("")))
          currList.get(coinIndex).editattributes(editAttributesTF.getText());
        else
          currList.get(coinIndex).editattributes("null");

        fileWriteAll(files[updateCB.getSelectedIndex()]);
       }
    });

    /** Button to cancel making changes to a coin */
    CustomJButton back = new CustomJButton(" Back ");
    back.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        currList.clear();
        fileReadAll(files[updateCB.getSelectedIndex()]);

        updatePanel.remove(editPanel);

        updateReset();

        updatePanel.add(updateSaveButton);
        updatePanel.add(updateCancelButton);
        updatePanel.add(updateScrlBox);
        updatePanel.revalidate();
        updatePanel.repaint();
       }
    });

    /** Button to delete a coin from list */
    CustomJButton remove = new CustomJButton("Delete");
    remove.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        currList.clear();
        fileReadAll(files[updateCB.getSelectedIndex()]);
        listRemove(currList.get(coinIndex));
        fileWriteAll(files[updateCB.getSelectedIndex()]);

        updatePanel.remove(editPanel);

        updateReset();

        updatePanel.add(updateSaveButton);
        updatePanel.add(updateCancelButton);
        updatePanel.add(updateScrlBox);
        updatePanel.revalidate();
        updatePanel.repaint();
       }
    });

    /** Button to add an image file to a coin */
    CustomJButton addPhoto = new CustomJButton("Add Photo");
    addPhoto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        //Open downloads directory in file choser
        String userhome = System.getProperty("user.home");
        JFileChooser fc = new JFileChooser(userhome +"\\Downloads");

        fc.showSaveDialog(null);

        currList.clear();
        fileReadAll(files[updateCB.getSelectedIndex()]);

          //Creates a copy of and renames the selected file in the appropriate folder
          try
          {
            //Renames the copied file using a unique string to describe the coin and the first available integer > 0
            File sourceFile = fc.getSelectedFile();
            File destFile = new File("Data\\" + denominations[updateCB.getSelectedIndex()] + "\\" + currList.get(coinIndex).LPrint() + "(" + currList.get(coinIndex).nameImage() + ")");
            Files.copy( sourceFile.toPath(), destFile.toPath() );

            currList.get(coinIndex).images.add(destFile.getPath());

            fileWriteAll(files[updateCB.getSelectedIndex()]);

            //Refresh edit panel to display new image
            updatePanel.removeAll();
            edit(coinIndex);
          }
          catch(Exception exc)
          {
            //Create directory if one doesn't exist
            new File("Data\\" + denominations[updateCB.getSelectedIndex()]).mkdirs();
          }
       }
    });

    /** Listener to delete an image file from a coin */
    ActionListener editListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {

          currList.clear();
          fileReadAll(files[updateCB.getSelectedIndex()]);

          //Get index of button selected
          int k = 0;
          while(k<(currList.get(coinIndex).images.size()) && deletePictureButtons[k] != e.getSource())
            k++;

          File file = new File(currList.get(coinIndex).images.get(k));

          //Delete image from directory
          if(file.delete())
            System.out.println("File deleted successfully");

          else
            System.out.println("Failed to delete the file");

          currList.get(coinIndex).images.remove(k); //remove image from list

          //Write changes to file and refresh edit panel
          fileWriteAll(files[updateCB.getSelectedIndex()]);
          updatePanel.removeAll();
          edit(coinIndex);
        }
      }
    };

    editPanels[0].add(back);
    editPanels[0].add(submit);
    editPanels[0].add(remove);
    editPanels[2].add(addPhoto);

    editConditionTF = new JTextField(4);
    editConditionLabel = new CustomJLabel("Condition");
    editAttributesTF = new JTextField(10);
    editAttributesLabel = new CustomJLabel("Attributes");

    if(!(currList.get(coinIndex).getcondition().equals("null")))
      editConditionTF.setText(currList.get(coinIndex).getcondition());

    if(!(currList.get(coinIndex).getattributes().equals("null")))
      editAttributesTF.setText(currList.get(coinIndex).getattributes());

    editPanels[1].add(editConditionLabel);
    editPanels[1].add(editConditionTF);
    editPanels[1].add(editAttributesLabel);
    editPanels[1].add(editAttributesTF);

    //JScrollpane containing coin images
    Box imgBox = Box.createVerticalBox();
    JScrollPane imgScrlBx = new JScrollPane(imgBox);
    imgScrlBx.getViewport().setBackground(backgroundColor);
    imgScrlBx.getVerticalScrollBar().setUnitIncrement(20);
    imgScrlBx.setPreferredSize(new Dimension(400, 490));

    editPanels[3].add(imgScrlBx);

    deletePictureButtons = new CustomJButton[currList.get(coinIndex).images.size()];

    imgBox.add(new JLabel("\t\t\t\t"));

    //Adds images to JScrollPane
    for(int k = 0; k<(currList.get(coinIndex).images.size()); k++)
    {
      ImageIcon imageIcon = new ImageIcon(new ImageIcon(currList.get(coinIndex).images.get(k)).getImage().getScaledInstance(350, 350, Image.SCALE_DEFAULT));
      CustomJLabel label = new CustomJLabel();
      label.setIcon(imageIcon);
      label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
      imgBox.add(label);

      imgBox.add(new JLabel("\t\t\t\t"));

      deletePictureButtons[k] = new CustomJButton("Remove Image");
      deletePictureButtons[k].setAlignmentX(JLabel.CENTER_ALIGNMENT);
      deletePictureButtons[k].addActionListener(editListener);
      imgBox.add(deletePictureButtons[k]);

      imgBox.add(new JLabel("\t\t\t\t"));

    }
  }


  /** Resets and refreshes update pannel */
  public void updateReset()
  {
    //clears update box and redefines variables used in the update panel
    updateBox.removeAll();
    updateBoxes = new CustomJCheckBox[currList.size()];
    updateButtons = new CustomJButton[currList.size()];
    updatePanels = new JPanel[currList.size()];
    updateCoinLabels = new CustomJLabel[currList.size()];

    for(int i = 0; i<currList.size(); i++)
    {
        updatePanels[i] = new JPanel();

        updatePanels[i].setBackground(backgroundColor);
        updatePanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
        updateBoxes[i] = new CustomJCheckBox();

        updateButtons[i] = new CustomJButton("Edit");
        updateButtons[i].addActionListener(updateListener);
        updateCoinLabels[i] = new CustomJLabel(currList.get(i).LPrint());

        if(currList.get(i).getinCollection()) //If current coin is in collection set JCheckBox to true
          updateBoxes[i].setSelected(true);
        updateBoxes[i].setBackground(backgroundColor);
        updatePanels[i].add(updateBoxes[i]);
        updatePanels[i].add(updateButtons[i]);
        updatePanels[i].add(updateCoinLabels[i]);
        updateBox.add(updatePanels[i]);
    }
  }


  /** Sets up add coin panel
  *   prompts user to chose denomination, date, and mint mark of coin to add to collection
  *   Calls addHelper to make changes
  */
  public void addCoin()
  {
    //Creates addCoinPanel with a box layout
    JPanel addCoinPanel = new JPanel();
    addCoinPanel.setBackground(backgroundColor);
    addCoinPanel.setLayout(new BoxLayout(addCoinPanel, BoxLayout.Y_AXIS));

    JPanel addCoinPannels[] = new JPanel[14];
    for(int i = 0; i<addCoinPannels.length; i++)
    {
      addCoinPannels[i] = new JPanel();
      addCoinPannels[i].setBackground(backgroundColor);
      addCoinPanel.add(addCoinPannels[i]);
    }

    addCoinCB = new CustomJComboBox(denominations);

    addCoinPannels[0].add(addCoinCB);

    /** Button to add a coin to collection */
    CustomJButton addCoinButton = new CustomJButton("Add Coin");
    addCoinButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         //If input is valid add coin to list
         if(!(addCoinDateTF.getText().equals("") || addCoinmMarkTF.getText().equals("")) && addCoinCB.getSelectedIndex() != 0)
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

    addCoinPannels[1].add(addCoinButton);

    addCoinDateLabel = new CustomJLabel("Date");
    addCoinDateTF = new JTextField(4);
    addCoinmMarkLabel = new CustomJLabel("Mint Mark");
    addCoinmMarkTF = new JTextField(2);

    addCoinPannels[1].add(addCoinDateLabel);
    addCoinPannels[1].add(addCoinDateTF);
    addCoinPannels[1].add(addCoinmMarkLabel);
    addCoinPannels[1].add(addCoinmMarkTF);

    tp.add("Add Coin",addCoinPanel);
  }


  /** Creates and inserts new coin object into currList */
  public void addHelper()
  {
    super.currList.clear();
    super.fileReadAll(files[addCoinCB.getSelectedIndex()]);
    super.listInsert(new coin(Integer.parseInt(addCoinDateTF.getText()), addCoinmMarkTF.getText().toUpperCase(), false));
    super.fileWriteAll(files[addCoinCB.getSelectedIndex()]);
    super.currList.clear();

    addCoinDateTF.setText("");
    addCoinmMarkTF.setText("");
  }


  /** Creates Export tab which exports selected file(s) to export.txt file in a human readable format */
  public void export()
  {
    JPanel exportPanel = new JPanel();
    exportPanel.setBackground(backgroundColor);

    /** Button to export all denominations */
    CustomJButton exportAllButton = new CustomJButton("Export All");
    exportAllButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        fileClear("Data\\export.txt");

        //Write each denomination list to export file
        for(int i = 1; i<files.length; i++)
        {
          fileReadAll(files[i]);
          fileExport("Data\\export.txt", denominations[i] + "", true);
          currList.clear();
        }
      }
    });

    /** Button to export a specific denomination */
    CustomJButton exportButton = new CustomJButton("Export List");
    exportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {

        //Get denomination and write list to export file
        if(exportCB.getSelectedIndex() != 0)
        {
           fileReadAll(files[exportCB.getSelectedIndex()]);
           fileExport("Data\\export.txt", exportCB.getItemAt(exportCB.getSelectedIndex()) + "", false);
           currList.clear();
        }
      }
    });

    exportPanel.add(exportAllButton);
    exportPanel.add(exportButton);

    exportCB = new CustomJComboBox(denominations);
    exportPanel.add(exportCB);

    tp.add("Export", exportPanel);
  }

}
