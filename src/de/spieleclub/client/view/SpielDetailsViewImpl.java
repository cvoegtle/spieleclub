package de.spieleclub.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.client.Global;

public class SpielDetailsViewImpl extends Composite implements SpielDetailsView {
  @UiTemplate("SpielDetailsView.ui.xml")
  interface SpielDetailsViewUiBinder extends UiBinder<Widget, SpielDetailsViewImpl> {}
  private static SpielDetailsViewUiBinder uiBinder = GWT.create(SpielDetailsViewUiBinder.class);
  
  @UiField HTML allTimesRanking;
  @UiField HTML firstDate;
  @UiField HTML timesPlayed;
  
  @UiField HTML yearList;
  
  @UiField Button closeButton;
  
  private Presenter presenter;

  public SpielDetailsViewImpl() {
    initWidget(uiBinder.createAndBindUi(this));
    closeButton.setHTML(Global.texte.close());
  }
  
  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void setAllTimesRanking(String rank) {
    allTimesRanking.setHTML(rank);
  }

  @Override
  public void setFirstDateLabel(String dateText) {
    firstDate.setHTML(dateText);
  }

  @Override
  public void setTimesPlayed(String timesPlayedText) {
    timesPlayed.setHTML(timesPlayedText);
  }
  
  @Override
  public void setYearlyStatistic(String yearlyStatistic) {
    yearList.setHTML(yearlyStatistic);
  }
  
  @UiHandler("closeButton")
  void onCloseButtonClicked(ClickEvent event) {
    closeButton.setEnabled(false);
    if (presenter != null) {
      presenter.onCloseButtonClicked();
    }
  }

}
