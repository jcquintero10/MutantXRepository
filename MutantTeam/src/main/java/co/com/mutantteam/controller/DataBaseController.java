package co.com.mutantteam.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		Connection connection = null;
		Statement statement = null;
		try {
			connection = DataBaseUtil.getConnection();
			statement = connection.createStatement();
			statement.execute(Constant.SEQUENCE);

		} catch (Exception e) {
			throw new MutantException(e.getMessage());
		} finally {
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public void createTable(String query) throws Exception {
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
			if (connection != null)
				connection.close();
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
			if (connection != null)
				connection.close();
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
				preparedStatement = connection.prepareStatement(Constant.INSERT_STATS_SQL,
						Statement.RETURN_GENERATED_KEYS);
				int cont = 1;
				preparedStatement.setString(cont++, mutantStat.getSequence());
				preparedStatement.setInt(cont++, mutantStat.getCountMutantSequence());
				preparedStatement.setInt(cont++, mutantStat.getCountHumanSequence());
				preparedStatement.setDouble(cont++, mutantStat.getRatio());

				id = preparedStatement.executeUpdate();
				connection.commit();
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					id = (int) rs.getLong(1);
				} else {
					System.out.println("Message => Generated Key cannot be accessed.");
				}
			}

		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + " " + query + " " + e.getMessage());
		} finally {
			if (preparedStatement != null)
				preparedStatement.close();
			if (connection != null)
				connection.close();
		}

		return id;
	}

}
