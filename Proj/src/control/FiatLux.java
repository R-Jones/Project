package control;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import control.Command;

/**
 * Application Lifecycle Listener implementation class Initialize
 *
 */
@WebListener
public class FiatLux implements ServletContextListener {
    /**
     * Default constructor. 
     */
    public FiatLux() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 

    	System.out.println("It's working");
    	
    	Command.initialize();
    	
    }
	
}
