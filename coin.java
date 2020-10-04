import java.io.*;
import java.util.*;
/**
* Class name: coin
* coin class creates, modifies, maintaines, and returns variables to store:
* date, mint mark, condition, attributes, and if the coin is in the user's collection.
*
* @author  Christian Autor
* @version 2.1
* @since   9/7/2020
*/
public class coin implements Serializable
{
  private int date;
  private String mMark;
  private String condition;
  private boolean inCollection;
  private String attributes;

  public LinkedList<String> images = new LinkedList<String>();


  /** Constructor */
  public coin(int date, String mMark, boolean inCollection)
  {
    this.date = date;
    this.mMark = mMark;
    this.inCollection = inCollection;
    condition = "null";
    attributes = "null";
  }

  /** Constructor */
  public coin(int date, String mMark, boolean inCollection, String condition, String attributes)
  {
    this.date = date;
    this.mMark = mMark;
    this.inCollection = inCollection;
    this.condition = condition;
    this.attributes = attributes;
  }

  /** Constructor */
  public coin(int date, String mMark, boolean inCollection, String condition, String attributes, String[] imageArr)
  {
    this.date = date;
    this.mMark = mMark;
    this.inCollection = inCollection;
    this.condition = condition;
    this.attributes = attributes;

    for(int i = 0; i<imageArr.length; i++)
      images.add(imageArr[i]);
  }


  /** @return Date */
  public int getdate()
  { return date; }

  /** @return Mint mark */
  public String getmMark()
  { return mMark; }

  /** @return Boolean for in collection */
  public boolean getinCollection()
  { return inCollection; }
  /** Set boolean for in collection */
  public void editgetinCollection(boolean i)
  { inCollection = i; }

  /** @return Coin condition */
  public String getcondition()
  { return condition; }
  /** Set coin condition */
  public void editcondition(String c)
  { condition = c; }

  /** @return Coin attributes */
  public String getattributes()
  { return attributes; }
  /** Set coin attributes */
  public void editattributes(String a)
  { attributes = a; }

  /** @return a valid integer to name an image */
  public int nameImage()
  {
    boolean flag = false;
    if(images.size() == 0)
      return 1;

    for(int i = 1; i<100; i++)
    {
      flag = false;
      for(int k = 0; k<images.size(); k++)
      {
        if(images.get(k).indexOf(("(" + i + ")")) != -1)
          flag = true;
      }
      if(!flag)
        return i;
    }
    return -1;
  }


  /** @return String containing all coin variables */
  public String LPrintAll()
  {
    String S = (date + " " + mMark);
    if(!(attributes.equals("null")))
      S += "  " + attributes;
    if(!(condition.equals("null")))
      S += "  " + condition;
    return S;
  }

  /** @return String containing all coin variables except condition*/
  public String LPrint()
  {
    String S;
    if(attributes.equals("null"))
      S = (date + " " + mMark);
    else
      S = (date + " " + mMark + " " + attributes);
    return S;
  }
}
