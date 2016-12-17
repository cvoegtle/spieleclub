package de.spieleclub.shared;

import java.io.Serializable;
import java.util.Date;

public class Period implements Serializable{
  private static final long serialVersionUID = 1L;
  
  private String label;
  private Date startDate = null;
  private Date endDate = null;
  
  private boolean userCreated = false;
  
  @SuppressWarnings("deprecation")
  protected Period(Integer year) {
    label = year.toString();
    startDate = new Date(year-1900, 0, 1);
    endDate = new Date(year-1900, 11, 31);
    eraseTime();
  }
  
  
  public Period(Date startDate, Date endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
    userCreated = true;
  }
  
  
  protected Period(String label) {
    this.label = label;
  }
  
  protected Period() {    
  }

  public String getLabel() {
    return label;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  @SuppressWarnings("deprecation")
  public int getYear() {
    return startDate.getYear()+1900;
  }


  public boolean isUserCreated() {
    return userCreated;
  }

  @SuppressWarnings("deprecation")
  private void eraseTime() {
    startDate.setHours(0);
    startDate.setMinutes(0);
    startDate.setSeconds(0);

    endDate.setHours(0);
    endDate.setMinutes(0);
    endDate.setSeconds(0);
  }

  
}
