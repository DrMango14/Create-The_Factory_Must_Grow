package com.drmangotea.tfmg.config;


import net.createmod.catnip.config.ConfigBase;

public class DepositConfig extends ConfigBase {


    public final ConfigInt depositMaxReserves = i(10000, 1000, "depositMaxReserves", Comments.depositMaxReserves);
    public final ConfigBool infiniteDeposits = b(false, "infiniteDeposits", Comments.infiniteDeposits);
    @Override
    public String getName() {
        return "deposits";
    }
    private static class Comments {
        static String depositMaxReserves = "Sets the maximum oil reserves a deposit can have.";
        static String infiniteDeposits = "Makes deposits bottomless.";
    }
}
