package com.betanet.capservice.web;

import java.io.IOException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author Alexander Shkirkov
 */
public class ServerMain {
    private static final String XML_CONFIG_FILES_LOCATION = "classpath:/spring/servlet-context.xml";
    private static final String CONTEXT_PATH = "/";
    private static final String MAPPING_URL = "/*";
    private static final int DEFAULT_SERVER_PORT = 12112;
    
    public static void main(String[] args) throws Exception {
        new ServerMain().startServer();
    }
    
    private void startServer() throws Exception {
        Server server = new Server(DEFAULT_SERVER_PORT);
        System.out.println("CAPTCHA service server started at port " + DEFAULT_SERVER_PORT);
        server.setHandler(getServletContextHandler(getContext()));
        server.start();
        server.join();
    }
    
    private Handler getServletContextHandler(WebApplicationContext applicationContext) throws IOException {
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath(CONTEXT_PATH);
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        servlet.setThrowExceptionIfNoHandlerFound(true);
        contextHandler.addServlet(new ServletHolder(servlet), MAPPING_URL);
        contextHandler.addEventListener(new ContextLoaderListener(applicationContext));
        return contextHandler;
    }

    private WebApplicationContext getContext() {
        XmlWebApplicationContext context = new XmlWebApplicationContext();
        context.setConfigLocation(XML_CONFIG_FILES_LOCATION);
        return context;
    }
}
