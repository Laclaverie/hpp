package coronavirusTrack;

public class Malade {

	private long id_;
	private long dateContamined_;
	private long idContaminedBy_;
	public Malade(long []data) {
		setId_(data[0]);
		setDateContamined_(data[1]);
		setIdContaminedBy_(data[2]);
		
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
}
 
