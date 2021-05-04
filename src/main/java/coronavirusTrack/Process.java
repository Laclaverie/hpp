package coronavirusTrack;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class Process {
	private ArrayBlockingQueue<String> readerqueue_; // non utilisé dans un premier temps 
	private ArrayBlockingQueue<String> writterqueue_; // non utilisé dans un premier temps
	private LinkedList<String> chaine_ = null;

	public Process(ArrayBlockingQueue<String> readerqueue, ArrayBlockingQueue<String> writterqueue) {
		this.readerqueue_ = readerqueue;
		this.writterqueue_ = writterqueue;
	}
	
	
	
	public void parseQueue() {
		/**
		 * Extraire les informations de la queue
		 *
		 */
	}

	public void putIntoQueue(String s) {
		/**
		 * Mets en forme les datas et les envois dans la queue d'écriture
		 */
		
	}

	public String[] getInformation(String s) {
		/**
		 * Entree : string du type "id,diagnosed_ts,contaminated_by"
		 * 
		 */
		return s.split(",");

	}

	public ArrayBlockingQueue<String> getReaderqueue() {
		return readerqueue_;
	}

	public void setReaderqueue(ArrayBlockingQueue<String> readerqueue) {
		this.readerqueue_ = readerqueue;
	}

	public ArrayBlockingQueue<String> getWritterqueue() {
		return writterqueue_;
	}

	public void setWritterqueue(ArrayBlockingQueue<String> writterqueue) {
		this.writterqueue_ = writterqueue;
	}

	public LinkedList<String> getChaine() {
		return chaine_;
	}

	public void setChaine(LinkedList<String> chaine) {
		this.chaine_ = chaine;
	}
}
