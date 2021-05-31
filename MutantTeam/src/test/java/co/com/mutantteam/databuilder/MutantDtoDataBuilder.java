package co.com.mutantteam.databuilder;

import co.com.mutantteam.model.MutantDto;

/**
 * 
 * @author juquintero
 *
 */
public class MutantDtoDataBuilder {
	
	private String[] dna;
	
	public MutantDtoDataBuilder addDna(String[] dna) {
		this.dna = dna;
		return this;
	}
	
	public MutantDto build() {
		MutantDto mutantDto = new MutantDto();
		mutantDto.setDna(this.dna);
		return mutantDto;
	}

}
