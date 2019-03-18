package org;

import org.server.TankServer;

public class StartWork {
    public static void main(String[] args) {
        TankServer tankServer=new TankServer();
        tankServer.init();
        tankServer.run();
    }
}
