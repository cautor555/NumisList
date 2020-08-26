/**
* Class name: coin
* coin class creates, modifies, maintaines, and returns cariables to store:
* date, mint mark, condition, attributes, and if the coin is in the user's collection.
*
* @author  Christian Autor
* @version 1.1
* @since   7/11/2020
*/
public class coin
{
  private int date;
  private String mMark;
  private String condition;
  private boolean inCollection;
  private String attributes;

  /** Constructor */
  public coin(int d, String m, boolean i)
  {
    date = d;
    mMark = m;
    inCollection = i;
    condition = "null";
    attributes = "null";
  }

  /** Constructor */
  public coin(int d, String m, boolean i, String c, String a)
  {
    date = d;
    mMark = m;
    inCollection = i;
    attributes = a;
    condition = c;
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

  /** @return String containing all coin variables */
  public String LPrintAll()
  {
    String S = (date + " " + mMark);
    if(!(attributes.equals("null")))
      S = S + "  " + attributes;
    if(!(condition.equals("null")))
      S = S + "  " + condition;
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
