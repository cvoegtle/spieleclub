package de.spieleclub.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class AvailablePeriods {
  private List<Period> periods = new ArrayList<>();
  private Period overallPeriod;
  
  public AvailablePeriods() {
    periods.add(new Period(getCurrentYear()));
  }
  
  public AvailablePeriods(Date startingDate, String nameOfOverallPeriod) {
    Integer startingYear = getStartingYear(startingDate);
    Integer currentYear = getCurrentYear();
    
    periods.add(new Period(currentYear));
    for (int year = currentYear-1; year >= startingYear; year--) {
      periods.add(new Period(year));
    }
    overallPeriod = new Period(nameOfOverallPeriod);
    periods.add(overallPeriod);
  }
  
  public Period getCurrentPeriod() {
    return periods.get(0);
  }

  @SuppressWarnings("deprecation")
  private Integer getStartingYear(Date startingDate) {
    return startingDate.getYear() + 1900;
  }
  
  @SuppressWarnings("deprecation")
  private Integer getCurrentYear() {
    Date currentDate = new Date();
    return currentDate.getYear() + 1900;
  }

  public List<String> getAvailablePeriodsAsStrings() {
    ArrayList<String> availablePeriods = new ArrayList<>();

    for (Period period : periods) {
      availablePeriods.add(period.getLabel());
    }

    return availablePeriods;
  }

  /**
   * if no period fits then return the last one.
   * Should be 'all'.
   * @param selectedPeriod
   * @return
   */
  public Period getPeriodByLabel(String selectedPeriod) {
    Period period = null;
    for (Period period1 : periods) {
      period = period1;
      if (period.getLabel().equals(selectedPeriod)) {
        break;
      }
    }
    return period;
  }

  public List<Period> getRecalculationRelevantPeriods() {
    ArrayList<Period> relevant = new ArrayList<>();
    relevant.add(periods.get(0));
    if (periods.size() > 2) {
      relevant.add(periods.get(1));
    }
    relevant.add(periods.get(periods.size()-1));
    return relevant;
  }

  public Iterator<Period> iterator() {
    return periods.iterator();
  }

  public void removeOtherThanYearlyPeriods() {
    periods.remove(overallPeriod);
    overallPeriod = null;
  }

  public Period getOverallPeriod() {
    return overallPeriod;
  }

  public int  size() {
    return periods.size();
  }
}
