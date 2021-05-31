package co.com.mutantteam.controller;

import co.com.mutantteam.model.MutantStat;

/**
 * 
 * @author juquintero
 *
 */
public interface IMutantController {

	MutantStat isMutant(String[] sequenceDNA) throws Exception;

	MutantStat getStatsMutantDNA(int id) throws Exception;

}
