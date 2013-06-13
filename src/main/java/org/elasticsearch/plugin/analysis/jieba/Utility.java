package org.elasticsearch.plugin.analysis.jieba;

public class Utility {

  public static final char[] STRING_CHAR_ARRAY = new String("未##串")
      .toCharArray();

  public static final char[] NUMBER_CHAR_ARRAY = new String("未##数")
      .toCharArray();

  public static final char[] START_CHAR_ARRAY = new String("始##始")
      .toCharArray();

  public static final char[] END_CHAR_ARRAY = new String("末##末").toCharArray();

  /**
   * Delimiters will be filtered to this character by {@link SegTokenFilter}
   */
  public static final char[] COMMON_DELIMITER = new char[] { ',' };

  /**
   * Space-like characters that need to be skipped: such as space, tab, newline, carriage return.
   */
  public static final String SPACES = " 　\t\r\n";
  
  
  /**
   * Maximum bigram frequency (used in the smoothing function). 
   */
  public static final int MAX_FREQUENCE = 2079997 + 80000;

  /**
   * compare two arrays starting at the specified offsets.
   * 
   * @param larray left array
   * @param lstartIndex start offset into larray
   * @param rarray right array
   * @param rstartIndex start offset into rarray
   * @return 0 if the arrays are equal，1 if larray > rarray, -1 if larray < rarray
   */
  public static int compareArray(char[] larray, int lstartIndex, char[] rarray,
      int rstartIndex) {

    if (larray == null) {
      if (rarray == null || rstartIndex >= rarray.length)
        return 0;
      else
        return -1;
    } else {
      // larray != null
      if (rarray == null) {
        if (lstartIndex >= larray.length)
          return 0;
        else
          return 1;
      }
    }

    int li = lstartIndex, ri = rstartIndex;
    while (li < larray.length && ri < rarray.length && larray[li] == rarray[ri]) {
      li++;
      ri++;
    }
    if (li == larray.length) {
      if (ri == rarray.length) {
        // Both arrays are equivalent, return 0.
        return 0;
      } else {
        // larray < rarray because larray has ended first.
        return -1;
      }
    } else {
      // differing lengths
      if (ri == rarray.length) {
        // larray > rarray because rarray has ended first.
        return 1;
      } else {
        // determine by comparison
        if (larray[li] > rarray[ri])
          return 1;
        else
          return -1;
      }
    }
  }

  /**
   * Compare two arrays, starting at the specified offsets, but treating shortArray as a prefix to longArray.
   * As long as shortArray is a prefix of longArray, return 0.
   * Otherwise, behave as {@link Utility#compareArray(char[], int, char[], int)}
   * 
   * @param shortArray prefix array
   * @param shortIndex offset into shortArray
   * @param longArray long array (word)
   * @param longIndex offset into longArray
   * @return 0 if shortArray is a prefix of longArray, otherwise act as {@link Utility#compareArray(char[], int, char[], int)}
   */
  public static int compareArrayByPrefix(char[] shortArray, int shortIndex,
      char[] longArray, int longIndex) {

    // a null prefix is a prefix of longArray
    if (shortArray == null)
      return 0;
    else if (longArray == null)
      return (shortIndex < shortArray.length) ? 1 : 0;

    int si = shortIndex, li = longIndex;
    while (si < shortArray.length && li < longArray.length
        && shortArray[si] == longArray[li]) {
      si++;
      li++;
    }
    if (si == shortArray.length) {
      // shortArray is a prefix of longArray
      return 0;
    } else {
      // shortArray > longArray because longArray ended first.
      if (li == longArray.length)
        return 1;
      else
        // determine by comparison
        return (shortArray[si] > longArray[li]) ? 1 : -1;
    }
  }

}