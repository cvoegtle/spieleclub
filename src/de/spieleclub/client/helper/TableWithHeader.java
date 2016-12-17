package de.spieleclub.client.helper;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.shared.helper.ColumnAccess;

public class TableWithHeader extends Composite {

  interface Binder extends UiBinder<Widget, TableWithHeader> { }
  private static final Binder binder = GWT.create(Binder.class);
  
  interface EvenStyle extends CssResource {
    String evenRow();
  }

  interface OddStyle extends CssResource {
    String oddRow();
  }

  @UiField FlexTable header;
  @UiField FlexTable table;
  @UiField EvenStyle evenStyle;
  @UiField OddStyle oddStyle;

  private List<ColumnDefinition> columnDefinitions;
  
  public TableWithHeader(List<ColumnDefinition> columnDefinitions) {
    initWidget(binder.createAndBindUi(this));
    
    this.columnDefinitions = columnDefinitions;

    initTable();
  }
  
  public void setValues(List<ColumnAccess> values) {
    for (int i = 0; i < values.size(); ++i) {

      ColumnAccess item = values.get(i);
      for (int col = 0; col < columnDefinitions.size(); col++) {
        table.setWidget(i, col, columnDefinitions.get(col).getWidget(item.getValueFor(col)));
        table.getCellFormatter().setHorizontalAlignment(i, col, columnDefinitions.get(col).getAlignment());    
      }
      
      styleRow(i);
    }
    
    table.setHeight(Integer.toString(table.getRowCount())+"em");
  }

  @Override
  protected void onLoad() {
  }

  /**
   * Initializes the table so that it contains enough rows for a full page of
   * emails. Also creates the images that will be used as 'read' flags.
   */
  private void initTable() {
    // Initialize the header.
    for (int col = 0; col < columnDefinitions.size(); col++) {
      ColumnDefinition columnDefinition = columnDefinitions.get(col);
      header.setText(0, col, columnDefinition.getCaption());
      header.getColumnFormatter().setWidth(col, columnDefinition.getWidthAsString());
      header.getCellFormatter().setHorizontalAlignment(0, col, columnDefinition.getAlignment());
      
      table.getColumnFormatter().setWidth(col, columnDefinition.getWidthAsString());
      table.getCellFormatter().setHorizontalAlignment(0, col, columnDefinition.getAlignment());
    }
  }

  private void styleRow(int row) {
    if (row != -1) {
      if (row % 2 == 0) {
        table.getRowFormatter().addStyleName(row, evenStyle.evenRow());
      } else {
        table.getRowFormatter().addStyleName(row, oddStyle.oddRow());
      }
    }
  }

  public void clear() {
    table.removeAllRows();    
  }
}
