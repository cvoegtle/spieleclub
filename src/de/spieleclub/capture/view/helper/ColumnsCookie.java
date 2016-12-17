package de.spieleclub.capture.view.helper;

import com.google.gwt.user.client.Cookies;

public class ColumnsCookie {
  private static int MAX_COLUMNS = 8;
  private static String COLUMN_COOKIE = "number_of_columns";
  private static int DEFAULT_COLUMNS = 3;
  
  public static int readNumberOfColumns() {
    int columns = DEFAULT_COLUMNS;
    String cookieColumns = Cookies.getCookie(COLUMN_COOKIE);
    if (cookieColumns != null && cookieColumns.length() > 0) {
      try {
        columns = Integer.parseInt(cookieColumns);
      } catch (Exception ex) {}
      
      if (columns < 1) {
        columns = 1;
      } else if (columns > MAX_COLUMNS) {
        columns = MAX_COLUMNS;
      }      
    }
    
    return columns;
  }
  
  
  public static int getMaxColumns() {
    return MAX_COLUMNS;
  }
  
  
  public static void setColumnsCookie(Integer columns) {
    Cookies.setCookie(COLUMN_COOKIE, columns.toString());
  }


}
