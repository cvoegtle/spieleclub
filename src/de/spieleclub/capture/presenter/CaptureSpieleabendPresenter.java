package de.spieleclub.capture.presenter;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

import de.spieleclub.capture.view.CaptureSpieleabendView;
import de.spieleclub.client.Global;
import de.spieleclub.client.event.AddSpielEvent;
import de.spieleclub.client.event.AddSpielEventHandler;
import de.spieleclub.client.event.CaptureSpieleabendCancelledEvent;
import de.spieleclub.client.event.CaptureSpieleabendEvent;
import de.spieleclub.client.event.SpieleabendeChangedEvent;
import de.spieleclub.client.helper.MarkupDialog;
import de.spieleclub.client.helper.NonExistingSpielDialog;
import de.spieleclub.client.helper.SpieleabendMarkup;
import de.spieleclub.client.presenter.Presenter;
import de.spieleclub.shared.GespieltesSpiel;
import de.spieleclub.shared.Spiel;
import de.spieleclub.shared.Spieleabend;
import de.spieleclub.shared.helper.ColumnAccess;

public class CaptureSpieleabendPresenter implements Presenter, CaptureSpieleabendView.Presenter {
  private AddSpielEventHandler eventHandler;

  private CaptureSpieleabendView view;
  private Spieleabend spieleabend;
  
  public CaptureSpieleabendPresenter(Spieleabend spieleabend, CaptureSpieleabendView view) {
    this.spieleabend = spieleabend;
    this.view = view;
    this.view.setPresenter(this);    
    
    bind();
  }
  
  private void bind() {
    eventHandler = new AddSpielEventHandler() {      
      @Override
      public void onAddSpiel(AddSpielEvent event) {
        createNewSpiel(event.getSpiel());
        addSpielToSpieleabend(event.getSpiel(), view.getAddInfo().getValue());    
      }
    };
    Global.eventBus.addHandler(AddSpielEvent.TYPE, eventHandler);
  }
  
  private void unbind() {
    Global.eventBus.removeHandler(AddSpielEvent.TYPE, eventHandler);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void go(HasWidgets container) {
    Global.eventBus.fireEvent(new CaptureSpieleabendEvent());
    
    view.clear();
    
    view.getDate().setValue(spieleabend.getDate());
    view.getDescription().setValue(spieleabend.getDescription());
    view.setColumnValues((List<ColumnAccess>)(spieleabend.getGespielteSpiele()));
    
    view.addMultipleSuggestions(Global.knownSpiele.getSuggestions());
    
    container.clear();
    container.add(view.asWidget());
    
    view.init();
  }

  @Override
  public void onAddButtonClicked() {
    addSpielToSpieleabend(view.getSuggestSpiel().getValue(), view.getAddInfo().getValue());
  }

  protected void addSpielToSpieleabend(String spieleName, String addInfo) {
    if (Global.knownSpiele.isNewSpiel(spieleName)) {
      NonExistingSpielDialog confirmationDialog = new NonExistingSpielDialog(spieleName);
      confirmationDialog.center();
    } else {
      Spiel selectedSpiel = Global.knownSpiele.getSpielForName(spieleName);
      if (selectedSpiel != null) {
        spieleabend.add(selectedSpiel, addInfo);
        view.update();
        view.setFocusToAddButton();
      }
    }
  }
  
  @Override
  public void onRemoveButtonClicked() {
    int index = view.getSelectedSpiele().getValue();
    int newIndex = index;
    if (index >= 0) {
      GespieltesSpiel spiel = (GespieltesSpiel)spieleabend.getGespielteSpiele().get(index);
      if (spiel.getCount() > 1) {
        spiel.decrementCount();
      } else {
        spieleabend.getGespielteSpiele().remove(index);
        if (spieleabend.getGespielteSpiele().size() <= index) {
          newIndex = index-1;
        }
      }
    }
    view.getSelectedSpiele().setValue(newIndex);
    view.update();
  }

  @Override
  public void onSaveButtonClicked() {
    unbind();
    Global.spieleclubService.saveSpieleabend(spieleabend, new AsyncCallback<Boolean>() {

      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorSavingSpieleabend());        
      }

      @Override
      public void onSuccess(Boolean result) {
        Global.eventBus.fireEvent(new SpieleabendeChangedEvent());
      }
    });    
  }
  
  public void onMarkupButtonClicked() {
    MarkupDialog markupDialog = new MarkupDialog(new SpieleabendMarkup(spieleabend).getMarkup());
    markupDialog.center();
  }

  @Override
  public void onCancelButtonClicked() {
    unbind();
    Global.eventBus.fireEvent(new CaptureSpieleabendCancelledEvent());
  }

  @Override
  public void onDateChanged(Date date) {
    spieleabend.setDate(date);  
  }

  @Override
  public void onDescriptionChanged(String description) {
    spieleabend.setDescription(description);    
  }

  public void createNewSpiel(String name) {
    Spiel spiel = new Spiel();
    spiel.setName(name);
    spiel.setCreation(new Date());
    spiel.setCreator(Global.login.getSpieler());
    
    saveSpiel(spiel);
  }

  protected void saveSpiel(Spiel spiel) {
    // already add the spiel to the list here, because it might take a while
    // until the callback returns the Spiel including its unique datastore key
    Global.knownSpiele.addSpiel(spiel);
    
    Global.spieleclubService.saveSpiel(spiel, new AsyncCallback<Spiel>() {
      @Override
      public void onFailure(Throwable caught) {
        // remove the item that we just added before
        Global.knownSpiele.removeLastSpiel();
      }

      @Override
      public void onSuccess(Spiel result) {
        // replace the spiel with the version from the server
        // including its unique datastore key.
        Global.knownSpiele.getLastSpiel().setWebsafeKey(result.getWebsafeKey());
        view.addSuggestion(result.getName());
      }
      
    });
  }

}
