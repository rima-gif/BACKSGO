package com.rima.ryma_prj.domain.model;

public enum RobotStatus {
    CONNECTED("connecté"),
    DISCONNECTED("Non connecté");

    private final  String displayName;

    RobotStatus (String displayName){
        this.displayName = displayName;

    }
    public String getDisplayName(){
        return displayName;
    }

}
