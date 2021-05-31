package co.com.mutantteam.server;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class AppResourceConfig extends ResourceConfig {

	public AppResourceConfig() {
		packages("co.com.mutantteam.service");
	}

}
