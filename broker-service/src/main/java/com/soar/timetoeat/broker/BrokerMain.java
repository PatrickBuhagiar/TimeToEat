package com.soar.timetoeat.broker;

import org.apache.activemq.broker.BrokerService;

public class BrokerMain {

    public static void main(String[] args) throws Exception {
        BrokerService broker = new BrokerService();
        broker.setUseJmx(true);
        broker.addConnector("tcp://localhost:3000");
        broker.start();
        System.err.println("Broker server running");
    }
}
