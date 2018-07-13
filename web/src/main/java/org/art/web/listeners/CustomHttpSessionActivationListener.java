package org.art.web.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

@WebListener
public class CustomHttpSessionActivationListener implements HttpSessionActivationListener {

    private static final Logger LOG = LogManager.getLogger(CustomHttpSessionActivationListener.class);

    public CustomHttpSessionActivationListener() {
        LOG.debug("CustomHttpSessionActivationListener: listener instantiation...");
    }

    @Override
    public void sessionWillPassivate(HttpSessionEvent httpSessionEvent) {
        LOG.debug("CustomHttpSessionActivationListener: session is about to be passivated. Session ID: {}", httpSessionEvent.getSession().getId());

    }

    @Override
    public void sessionDidActivate(HttpSessionEvent httpSessionEvent) {
        LOG.debug("CustomHttpSessionActivationListener: session has been activated. Session ID: {}", httpSessionEvent.getSession().getId());
    }
}
