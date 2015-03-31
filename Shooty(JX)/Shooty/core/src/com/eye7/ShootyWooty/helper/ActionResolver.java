package com.eye7.ShootyWooty.helper;



/**
 * Created by anvithaprashanth on 17/03/15.
 */
public interface ActionResolver {
    public void sendMessageAll(String x,String s);
    public void sendMessageHost(String s);
    public boolean getValid();
    public String getMoves();
    public void setValid(boolean v);

}
