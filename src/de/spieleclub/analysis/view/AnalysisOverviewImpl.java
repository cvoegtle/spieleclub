package de.spieleclub.analysis.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.spieleclub.client.Global;

public class AnalysisOverviewImpl  extends Composite implements AnalysisOverview {

  @UiTemplate("AnalysisOverview.ui.xml")
  interface AnalysisOverviewUiBinder extends UiBinder<Widget, AnalysisOverviewImpl> {
  }

  private static AnalysisOverviewUiBinder uiBinder = GWT.create(AnalysisOverviewUiBinder.class);

  private Presenter presenter;

  @UiField  Anchor spieleclubAnchor;
  @UiField HorizontalPanel analysisPanel;
  @UiField Button addColumn;
 
  
  public AnalysisOverviewImpl() {
    initWidget(uiBinder.createAndBindUi(this));

    spieleclubAnchor.setHTML(Global.texte.backToSpieleclub());
  }

  @Override
  public void setPresenter(Presenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void addWidget(Widget widget) {
    analysisPanel.insert(widget, analysisPanel.getWidgetCount()-1);
  }

  @Override
  public Widget asWidget() {
    return this;
  }

  
  @UiHandler("addColumn")
  void onAddColumnClicked(ClickEvent event) {
    presenter.onAddColumnClicked();
  }
  

}
