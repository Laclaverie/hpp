package coronavirusTrack;

public class Malade {

	private long id_;
	private long dateContamined_;
	private long idContaminedBy_;
	private long idPays_;
	private long score_=0;
	public Malade(long []data) {
		setId_(data[0]);
		setDateContamined_(data[1]);
		setIdContaminedBy_(data[2]);
		setIdPays_(data[3]);
		
		
	}
	public void printMalade() {
		System.out.println("\nId: "+id_+"\nDate de contamination : "+dateContamined_+"\nID de la personne qui a contaminé : "+ idContaminedBy_);
	}
	public long getId_() {
		return id_;
	}
	public void setId_(long id_) {
		this.id_ = id_;
	}
	public long getDateContamined_() {
		return dateContamined_;
	}
	public void setDateContamined_(long dateContamined_) {
		this.dateContamined_ = dateContamined_;
	}
	public long getIdContaminedBy_() {
		return idContaminedBy_;
	}
	public void setIdContaminedBy_(long idContaminedBy_) {
		this.idContaminedBy_ = idContaminedBy_;
	}
	public long getIdPays_() {
		return idPays_;
	}
	public void setIdPays_(long idPays_) {
		this.idPays_ = idPays_;
	}
	public long getScore_() {
		return score_;
	}
	public void setScore_(long score_) {
		this.score_ = score_;
	}
}
 
