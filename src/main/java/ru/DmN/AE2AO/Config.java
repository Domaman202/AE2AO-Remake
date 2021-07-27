package ru.DmN.AE2AO;

public class Config implements Cloneable {
    public boolean DC = false;
    public boolean CL = false;
    //
    public int MX = 7;
    public int MY = 7;
    public int MZ = 7;
    // Storage Cell Fire Damage
    public boolean SCFD = false;
    //
    public boolean CI = true;
    //
    public Config clone() {
        try {
            return (Config) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
