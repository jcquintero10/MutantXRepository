package co.com.mutantteam.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import co.com.mutantteam.model.MutantStat;
import co.com.mutantteam.utility.Constant;
import co.com.mutantteam.utility.DataBaseUtil;
import co.com.mutantteam.utility.MutantException;

/**
 * 
 * @author juquintero
 *
 */
public class DataBaseController implements IDataBaseController {

	public void createSequence() throws Exception {		
		executeQuery(Constant.SEQUENCE);
	}
	
	public int getSequenceVal(String nameSequence) throws Exception {
		Connection connection = null;
		Statement statement = null;
		int value = 0;
		try {
			if (nameSequence != null && !nameSequence.trim().isEmpty()) {
				connection = DataBaseUtil.getConnection();
				statement = connection.createStatement();
				ResultSet rs = statement.executeQuery(nameSequence);
				if(rs.next()) {
					value = (int)rs.getLong(1);
				}
			}

		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + " " + nameSequence + " " + e.getMessage());
		} finally {
			if (statement != null)
				statement.close();
			if (connection != null) {
				connection.commit();
				connection.close();
			}
		}
		
		return value;	
	}

	@Override
	public void createTable(String query) throws Exception {
		executeQuery(query);
	}

	private void executeQuery(String query) throws MutantException, SQLException {
		Connection connection = null;
		Statement statement = null;
		try {
			if (query != null && !query.trim().isEmpty()) {
				connection = DataBaseUtil.getConnection();
				statement = connection.createStatement();
				statement.execute(query);
			}

		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + " " + query + " " + e.getMessage());
		} finally {
			if (statement != null)
				statement.close();
			if (connection != null) {
				connection.commit();
				connection.close();
			}
		}
	}

	@Override
	public MutantStat selectTable(String query, int id) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		MutantStat mutantStat = null;
		try {
			if (query != null && !query.trim().isEmpty()) {
				connection = DataBaseUtil.getConnection();
				preparedStatement = connection.prepareStatement(Constant.SELECT_STATS_SQL);
				int cont = 1;
				preparedStatement.setInt(cont++, id);
				rs = preparedStatement.executeQuery();

				while (rs.next()) {
					mutantStat = new MutantStat();
					int countMutant = rs.getInt("count_mutant_dna");
					int countHuman = rs.getInt("count_human_dna");
					double radio = rs.getDouble("ratio");
					mutantStat.setCountMutantSequence(countMutant);
					mutantStat.setCountHumanSequence(countHuman);
					mutantStat.setRatio(radio);
				}
			}

		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + " " + query + " " + e.getMessage());
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null) {
				connection.commit();
				connection.close();
			}				
		}

		return mutantStat;
	}

	@Override
	public int insertTable(String query, Object obj) throws Exception {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int id = 0;
		try {
			if (query != null && !query.trim().isEmpty() && obj instanceof MutantStat) {
				MutantStat mutantStat = (MutantStat) obj;
				connection = DataBaseUtil.getConnection();
				preparedStatement = connection.prepareStatement(Constant.INSERT_STATS_SQL);
				int cont = 1;
				preparedStatement.setInt(cont++, mutantStat.getId());
				preparedStatement.setString(cont++, mutantStat.getSequence());
				preparedStatement.setInt(cont++, mutantStat.getCountMutantSequence());
				preparedStatement.setInt(cont++, mutantStat.getCountHumanSequence());
				preparedStatement.setDouble(cont++, mutantStat.getRatio());

				id = preparedStatement.executeUpdate();				
			}

		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + " " + query + " " + e.getMessage());
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null) {
				connection.commit();
				connection.close();
			}
		}

		return id;
	}

}
