import java.io.*;
import java.util.*;
/**
* Class name: appActions
* appActions is made up of file and list operation methods utilized by the GUI class.
*
* @author  Christian Autor
* @version 1.0
* @since   5/28/2020
*/
public class appActions
{
    //Working LList for handeling current list of coins
    public static LinkedList<coin> currList = new LinkedList<coin>();


/** File Operations */


    /** Read and store all coin objects from a file in currList
    *   @param  fileName file to be read
    */
    public static void fileReadAll(String fileName)
    {
      try
      {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine())
          fileRead(sc.nextLine());
      }
      catch(IOException ex)
      {
        System.out.println("error");
      }
    }

    /** Read and store current line in file in currList
    *   @param  fileName file to be read
    */
    public static void fileRead(String fileName)
    {

      StringTokenizer st1 = new StringTokenizer(fileName, "%");    //Tokenizer to decode serialized data from file

      int date = Integer.parseInt(st1.nextToken());
      String mMark = st1.nextToken();
      String condition = st1.nextToken();
      boolean inCollection = false;
      if(st1.nextToken().equals("true"))
        inCollection = true;
      String attributes = st1.nextToken();

      listAppend(new coin(date, mMark, inCollection, condition, attributes));   //Add new coin object from file to currlist
    }

    /** Write all coin objects in currList to file
    *   @param  fileName file to be written to
    */
    public static void fileWriteAll(String fileName)
    {
      try
      {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter output = new PrintWriter(fileWriter);

        //Encode coin object variables separated by percent signs and write to file
        for(int i = 0; i<currList.size(); i++)
        {
          output.print(currList.get(i).getdate() + "%" + currList.get(i).getmMark() + "%" + currList.get(i).getcondition() + "%" + currList.get(i).getinCollection() + "%" + currList.get(i).getattributes() + "%\n");
        }
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
    */
    public static void fileExport(String fileName, String Denomination)
    {
      try
      {
        FileWriter fileWriter = new FileWriter(fileName);
        PrintWriter output = new PrintWriter(fileWriter);

        output.print(Denomination + "\n\n");

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


/** List Operations */


    /** Appends coin object to currList
    *   @param  addCoin coin object to be appended
    */
    public static void listAppend(coin addCoin)
    {
      currList.add(addCoin);
    }


    /** Inserts coin object at correct position in currList
    *   @param  insertCoin coin object to be inserted
    */
    public static void listInsert(coin insertCoin)
    {
      //Mint mark hierarchy
      String[] mintMarks = {"P","D","S","O", "CC", "W"};
      int i = 0;
      int j = 0;
      int k = 0;

      //increment currList untill spot i in currlist has the same date as insertCoin
      while(i< currList.size() && insertCoin.getdate() < currList.get(i).getdate())
        i++;

      //Increment j untill insertCoin has the same mint mark as index j in mintMarks
      while(j<6 && !(mintMarks[j].equals(insertCoin.getmMark())))
        j++;

      //Don't insert if mint mark is invalid
      if(j == 6 && !(insertCoin.getmMark().equals(mintMarks[5])))
        return;

      //Increment i untill insertCoin is at the correct position in currList
      while(i < currList.size() && insertCoin.getdate() == currList.get(i).getdate())
      {
        //Increment k untill current coin in currList has the same mint mark as index k in mintMarks
        k = 0;
        while(k<6 && !(mintMarks[k].equals(currList.get(i).getmMark())))
          k++;

        //Insert insertCoin if insertCoin has a lower order mint mark than coin i in currlist
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
