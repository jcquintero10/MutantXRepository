package co.com.mutantteam.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import co.com.mutantteam.model.MutantStat;
import co.com.mutantteam.utility.Constant;
import co.com.mutantteam.utility.MutantException;

/**
 * 
 * @author juquintero
 *
 */
public class MutantController implements IMutantController {

	private IDataBaseController databaseController = null;
	 
	@Inject
	public MutantController() throws Exception {
		this.databaseController =  new DataBaseController();
		databaseController.createSequence();
	}
	
		
	@Override
	public MutantStat isMutant(String[] sequenceDNA) throws Exception {
		boolean mutant = false;
		MutantStat mutantStats = null;
		try {
			List<String> bases = setNitrogenBasesToList(sequenceDNA);
			if (!bases.isEmpty()) {
				String[][] dna = convertArrayToMatrix(sequenceDNA);
				List<String> basesFound = new ArrayList<>();
				mutantStats = new MutantStat();
				if (dna.length > 0) {
					mutant = checkIsMutant(mutant, bases, dna, basesFound, mutantStats);
				}
			}
		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR + e.getMessage());
		}

		if (mutant) {
			mutantStats.setMutant(mutant);
			int id = insertStats(mutantStats);
			if(id >0) {
				mutantStats.setId(id);
			}else {
				throw new MutantException(Constant.MESSAGE_ERROR_INSERT);
			}		
		}

		return mutantStats;
	}

	@Override
	public MutantStat getStatsMutantDNA(int id) throws Exception {
		try {
			return databaseController.selectTable(Constant.SELECT_STATS_SQL, id);
		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + e.getMessage());
		}
	}

	/**
	 * @author juquintero
	 * @throws Exception
	 */
	private int insertStats(MutantStat mutantStats) throws Exception {
		try {
			createTable();
			return databaseController.insertTable(Constant.INSERT_STATS_SQL, mutantStats);
		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + e.getMessage());
		}
	}
	
	/**
	 * @author juquintero
	 * @throws Exception
	 */
	private void createTable() throws Exception {
		try {
			databaseController.createTable(Constant.QUERY_CREATE_TABLE_STATS);
		} catch (Exception e) {
			throw new MutantException(Constant.MESSAGE_ERROR_QUERY + e.getMessage());
		}	
	}

	/**
	 * @author juquintero M&eacute;todo encargado de validar si una sequencia de adn
	 *         pertenece a un mutante o no
	 * @param mutant
	 * @param bases
	 * @param dna
	 * @param basesFound
	 * @return
	 */
	private boolean checkIsMutant(boolean mutant, List<String> bases, String[][] dna, List<String> basesFound,
			MutantStat mutantStats) {
		int totalSequenceMutant = 0;
		for (int i = 0; i < bases.size(); i++) {
			String base = bases.get(i);
			if (!checkBaseInList(basesFound, base) && checkMutant(base, dna)) {
				totalSequenceMutant++;
				basesFound.add(base);
			}
		}

		String sequence = bases.stream().map(Object::toString).collect(Collectors.joining(","));
		mutantStats.setSequence(sequence);
		int sequencesHumans = bases.size() - totalSequenceMutant;
		mutantStats.setCountHumanSequence(sequencesHumans);
		mutantStats.setCountMutantSequence(totalSequenceMutant);
		int percent = (int) ((totalSequenceMutant * 100.0f) / sequencesHumans);
		mutantStats.setRatio(percent);
		if (totalSequenceMutant >= Constant.TOTAL_SEQUENCES_IS_MUTANT)
			mutant = true;
		return mutant;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de validar si una base ya fue
	 *         encontrada para un mutante
	 * @param basesFound
	 * @param base
	 * @return
	 */
	private boolean checkBaseInList(List<String> basesFound, String base) {
		boolean found = false;
		if (!basesFound.isEmpty()) {
			found = basesFound.stream().anyMatch(base::contains);
		}

		return found;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de validar si una base se
	 *         encuentra de forma diagonal u horizontal o vertical 4 veces dentro de
	 *         la matriz de sequencias
	 * @param base
	 * @param dna
	 * @return
	 */
	private boolean checkMutant(String base, String[][] dna) {
		boolean mutant = loopHorizontal(dna, base);
		if (!mutant) {
			if (!loopVertical(dna, base)) {
				mutant = loopOblique(dna, base);
			} else {
				return true;
			}
		}

		return mutant;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de recorrer una matriz de forma
	 *         diagonal
	 * @param dna
	 * @param base
	 * @return
	 */
	private boolean loopOblique(String[][] dna, String base) {
		boolean isMutant = false;
		int column = 0;
		int counterEqualBase = 0;
		for (int row = 0; row < dna.length; row++) {
			for (column = row; column < dna[row].length; column++) {
				if (isNitrogenBase(dna[row][column]) && counterEqualBase <= Constant.NUMBER_EQUAL_BASES) {
					String nitrogenBase = dna[row][column];
					if (nitrogenBase.equals(base)) {
						counterEqualBase++;
					}
					break;
				}
			}
			if (counterEqualBase == Constant.NUMBER_EQUAL_BASES) {
				isMutant = true;
				break;
			}
		}

		return isMutant;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de recorrer una matriz de forma
	 *         columna x filas
	 * @param dna
	 * @param base
	 * @return
	 */
	private boolean loopVertical(String[][] dna, String base) {
		boolean isMutant = false;
		for (int column = 0; column < dna[0].length; column++) {
			int counterEqualBase = 0;
			for (int i = 0; i < dna.length; i++) {
				if (isNitrogenBase(dna[i][column]) && counterEqualBase <= Constant.NUMBER_EQUAL_BASES) {
					String nitrogenBase = dna[i][column];
					if (nitrogenBase.equals(base)) {
						counterEqualBase++;
					} else {
						break;
					}
				}
			}
			if (counterEqualBase == Constant.NUMBER_EQUAL_BASES) {
				isMutant = true;
				break;
			}
		}

		return isMutant;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de recorrer una matriz de forma
	 *         fila x columnas
	 * @param dna
	 * @param row
	 * @return
	 */
	private boolean loopHorizontal(String[][] dna, String base) {
		boolean isMutant = false;
		for (int i = 0; i < dna.length; i++) {
			int counterEqualBase = 0;
			for (int j = 0; j < dna[i].length; j++) {
				if (isNitrogenBase(dna[i][j]) && counterEqualBase <= Constant.NUMBER_EQUAL_BASES) {
					String nitrogenBase = dna[i][j];
					if (nitrogenBase.equals(base)) {
						counterEqualBase++;
					} else {
						break;
					}
				}
			}

			if (counterEqualBase == Constant.NUMBER_EQUAL_BASES) {
				isMutant = true;
				break;
			}
		}

		return isMutant;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de validar si es una base
	 *         nitrogenada valida(A,C,T,G)
	 * @param base
	 * @return
	 */
	private boolean isNitrogenBase(String base) {
		boolean isGoodBase = false;
		if (base.equals(Constant.ADENINE) || base.equals(Constant.CYTOSINE) || base.equals(Constant.GUANINE)
				|| base.equals(Constant.THYMINE)) {
			isGoodBase = true;
		}

		return isGoodBase;
	}

	/**
	 * @author juquintero M&eacute;todo encargado de convertir un arreglo de una
	 *         dimensi&oacute;n en una matriz de 6X6
	 * @param sequenceDNA
	 * @return
	 */
	private String[][] convertArrayToMatrix(String[] sequenceDNA) {
		String[][] dna = new String[Constant.ROWS][Constant.COLUMNS];
		for (int i = 0; i <= Constant.ROWS - 1; i++) {
			int counterBases = 0;
			int sequenceSize = sequenceDNA[i].length();
			for (int j = 0; j <= Constant.COLUMNS - 1; j++) {
				if (counterBases < sequenceSize) {
					dna[i][j] = String.valueOf(sequenceDNA[i].charAt(counterBases));
					counterBases++;
				}
			}
		}

		return dna;
	}

	/***
	 * @author juquintero M&eacute;todo encargado de guardar todas las bases
	 *         nitrogenadas en una lista
	 * @param sequenceDNA
	 * @return
	 */
	private List<String> setNitrogenBasesToList(String[] sequenceDNA) {
		List<String> bases = new ArrayList<>();
		for (int i = 0; i < sequenceDNA.length; i++) {
			int counterBases = 0;
			int sequenceSize = sequenceDNA[i].length();
			while (counterBases < sequenceSize && Character.isLetter(sequenceDNA[i].charAt(counterBases))) {
				bases.add(String.valueOf(sequenceDNA[i].charAt(counterBases)));
				counterBases++;
			}
		}
		return bases;
	}

}
