package de.spieleclub.capture.view;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.shared.helper.ColumnAccess;

public interface CaptureSpieleabendView {
  public interface Presenter {
    void onMarkupButtonClicked();
    void onAddButtonClicked();
    void onRemoveButtonClicked();
    void onSaveButtonClicked();
    void onCancelButtonClicked();
    
    void onDateChanged(Date date);
    void onDescriptionChanged(String description);
  }

  void setPresenter(Presenter presenter);  
  void clear();
  void init();
  
  void addSuggestion(String suggestion);
  void addMultipleSuggestions(List<String> suggestions);
  void setColumnValues(List<ColumnAccess> values);
  
  void update();
  void setFocusToAddButton();
  
  HasValue<Date> getDate();
  HasValue<String> getDescription();
  HasValue<String> getSuggestSpiel();
  HasValue<String> getAddInfo();
  HasValue<Integer> getSelectedSpiele();
  
  Widget asWidget();
}
