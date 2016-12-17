package de.spieleclub.client.helper;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;

import de.spieleclub.client.Global;
import de.spieleclub.client.event.CloseSpieleDetailsEvent;
import de.spieleclub.client.event.CloseSpieleDetailsEventHandler;
import de.spieleclub.client.presenter.SpielDetailsPresenter;
import de.spieleclub.client.view.SpielDetailsViewImpl;
import de.spieleclub.shared.SpieleProfile;

public class SpieleDetailsDialog extends DialogBox {
  
  public SpieleDetailsDialog(final String nameOfTheClickedSpiel) {
    this.setText(nameOfTheClickedSpiel);
    this.setModal(false);
    
    terminateOtherSpieleDetailsDialogs();
   
    SpielDetailsPresenter presenter = new SpielDetailsPresenter(new SpielDetailsViewImpl(), nameOfTheClickedSpiel);
    presenter.go(this);
    bind();
    
    loadSpieleDetails(nameOfTheClickedSpiel);

  }

  private void loadSpieleDetails(final String nameOfTheClickedSpiel) {
    Global.spieleclubService.loadSpieleProfile(nameOfTheClickedSpiel, new AsyncCallback<SpieleProfile>() {
      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorLoadingSpieleDetails() + " " + nameOfTheClickedSpiel + ".");
        hide();
      }

      @Override
      public void onSuccess(SpieleProfile result) {
        SpielDetailsPresenter presenter = new SpielDetailsPresenter(new SpielDetailsViewImpl(), result);
        presenter.go(SpieleDetailsDialog.this);
      }
    });
  }

  private void terminateOtherSpieleDetailsDialogs() {
    Global.eventBus.fireEvent(new CloseSpieleDetailsEvent());
  }

  private void bind() {
    Global.eventBus.addHandler(CloseSpieleDetailsEvent.TYPE, new CloseSpieleDetailsEventHandler() {
      @Override
      public void onCloseSpieleDetails(CloseSpieleDetailsEvent event) {
        hide();        
      }
    });
  }
}
