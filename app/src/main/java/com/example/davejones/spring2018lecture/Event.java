package com.example.davejones.spring2018lecture;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event {

  @SerializedName("id")
  @Expose
  private Integer id;
  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("description")
  @Expose
  private String description;

  @SerializedName("attendees")
  @Expose
  private Object attendees;

  @SerializedName("startDate")
  @Expose
  private Date startDate;

  @SerializedName("endDate")
  @Expose
  private Date endDate;
  @SerializedName("created_at")
  @Expose
  private Date createdAt;
  @SerializedName("updated_at")
  @Expose
  private Date updatedAt;

  /**
   * No args constructor for use in serialization
   */
  public Event() {
  }

  /**
   *
   * @param updatedAt
   * @param id
   * @param startDate
   * @param attendees
   * @param createdAt
   * @param description
   * @param name
   * @param endDate
   */
  public Event( Integer id, String name, String description, Object attendees, Date startDate, Date endDate, Date createdAt, Date updatedAt ) {
    super();
    this.id = id;
    this.name = name;
    this.description = description;
    this.attendees = attendees;
    this.startDate = startDate;
    this.endDate = endDate;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public String toString() {
    return name + "(" + id + ") : " + description;
  }

  public Integer getId() {
    return id;
  }

  public void setId( Integer id ) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription( String description ) {
    this.description = description;
  }

  public Object getAttendees() {
    return attendees;
  }

  public void setAttendees( Object attendees ) {
    this.attendees = attendees;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate( Date startDate ) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate( Date endDate ) {
    this.endDate = endDate;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt( Date createdAt ) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt( Date updatedAt ) {
    this.updatedAt = updatedAt;
  }

}