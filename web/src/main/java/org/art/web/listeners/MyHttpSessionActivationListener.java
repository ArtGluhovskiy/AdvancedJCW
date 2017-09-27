package org.art.web.listeners;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

public class MyHttpSessionActivationListener implements HttpSessionActivationListener {

    public MyHttpSessionActivationListener() {
        System.out.println("<< MyHttpSessionActivationListener - NEW");
    }

    @Override
    public void sessionWillPassivate(HttpSessionEvent httpSessionEvent) {
        System.out.println("<< MyHttpSessionActivationListener will passivate");

    }

    @Override
    public void sessionDidActivate(HttpSessionEvent httpSessionEvent) {
        System.out.println("<< MyHttpSessionActivationListener did activate");
    }
}
