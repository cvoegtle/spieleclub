package de.spieleclub.capture;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import de.spieleclub.client.Global;
import de.spieleclub.client.SpieleclubService;
import de.spieleclub.client.i18n.Texte;
import de.spieleclub.shared.LoginInfo;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Spieleclub implements EntryPoint {

  AppController appViewer = null;

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    Global.spieleclubService = GWT.create(SpieleclubService.class);
    Global.texte = (Texte)GWT.create(Texte.class);
    final HandlerManager eventBus = new HandlerManager(null);
    
       
    Global.spieleclubService.loginSpieler(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
      
      @Override
      public void onSuccess(LoginInfo result) {
        Global.login = result;
        appViewer = new AppController(eventBus);
        appViewer.go(RootPanel.get("captureArea"));
      }
      
      @Override
      public void onFailure(Throwable caught) {
        Window.alert(Global.texte.errorLoggingIn());
      }
    });
  }
}
