package org.handler;

public class HandleMatch implements Runnable {
    String json=null;
    @Override
    public void run() {

        while (true){
            if ((UserMatchMap.userMap.size())==2){
                json="{\"status\":\"1\"}";
                break;
            }
        }
    }
}
