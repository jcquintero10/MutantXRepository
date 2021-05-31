package co.com.mutantteam.databuilder;

import co.com.mutantteam.model.ResponseService;

/**
 * 
 * @author juquintero
 *
 */
public class ResponseServiceDataBuilder {

	private String message;

	public ResponseServiceDataBuilder() {
		this.message = "La sequencia de ADN pertenece a un mutante  ID:  1";
	}

	public ResponseServiceDataBuilder addMessage(String message) {
		this.message = message;
		return this;
	}

	public ResponseService build() {
		ResponseService responseService = new ResponseService();
		responseService.setMessage(this.message);
		return responseService;
	}

}
