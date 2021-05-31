package co.com.mutantteam.utility;

/**
 * 
 * @author juquintero
 *
 */
public class MutantException extends Exception {
	
private static final long serialVersionUID = 1L;
	
	public MutantException() {
		super();
	}

	public MutantException(String mensaje) {
		super(mensaje);
	}

	public MutantException(Throwable throwable) {
		super(throwable);
	}

	public MutantException(String mensaje,Throwable throwable) {
		super(mensaje, throwable);
	}

}
