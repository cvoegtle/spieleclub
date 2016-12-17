package de.spieleclub.analysis;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

import de.spieleclub.client.Global;
import de.spieleclub.client.SpieleclubService;
import de.spieleclub.client.i18n.Texte;

public class SpieleclubAnalysis implements EntryPoint {
  AnalysisController analysisController;

  @Override
  public void onModuleLoad() {
    Global.spieleclubService = GWT.create(SpieleclubService.class);
    Global.texte = (Texte)GWT.create(Texte.class);
       
    analysisController = new AnalysisController();
    analysisController.go(RootPanel.get());
  }

}
