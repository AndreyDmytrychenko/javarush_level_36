package com.javarush.task37.task3709.connectors;

import com.javarush.task37.task3709.security.SecurityChecker;
import com.javarush.task37.task3709.security.SecurityCheckerImpl;

public class SecurityProxyConnector implements Connector {

    private SecurityChecker securityChecker = new SecurityCheckerImpl();
    private SimpleConnector simpleConnector;



    public SecurityProxyConnector(String resourceString) {
        this.simpleConnector = new SimpleConnector(resourceString);
    }

    @Override
    public void connect() {
        System.out.println("Connection security check");
        if (securityChecker.performSecurityCheck()) {
            simpleConnector.connect();
            System.out.println("Connection established");
        } else {
            System.out.println("Connection isn't safe");
        }
    }
}
