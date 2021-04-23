package ru.DmN.AE2AO;

public class Config implements Cloneable {
    public boolean DisableChannels = false;
    public boolean ControllerLimits = false;
    //
    public int Max_X = 7;
    public int Max_Y = 7;
    public int Max_Z = 7;
    // Storage Cell Fire Damage
    public boolean SCFD = false;
    //
    public Config clone() throws CloneNotSupportedException {
        return (Config) super.clone();
    }
}
