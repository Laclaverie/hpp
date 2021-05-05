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

/*
 * public void updateChaineArrayList(String s) {
 * 
 * String data[] = getInformation(s); // data[0] = id, data[1]=dateContamined,
 * data[2] = idContaminedBy Integer id = Integer.parseInt(data[0]); Integer
 * dateContamined = Integer.parseInt(data[1]); Integer idContaminedBy; int index
 * = -1; System.out.println(data[2]); if (data[2].equals("unknow")) {
 * System.out.println("je suis là"); idContaminedBy = -1; } else {
 * System.out.println("je suis ici"); idContaminedBy =
 * Integer.parseInt(data[2]); }
 * 
 * if (chaineArrayList_.size() == 0 || idContaminedBy == -1) {// on assume le
 * fait que le premier malade n'a été // contaminé par personne
 * creerNouvelleChaineArrayList(id, dateContamined);
 * 
 * } else {
 * 
 * int i = 0; int j = 0; int k = 0; boolean contains = false; int taille =
 * chaineArrayList_.size();
 * 
 * do { while (j < chaineArrayList_.get(i).size()) { // parcourir la ième chaine
 * de contamination. while (k < chaineArrayList_.get(i).get(j).size()) {//
 * Parcourir la liste des malades de la chaine if
 * (chaineArrayList_.get(i).get(j).get(k).get(0) == idContaminedBy) { contains =
 * true; index = i; } k++; }
 * 
 * j++;
 * 
 * } i++;
 * 
 * } while (!contains && i < taille); // si jamais la bonne chaine est trouvée,
 * on finit de parcourir la chaine // actuelle, mais on ne parcours pas les
 * chaines suivantes
 * 
 * if (contains) { // rajouter le malade en fin de chaine
 * chaineArrayList_.get(index).add(creerNouveauMaladeArrayList(id,
 * dateContamined)); } }
 * 
 * }
 */
/*
 * public void updateChaineLinkedList(String s) { String data[] =
 * getInformation(s); // data[0] = id, data[1]=dateContaminedBy, data[2] =
 * idContaminedBy
 * 
 * if (chaine_.size() == 0) { // la personne considérée est la première
 * contaminée } }
 * 
 * public ArrayList<ArrayList<Integer>> creerNouveauMaladeArrayList(Integer id,
 * Integer date) { ArrayList<ArrayList<Integer>> nouveauMalade = new
 * ArrayList<ArrayList<Integer>>(2);
 * 
 * nouveauMalade.add(new ArrayList<Integer>()); // id du malade
 * nouveauMalade.add(new ArrayList<Integer>()); // date pour laquelle il a été
 * malade nouveauMalade.get(0).add(id); nouveauMalade.get(1).add(date);
 * 
 * return nouveauMalade; }
 * 
 * public void creerNouvelleChaineArrayList(Integer id, Integer date) {
 * 
 * ArrayList<ArrayList<ArrayList<Integer>>> nouvelleChaineContamination = new
 * ArrayList<ArrayList<ArrayList<Integer>>>();
 * nouvelleChaineContamination.add(creerNouveauMaladeArrayList(id, date));
 * 
 * chaineArrayList_.add(nouvelleChaineContamination); }
 * 
 * public String[] getInformation(String s) {
 * 
 * 
 * return s.split(",");
 * 
 * }
 * 
 * public ArrayBlockingQueue<String> getReaderqueue() { return readerqueue_; }
 * 
 * public void setReaderqueue(ArrayBlockingQueue<String> readerqueue) {
 * this.readerqueue_ = readerqueue; }
 * 
 * public ArrayBlockingQueue<String> getWritterqueue() { return writterqueue_; }
 * 
 * public void setWritterqueue(ArrayBlockingQueue<String> writterqueue) {
 * this.writterqueue_ = writterqueue; }
 * 
 * public LinkedList<LinkedList<String>> getChaine() { return chaine_; }
 * 
 * public void setChaine(LinkedList<LinkedList<String>> chaine) { this.chaine_ =
 * chaine; }
 * 
 * public ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>
 * getChaineArrayList_() { return chaineArrayList_; }
 * 
 * public void
 * setChaineArrayList_(ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>>
 * chaineArrayList_) { this.chaineArrayList_ = chaineArrayList_; } }
 */
