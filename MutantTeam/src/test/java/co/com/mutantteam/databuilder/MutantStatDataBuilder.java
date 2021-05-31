package co.com.mutantteam.databuilder;

import co.com.mutantteam.model.MutantStat;

/**
 * 
 * @author juquintero
 *
 */
public class MutantStatDataBuilder {
	
	private int id;
	private String sequence;
	private int countMutantSequence;
	private int countHumanSequence;
	private double ratio;
	private boolean isMutant;
	
	public MutantStatDataBuilder() {
		this.id = 1;
		this.sequence = "TTGCAACAGTGCTTATGTAGAAGGCCCCTATCACTG";
		this.countHumanSequence = 3;
		this.countMutantSequence = 33;
		this.ratio = 9.0;
		this.isMutant = true;
	}
	
	public MutantStatDataBuilder addId(int id) {
		this.id = id;
		return this;
	}
	public MutantStatDataBuilder addSequence(String sequence) {
		this.sequence = sequence;
		return this;
	}
	public MutantStatDataBuilder addCountMutantSequence(int countMutantSequence) {
		this.countMutantSequence = countMutantSequence;
		return this;
	}
	public MutantStatDataBuilder addCountHumanSequence(int countHumanSequence) {
		this.countHumanSequence = countHumanSequence;
		return this;
	}
	public MutantStatDataBuilder addRatio(double ratio) {
		this.ratio = ratio;
		return this;
	}
	public MutantStatDataBuilder addIsMutant(boolean isMutant) {
		this.isMutant = isMutant;
		return this;
	}
	
	
	public MutantStat build() {
		MutantStat mutantStat = new MutantStat();
		mutantStat.setId(this.id);
		mutantStat.setSequence(this.sequence);
		mutantStat.setCountMutantSequence(this.countMutantSequence);
		mutantStat.setCountHumanSequence(this.countHumanSequence);
		mutantStat.setRatio(this.ratio);
		mutantStat.setMutant(this.isMutant);
		
		return mutantStat;
	}
	
	

}
