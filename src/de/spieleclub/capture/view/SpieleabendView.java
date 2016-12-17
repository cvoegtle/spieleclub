package de.spieleclub.capture.view;

import java.util.List;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public interface SpieleabendView {
  public interface Presenter {
    void onEditButtonClicked();
  }

  void setPresenter(Presenter presenter);
  void setLabelText(String text);
  void setDescription(String text);
  void setListData(List<String> data);
  
  void setEnabled(boolean enabled);
  void setEditable(boolean editable);
  
  HasWidgets getEditContainer(); 
  
  Widget asWidget();  
}
