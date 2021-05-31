package co.com.mutantteam.utility;

/**
 * 
 * @author juquintero
 *
 */
public class Constant {
	
	private Constant() {
		throw new IllegalStateException("Constant class");
	}
	
	public static final String MESSAGE_NOT_MUTANT = "La sequencia de ADN no pertenece a un mutante";
	public static final String MESSAGE_NOT_STATS = "No se encontaron estadisticas";
	public static final String MESSAGE_ERROR_DATA_BASE = "Error al configurar base de datos";
	public static final String MESSAGE_ERROR = "Error al validar mutante";
	public static final String MESSAGE_ERROR_QUERY = "Error al ejecutar query";
	public static final String MESSAGE_ERROR_INSERT = "Error al insertar valores";
	public static final String MESSAGE_MANDATORY_SEQUENCE = "Debe enviar una sequencia de ADN para verificar";
	public static final String MESSAGE_IS_MUTANT = "La sequencia de ADN pertenece a un mutante";
	public static final String ADENINE = "A";
	public static final String GUANINE = "G";
	public static final String CYTOSINE = "C";
	public static final String THYMINE = "T";
	public static final int COLUMNS = 6;
	public static final int ROWS = 6;
	public static final int TOTAL_SEQUENCES_IS_MUTANT = 2;
	public static final int NUMBER_EQUAL_BASES = 4;
	
	public static final String SEQUENCE = "CREATE SEQUENCE  IF NOT EXISTS MUTANT_ID START WITH 1 INCREMENT BY 1";
	public static final String GET_SEQUENCE_VAL = "select MUTANT_ID.nextval from dual;";
	
	public static final String QUERY_CREATE_TABLE_STATS = "CREATE TABLE IF NOT EXISTS dna_stats " + 
            "(id NUMBER NOT NULL PRIMARY KEY, " + 
            " sequence VARCHAR(100), " +  
            " count_mutant_dna INTEGER, " +  
            " count_human_dna INTEGER, " +  
            " ratio INTEGER)";  
		
	public static final String INSERT_STATS_SQL = "INSERT INTO dna_stats" +
		        "  (id,sequence, count_mutant_dna, count_human_dna, ratio) VALUES " +
		        " (?,?, ?, ?, ?);";
	
	
	public static final String SELECT_STATS_SQL = "select count_mutant_dna,count_human_dna,ratio from dna_stats where id =?";

}
