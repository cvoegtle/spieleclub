package de.spieleclub.capture.view.helper;

import java.util.Iterator;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

public class SpieleabendeTableBuilder {
  private FlexTable table;

  public SpieleabendeTableBuilder() {
    table = new FlexTable();
    configureTable();
  }
  
  public SpieleabendeTableBuilder(FlexTable table) {
    this.table = table;
    configureTable();
  }
  
  private void configureTable()  {
    table.addStyleName("style.spieleabendeFlexTable");
    table.setBorderWidth(1);
  }
  
  public void addWidgetsToTable(Iterator<Widget> it) {
    int numberOfColumns = ColumnsCookie.readNumberOfColumns();
    int column = 0;
    int row = 0;
    while (it.hasNext()) {
      table.setWidget(row, column, it.next());
      column = (column+1) % numberOfColumns;
      row += (column == 0 ? 1 : 0); // increment row if the column equals 0      
    }
  }
  
  public FlexTable getTable() {
    return table;
  }
}
