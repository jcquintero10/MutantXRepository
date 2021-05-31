package co.com.mutantteam.controller;

import co.com.mutantteam.model.MutantStat;

/**
 * 
 * @author juquintero
 *
 */
public interface IDataBaseController {

	void createTable(String query) throws Exception;
	
	void createSequence() throws Exception;

	MutantStat selectTable(String query, int id) throws Exception;

	int insertTable(String query, Object obj) throws Exception;
	
	public int getSequenceVal(String query) throws Exception;
}
