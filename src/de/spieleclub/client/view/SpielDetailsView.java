package de.spieleclub.client.view;

import com.google.gwt.user.client.ui.Widget;

public interface SpielDetailsView {
  public interface Presenter {
    void onCloseButtonClicked();
  }

  public void setPresenter(Presenter presenter);
  
  public void setFirstDateLabel(String dateText);
  public void setTimesPlayed(String timesPlayedText);
  public void setAllTimesRanking(String rank);
  public void setYearlyStatistic(String yearlyStatistic);

  public Widget asWidget();
}
