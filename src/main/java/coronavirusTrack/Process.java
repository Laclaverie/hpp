package coronavirusTrack;

import java.awt.List;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class Process {
	private ArrayBlockingQueue<long[]> readerqueue_; // non utilisé dans un premier temps
	private ArrayBlockingQueue<long[]> writterqueue_; // non utilisé dans un premier temps
	private LinkedList<LinkedList<Malade>> chaine_ = new LinkedList<>();
	private HashMap<Long, Integer> map = new HashMap<Long, Integer>();

	// private ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> chaineArrayList_
	// =new ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>();

	public Process(ArrayBlockingQueue<long[]> readerqueue, ArrayBlockingQueue<long[]> writterqueue) {
		/**
		 * Dans la HashMap <ID de la personne infectée, ID de la première personne de la
		 * chaine>
		 */
		this.setReaderqueue_(readerqueue);
		this.setWritterqueue_(writterqueue);
	}

	public void parseQueue() {

	}

	public void putIntoQueue(String s) {

	}

	public void updateChaine(long[] data) {
		Malade m = new Malade(data);
		Boolean testUnkow = (data[2] == -1);
		Boolean testSize = (chaine_.size() == 0);
		if (testUnkow || testSize) { // créer une nouvelle chaine de contamination
			chaine_.add(createNewChain(m));
			map.put(data[0], chaine_.size());
		} else { // deux cas : soit on trouve le contaminant soit on ne le trouve pas (et donc il
					// y a un problème dans ce qui a été rentré

			Integer chainNumber = map.get(data[2]);
			if (chainNumber==null) { // alors on n'a pas trouvé notre contaminant dans la table de hash
				System.out.println(" La personne qui a pour ID : " + data[2] + " qui a contaminé " + data[0]
						+ " n'a pas été trouvée ! \n Une nouvelle chaine a été crée à partir de la personne d'ID : "
						+ data[0] + " \nMerci de faire attention !");
				chaine_.add(createNewChain(m));
				map.put(data[0], chaine_.size());
			} else {
				chaine_.get(chainNumber).add(m);

			}

		}
	}

	public LinkedList<Malade> createNewChain(Malade m) {
		LinkedList<Malade> newChain = new LinkedList<Malade>();
		newChain.add(m);
		return newChain;
	}

	public LinkedList<LinkedList<Malade>> getChaine_() {
		return chaine_;
	}

	public void setChaine_(LinkedList<LinkedList<Malade>> chaine_) {
		this.chaine_ = chaine_;
	}

	public ArrayBlockingQueue<long[]> getWritterqueue_() {
		return writterqueue_;
	}

	public void setWritterqueue_(ArrayBlockingQueue<long[]> writterqueue_) {
		this.writterqueue_ = writterqueue_;
	}

	public ArrayBlockingQueue<long[]> getReaderqueue_() {
		return readerqueue_;
	}

	public void setReaderqueue_(ArrayBlockingQueue<long[]> readerqueue_) {
		this.readerqueue_ = readerqueue_;
	}

	public HashMap<Long, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<Long, Integer> map) {
		this.map = map;
	}

}
