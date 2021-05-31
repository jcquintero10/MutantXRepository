package co.com.mutantteam.model;

/**
 * 
 * @author juquintero
 *
 */
public class MutantStat {
	
	private int id;
	private String sequence;
	private int countMutantSequence;
	private int countHumanSequence;
	private double ratio;
	private boolean isMutant;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public int getCountMutantSequence() {
		return countMutantSequence;
	}
	public void setCountMutantSequence(int countMutantSequence) {
		this.countMutantSequence = countMutantSequence;
	}
	public int getCountHumanSequence() {
		return countHumanSequence;
	}
	public void setCountHumanSequence(int countHumanSequence) {
		this.countHumanSequence = countHumanSequence;
	}
	public double getRatio() {
		return ratio;
	}
	public void setRatio(double ratio) {
		this.ratio = ratio;
	}
	public boolean isMutant() {
		return isMutant;
	}
	public void setMutant(boolean isMutant) {
		this.isMutant = isMutant;
	}
	
	public String toString() {
		return "count_mutant_dna:" + getCountMutantSequence() + "," + " count_human_dna:" + getCountHumanSequence() + "," + " ratio:" + getRatio();
	}
	
}
