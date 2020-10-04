import java.io.*;
import java.util.*;
/**
* Class name: appActions
* appActions is made up of file and list operation methods utilized by the GUI class.
*
* @author  Christian Autor
* @version 2.1
* @since   9/7/2020
*/
public class appActions
{
    //Working Linked List for handeling current list of coins
    public static LinkedList<coin> currList = new LinkedList<coin>();


/** File Operations */


    /** Read and store all coin objects from a file in currList
    *   @param  fileName file to be read
    */
    public static void fileReadAll(String fileName)
    {
      try
      {
        FileInputStream file = new FileInputStream(fileName);
        ObjectInputStream in = new ObjectInputStream(file);

        Object serializedList = in.readObject();  //Deserialize input from selected file
        currList = (LinkedList<coin>)serializedList;  //Cast object to a linked list of coins and set it to currList

        in.close();
        file.close();

      }
      catch(ClassNotFoundException Ex)
      {
        System.out.println("error");
      }
      catch(IOException Ex)
      {
        System.out.println("error");
      }
    }

    /** Write currList to file in serial
    *   @param  fileName file to be written to
    */
    public static void fileWriteAll(String fileName)
    {
      try
      {
        FileOutputStream fileOut = new FileOutputStream(fileName);
        ObjectOutputStream output = new ObjectOutputStream(fileOut);
        output.writeObject(currList); //writes entire instance of currList to file in serial
        output.close();
      }
      catch(IOException ex)
      {
        System.out.println("error");
      }
    }

    /** Export all coin objects in currList to file in a human readable format
    *   @param  fileName file to be written to
    *   @param  Denomination denomination of coins in fileName
    *   @param  append marks if fileWriter will write over the file or append to it
    */
    public static void fileExport(String fileName, String Denomination, boolean append)
    {
      try
      {
        FileWriter fileWriter = new FileWriter(fileName, append);
        PrintWriter output = new PrintWriter(fileWriter);

        output.print(Denomination + "\n\n");  //print denomination to file

        //If a coin is in collection prepend it with a "+" otherwise prepend it with a "-"
        for(int i = 0; i<currList.size(); i++)
        {
          if(currList.get(i).getinCollection())
            output.print("  +  " + currList.get(i).LPrintAll() + "\n");
          else
            output.print("  -  " + currList.get(i).LPrintAll() + "\n");
        }
        output.close();
      }
      catch(IOException ex)
      {
        System.out.println("error");
      }
    }

    /** Clears selected file
    *   @param  fileName file to be written to
    */
    public static void fileClear(String fileName)
    {
      try
      {
        FileWriter fileWriter = new FileWriter(fileName, true);
        PrintWriter output = new PrintWriter(fileWriter);

        output.print(""); //overwrite file with a blank string
      }
      catch(IOException ex)
      {
        System.out.println("error");
      }
    }


/** List Operations */


    /** Appends coin object to currList
    *   @param  appendCoin coin object to be appended
    */
    public static void listAppend(coin appendCoin)
    {
      currList.add(appendCoin);
    }


    /** Inserts coin object at correct position in currList
    *   @param  insertCoin coin object to be inserted
    */
    public static void listInsert(coin insertCoin)
    {
      String[] mMarks = {"P","D","S","O", "CC", "W"};  //mint mark list order
      int i = 0;
      int j = 0;
      int k = 0;

      //increment currList untill spot i in currlist has the same date as insertCoin
      while(i< currList.size() && insertCoin.getdate() < currList.get(i).getdate())
        i++;

      //Increment j untill insertCoin has the same mint mark as index j in mMarks
      while(j<6 && !(mMarks[j].equals(insertCoin.getmMark())))
        j++;

      //Don't insert if mint mark is invalid
      if(j == 6 && !(insertCoin.getmMark().equals(mMarks[5])))
        return;

      //Increment i untill insertCoin is at the correct position in currList
      while(i < currList.size() && insertCoin.getdate() == currList.get(i).getdate())
      {
        //Increment k untill current coin in currList has the same mint mark as index k in mMarks
        k = 0;
        while(k<6 && !(mMarks[k].equals(currList.get(i).getmMark())))
          k++;

        //Insert insertCoin if insertCoin has a lower priority mint mark than coin i in currlist
        if(!(j>k))
          break;
        i++;
      }

      currList.add(i, insertCoin);
    }

    /** Removes first instance of coin object from currList
    *   @param  removeCoin coin object to be removed
    */
    public static void listRemove(coin removeCoin)
    {
      currList.remove(removeCoin);
    }
}
