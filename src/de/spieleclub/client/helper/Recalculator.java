package de.spieleclub.client.helper;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.spieleclub.client.Global;
import de.spieleclub.shared.Period;


public class Recalculator {
  private MessageHandler messageHandler;
  private Iterator<Period> it;
  
  public Recalculator(MessageHandler messageHandler) {
    this.messageHandler = messageHandler;
    List<Period> recalc = Global.availablePeriods.getRecalculationRelevantPeriods();
    it = recalc.iterator();
  }

  
  public void clearAndRecalculate() {
    
    final long startTime = System.currentTimeMillis();
    Global.spieleclubService.clearPrecalculatedData(new AsyncCallback<Void>() {
      
      @Override
      public void onSuccess(Void result) {
        Recalculator.this.run();
        messageHandler.handleMessage(buildMessage("clear", startTime));
      }
      
      @Override
      public void onFailure(Throwable caught) {
        Recalculator.this.run();
        messageHandler.handleMessage(Global.texte.errorClearingPrecalculation());
      }
    });
    
  }
  
  public void run() {
    if (it.hasNext()) {
      recalculatePeriod(it.next());
    } else {
      recalculateFirstDates();
    }
  }

  private void recalculatePeriod(final Period period) {
    final long startTime = System.currentTimeMillis();

    Global.spieleclubService.touchRanking(period, new AsyncCallback<Void>() {

      @Override
      public void onFailure(Throwable caught) {
        Recalculator.this.run();
        messageHandler.handleMessage(period.getLabel() + " - fehlgeschlagen");
      }

      @Override
      public void onSuccess(Void result) {
        Recalculator.this.run();
        messageHandler.handleMessage(buildMessage(period.getLabel(), startTime));
      }
    });
  }
  
  private void recalculateFirstDates() {
    final long startTime = System.currentTimeMillis();

    Global.spieleclubService.touchDates(new AsyncCallback<Void>() {

      @Override
      public void onFailure(Throwable caught) {
        messageHandler.handleMessage("Erstes Mal gespielt - fehlgeschlagen");
      }

      @Override
      public void onSuccess(Void result) {
        messageHandler.handleMessage(buildMessage("Erstes Mal gespielt", startTime));
      }
      
    });
  }


  protected String buildMessage(String message, long startTime) {
    long millisecondsSinceStart = System.currentTimeMillis() - startTime;
    return message + " - " + millisecondsSinceStart +"ms";
  }



}
