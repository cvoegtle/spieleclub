package de.spieleclub.capture.presenter;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.capture.view.CaptureSpieleabendViewImpl;
import de.spieleclub.capture.view.SpieleabendView;
import de.spieleclub.client.Global;
import de.spieleclub.client.event.CaptureSpieleabendEvent;
import de.spieleclub.client.event.CaptureSpieleabendEventHandler;
import de.spieleclub.client.presenter.Presenter;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Spieleabend;

public class SpieleabendPresenter implements
    Presenter, SpieleabendView.Presenter {

  private SpieleabendView view;
  private Spieleabend spieleabend;
  
  public SpieleabendPresenter(Spieleabend spieleabend, SpieleabendView view) {
    this.spieleabend = spieleabend;
    this.view = view;
    this.view.setPresenter(this);
    
    bind();
  }
  
  private void bind() {
    Global.eventBus.addHandler(CaptureSpieleabendEvent.TYPE, new CaptureSpieleabendEventHandler() {
      @Override
      public void onCaptureSpieleabend(CaptureSpieleabendEvent event) {
        view.setEnabled(false);
      }
    });
  }



  /**
   * @param container must be <b>null</b>
   */
  @Override  
  public void go(HasWidgets container) {
    setSpieleabend();
    view.setEditable(Global.login.isAdmin());
  }

  @Override
  public void onEditButtonClicked() {
    CaptureSpieleabendViewImpl captureSpieleabendView = new CaptureSpieleabendViewImpl();
    new CaptureSpieleabendPresenter(spieleabend.clone(), captureSpieleabendView).go(view.getEditContainer());
  }

  private void setSpieleabend() {
    view.setLabelText(DateTimeFormat.getFormat("EEEE, dd.MM.yyyy").format(spieleabend.getDate()));
    view.setDescription(spieleabend.getDescription());
    ArrayList<String> spieleList = new ArrayList<String>();
    @SuppressWarnings("unchecked")
    Iterator<GespieltesSpiel> it = spieleabend.getGespielteSpiele().iterator();
    while (it.hasNext()) {
      spieleList.add(it.next().getNameWithCount());
    }
    view.setListData(spieleList);
  }
}
