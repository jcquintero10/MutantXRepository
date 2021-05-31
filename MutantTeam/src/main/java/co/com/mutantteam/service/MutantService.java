package co.com.mutantteam.service;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.com.mutantteam.controller.IMutantController;
import co.com.mutantteam.model.MutantDto;
import co.com.mutantteam.model.MutantStat;
import co.com.mutantteam.model.ResponseService;
import co.com.mutantteam.utility.Constant;


/**
 * 
 * @author juquintero
 *
 */
@Path("/mutantservice")
public class MutantService {
	
	@Inject
	private IMutantController mutantController;
	Logger log = Logger.getLogger(MutantService.class.getName());
	private Gson gson;
	
	public MutantService() {
		gson = new GsonBuilder().setPrettyPrinting().create();
	}
	
	@POST
	@Path("/mutant")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response isMutant(MutantDto mutantDto) {
		Object message = null;
		Status httpStatus = null;
		try {
			if(mutantDto != null && mutantDto.getDna() != null && mutantDto.getDna().length >0) {
				MutantStat mutante = mutantController.isMutant(mutantDto.getDna());
				if(mutante != null && mutante.getId() >0 && mutante.isMutant()) {
					httpStatus = Response.Status.OK;
					message = Constant.MESSAGE_IS_MUTANT + " "  + " ID: " + " " + mutante.getId();
				}else{
					httpStatus = Response.Status.FORBIDDEN;
					message = Constant.MESSAGE_NOT_MUTANT;
				}
			}else {
				httpStatus = Response.Status.BAD_REQUEST;
				message = Constant.MESSAGE_MANDATORY_SEQUENCE;
			}
			
		} catch (Exception e) {
			log.severe("Error al validar mutante " + e.getMessage());
			httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
			message = e.getMessage();
		}
		
		ResponseService response = new ResponseService();
		response.setMessage((String)message);
		
		String result = gson.toJson(response);
	
		return Response.status(httpStatus).entity(result).build();
	}
	
	
	@GET
	@Path("/stats/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response statsMutant(@PathParam("id") Integer id) {
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
			log.severe("Error al validar mutante " + e.getMessage());
			httpStatus = Response.Status.INTERNAL_SERVER_ERROR;
			message = e.getMessage();
		}
		
		
		ResponseService response = new ResponseService();
		response.setMessage((String)message);
		
		String result = gson.toJson(response);
	    return Response.status(httpStatus).entity(result).build();
	}
}
