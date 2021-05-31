package co.com.mutantteam.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import co.com.mutantteam.controller.DataBaseController;
import co.com.mutantteam.controller.IDataBaseController;
import co.com.mutantteam.controller.IMutantController;
import co.com.mutantteam.controller.MutantController;

public class RestServer {

	public static void main(String[] args) {
		final ResourceConfig application = new ResourceConfig().packages("co.com.mutantteam.service").register(
				new AbstractBinder() {			
					@Override
					protected void configure() {
						try {
							bind(new MutantController()).to(IMutantController.class);
							bind(new MutantController()).to(IMutantController.class);
							bind(new DataBaseController()).to(IDataBaseController.class);
						} catch (Exception e) {
						}			
					}
				});

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server jettyServer = new Server(9093);
		jettyServer.setHandler(context);
		ServletHolder jerseyServlet = new ServletHolder(new org.glassfish.jersey.servlet.ServletContainer(application));
		jerseyServlet.setInitOrder(0);
		context.addServlet(jerseyServlet, "/*");
		 
		
		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			System.out.println("Could not start server");
			e.printStackTrace();
		} finally {
			jettyServer.destroy();
		}
	}

}
