package de.spieleclub.client.helper;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.shared.helper.ColumnAccess;

public class SelectableList extends Composite implements HasValue<Integer> {
  public interface Listener {
    void onItemSelected(ColumnAccess item);
  }

  interface Binder extends UiBinder<Widget, SelectableList> { }
  interface SelectionStyle extends CssResource {
    String selectedRow();
  }

  private static final Binder binder = GWT.create(Binder.class);

  @UiField FlexTable header;
  @UiField FlexTable table;
  @UiField SelectionStyle selectionStyle;

  private Listener listener;
  private int selectedRow = -1;

  private List<ColumnDefinition> columnDefinitions;
  private List<ColumnAccess> values;
  
  public SelectableList(List<ColumnDefinition> columnDefinitions) {
    initWidget(binder.createAndBindUi(this));
    
    this.columnDefinitions = columnDefinitions;

    initTable();
  }
  
  public void setValues(List<ColumnAccess> values) {
    this.values = values;
  }

  /**
   * Sets the listener that will be notified when an item is selected.
   */
  public void setListener(Listener listener) {
    this.listener = listener;
  }

  @Override
  protected void onLoad() {
    // Select the first row if none is selected.
    if (selectedRow == -1) {
      selectRow(0);
    }
  }

  @UiHandler("table")
  void onTableClicked(ClickEvent event) {
    // Select the row that was clicked (-1 to account for header row).
    Cell cell = table.getCellForEvent(event);
    if (cell != null) {
      int row = cell.getRowIndex();
      selectRow(row);
    }
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

  public void selectRow(int row) {
    // When a row (other than the first one, which is used as a header) is
    // selected, display its associated Spiel.
    if (row < 0 || row >= values.size()) {
      return;
    }

    ColumnAccess item = values.get(row);

    styleRow(selectedRow, false);
    styleRow(row, true);

    selectedRow = row;

    if (listener != null) {
      listener.onItemSelected(item);
    }
  }

  private void styleRow(int row, boolean selected) {
    if (row != -1) {
      String style = selectionStyle.selectedRow();

      if (selected) {
        table.getRowFormatter().addStyleName(row, style);
      } else {
        table.getRowFormatter().removeStyleName(row, style);
      }
    }
  }

  public void update() {
    // Update the older/newer buttons & label.
    int max = values.size();

    // Show the Spiele
    
    for (int i = 0; i < max; ++i) {

      ColumnAccess item = values.get(i);

      // Add a new row to the table, then set each of its columns to the
      // email's sender and subject values.
      for (int col = 0; col < columnDefinitions.size(); col++) {
        table.setText(i, col, item.getValueFor(col));
        table.getCellFormatter().setHorizontalAlignment(i, col, columnDefinitions.get(col).getAlignment());        
      }
    }
    
    for (int i = table.getRowCount()-1; i >= max; i--) {
      table.removeRow(i);
    }
    table.setHeight(Integer.toString(table.getRowCount())+"em");
  }
  
  public int getSelectedRow() {
    return selectedRow;
  }

  @Override
  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<Integer> handler) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Integer getValue() {
    return new Integer(getSelectedRow());
  }

  @Override
  public void setValue(Integer value) {
    if (value != null) {
      selectRow(value.intValue());
    }
  }

  @Override
  public void setValue(Integer value, boolean fireEvents) {
    setValue(value);
    if (listener != null) {
      if (selectedRow >= 0 && selectedRow < values.size()) {
        listener.onItemSelected(values.get(selectedRow));
      }
    }    
  }

}
