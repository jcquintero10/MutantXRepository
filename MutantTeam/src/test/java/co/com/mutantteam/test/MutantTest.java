package co.com.mutantteam.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.com.mutantteam.controller.MutantController;
import co.com.mutantteam.databuilder.MutantDtoDataBuilder;
import co.com.mutantteam.model.MutantDto;
import co.com.mutantteam.model.MutantStat;
import co.com.mutantteam.model.ResponseService;
import co.com.mutantteam.utility.Constant;
import co.com.mutantteam.utility.MutantException;

/**
 * 
 * @author juquintero
 *
 */

public class MutantTest extends JerseyTest {

	@Mock
	private static MutantController mutantController;
	private MutantDtoDataBuilder mutantDtoDataBuilder;
	private static Gson gson;

	public MutantTest() throws Exception {
		mutantDtoDataBuilder = new MutantDtoDataBuilder();
		mutantController = new MutantController();
		gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Path("/mutantservice")
	public static class MutantServiceResource {

		@POST
		@Path("/mutant")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Response isMutant(MutantDto mutantDto) throws Exception {
			Object message = null;
			Status httpStatus = null;
			try {
				if (mutantDto != null && mutantDto.getDna() != null && mutantDto.getDna().length > 0) {
					MutantStat mutante = mutantController.isMutant(mutantDto.getDna());
					if (mutante != null && mutante.getId() > 0 && mutante.isMutant()) {
						httpStatus = Response.Status.OK;
						message = Constant.MESSAGE_IS_MUTANT + " " + " ID: " + " " + mutante.getId();
					} else {
						httpStatus = Response.Status.FORBIDDEN;
						message = Constant.MESSAGE_NOT_MUTANT;
					}
				} else {
					httpStatus = Response.Status.BAD_REQUEST;
					message = Constant.MESSAGE_MANDATORY_SEQUENCE;
				}

			} catch (Exception e) {
				httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
				message = e.getMessage();
			}

			ResponseService response = new ResponseService();
			response.setMessage((String) message);

			String result = gson.toJson(response);

			return Response.status(httpStatus).entity(result).build();
		}
		
		
		@GET
		@Path("/stats/{id}")
		@Produces(MediaType.APPLICATION_JSON)
		public Response statsMutant(@PathParam("id") Integer id) throws Exception {
			Object message = null;
			Status httpStatus = null;
			try {
				if(id  < 0){
			        return Response.noContent().build();
			    }
				MutantStat stats = mutantController.getStatsMutantDNA(id);
				if(stats != null) {
					httpStatus = Response.Status.OK;
					message = stats.toString();
				}else {
					httpStatus = Response.Status.NO_CONTENT;
					message = Constant.MESSAGE_NOT_STATS;
				}
					
			} catch (Exception e) {
				httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
				message = e.getMessage();
			}
			
			
			ResponseService response = new ResponseService();
			response.setMessage((String)message);
			
			String result = gson.toJson(response);
		    return Response.status(httpStatus).entity(result).build();
		}
		
	}

	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(MutantServiceResource.class);
	}

	@Test
	public void isAMutantTest() {
		try {
			String[] dna = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
			mutantDtoDataBuilder.addDna(dna);
			MutantDto mutantDto = mutantDtoDataBuilder.build();
			final Response response = target("mutantservice/mutant").request().post(Entity.json(mutantDto),
					Response.class);
			String responseAsString = response.readEntity(String.class);
			assertEquals(200, response.getStatus());
			ResponseService responseService = gson.fromJson(responseAsString, ResponseService.class);
			assertEquals("La sequencia de ADN pertenece a un mutante  ID:  1", responseService.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void badRequest() {
		try {
			String[] dna = {};
			mutantDtoDataBuilder.addDna(dna);
			MutantDto mutantDto = mutantDtoDataBuilder.build();
			final Response response = target("mutantservice/mutant").request().post(Entity.json(mutantDto),Response.class);
			String responseAsString = response.readEntity(String.class);
			assertEquals(400, response.getStatus());
			ResponseService responseService = gson.fromJson(responseAsString, ResponseService.class);
			assertEquals(Constant.MESSAGE_MANDATORY_SEQUENCE, responseService.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void isNotMutantTest() {
		try {
			String[] dna = {"TTGCAA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
			mutantDtoDataBuilder.addDna(dna);
			MutantDto mutantDto = mutantDtoDataBuilder.build();
			final Response response = target("mutantservice/mutant").request().post(Entity.json(mutantDto),Response.class);
			String responseAsString = response.readEntity(String.class);
			assertEquals(403, response.getStatus());
			ResponseService responseService = gson.fromJson(responseAsString, ResponseService.class);
			assertEquals("La sequencia de ADN no pertenece a un mutante", responseService.getMessage());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	
	@Test
	public void statsTest() {
		try {
			int id = 1;
			final Response response = target("mutantservice/stats/"+id).request().get();
			assertEquals(200, response.getStatus());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void noStatsTest() {
		try {
			int id = 0;
			final Response response = target("mutantservice/stats/"+id).request().get();
			assertEquals(204, response.getStatus());
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	@Test
	public void exceptionTest() {
		try {
			String[] dna = {"TTGCAA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"};
			mutantDtoDataBuilder.addDna(dna);
			MutantDto mutantDto = mutantDtoDataBuilder.build();
			MutantException mutantException = new MutantException(Constant.MESSAGE_ERROR);
			mutantController = Mockito.mock(MutantController.class);
			Mockito.when(mutantController.isMutant(dna)).thenThrow(mutantException);
			final Response response = target("mutantservice/mutant").request().post(Entity.json(mutantDto),Response.class);
			String responseAsString = response.readEntity(String.class);
			assertEquals(500, response.getStatus());
			ResponseService responseService = gson.fromJson(responseAsString, ResponseService.class);
			assertTrue(responseService.getMessage().contains(Constant.MESSAGE_ERROR));
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	
}
