package com.fixme;

import java.util.HashMap;
import java.util.concurrent.*;

public class RouterServer{


    public void runServers() {
        ConnectorsLists connectorsLists = new ConnectorsLists();
        ExecutorService serve = Executors.newCachedThreadPool();
        serve.submit(new Servers(5000,connectorsLists));
        serve.submit(new Servers(5001,connectorsLists));
        serve.shutdown();
    }
}

/**
 * RouterServer
 */
