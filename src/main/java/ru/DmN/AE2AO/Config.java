package ru.DmN.AE2AO;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;

@me.sargunvohra.mcmods.autoconfig1u.annotation.Config(name = "ae2ao")
public class Config implements ConfigData, Cloneable {
    public boolean DisableChannels = false;
    public boolean ControllerLimits = false;
    //
    public int Max_X = 7;
    public int Max_Y = 7;
    public int Max_Z = 7;
    //
    // Storage Cell Fire Damage
    public boolean SCFD = false;
    //
    public Config clone() {
        Config nc = new Config();

        nc.DisableChannels = DisableChannels;
        nc.ControllerLimits = ControllerLimits;
        nc.Max_X = Max_X;
        nc.Max_Y = Max_Y;
        nc.Max_Z = Max_Z;
        nc.SCFD = SCFD;

        return nc;
    }
}
