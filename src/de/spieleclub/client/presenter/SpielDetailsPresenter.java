package de.spieleclub.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.client.Global;
import de.spieleclub.client.event.CloseSpieleDetailsEvent;
import de.spieleclub.client.helper.SpieleProfileDecorator;
import de.spieleclub.client.view.SpielDetailsView;
import de.spieleclub.client.view.SpielDetailsViewImpl;
import de.spieleclub.shared.SpieleProfile;

public class SpielDetailsPresenter implements Presenter, SpielDetailsView.Presenter {
  private SpielDetailsView view;
  private SpieleProfile profile;
  
  public SpielDetailsPresenter(SpielDetailsViewImpl view, SpieleProfile profile) {
    view.setPresenter(this);
    this.view = view;
    this.profile = profile;
  }

  public SpielDetailsPresenter(SpielDetailsViewImpl view,  String spieleName) {
    view.setPresenter(this);
    this.view = view;
    this.profile = new SpieleProfile(spieleName);
  }

  @Override
  public void onCloseButtonClicked() {
    Global.eventBus.fireEvent(new CloseSpieleDetailsEvent());
  }

  @Override
  public void go(HasWidgets container) {
    
    if (profile.isEmpty()) {
      view.setYearlyStatistic(Global.texte.detailsAreTransfered());       
    } else {
      SpieleProfileDecorator decorator = new SpieleProfileDecorator(profile);
      view.setFirstDateLabel(decorator.getFirstTimePlayed());
      view.setTimesPlayed(decorator.getTimesPlayed());
      view.setAllTimesRanking(decorator.getRank());
      view.setYearlyStatistic(decorator.getYearlyStatistic());
    }
    
    container.clear();
    container.add(view.asWidget());
  }

}
