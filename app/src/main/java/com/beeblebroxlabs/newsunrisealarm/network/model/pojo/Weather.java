package com.beeblebroxlabs.newsunrisealarm.network.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by devgr on 11-Sep-17.
 */

public class Weather {
  @SerializedName("main")
  @Expose
  public String main;
  @SerializedName("description")
  @Expose
  public String description;

  public String getMain() {
    return main;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return main +" "+ description;
  }
}
