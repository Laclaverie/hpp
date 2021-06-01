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
	private HashMap<Long, Integer> map = new HashMap<Long, Integer>(); // <Id du malade,chaine associée>
	private int[] three_largest_chains = new int[3];
	private long[] chainScore;
	long date_ =0;

	public Process(ArrayBlockingQueue<long[]> readerqueue, ArrayBlockingQueue<long[]> writterqueue,long date) {
		/**
		 * Dans la HashMap <ID de la personne infectée, ID de la première personne de la
		 * chaine>
		 */
		this.setReaderqueue_(readerqueue);
		this.setWritterqueue_(writterqueue);
		this.date_=date;
	}

	public void parseQueue() {

	}

	public void putIntoQueue() {
		findScoreOfAllChains();
		
		for (int i=0;i<3;i++) {
			int maxIndex= getIndexOfLargest(chainScore);
			long [] datatoQueue=DataToQueue(maxIndex);
			chainScore[maxIndex]=-1; // enlever le score le plus élevé
			System.out.println("data to send ");
			for(int j=0;j<3;j++) {
				System.out.println(datatoQueue[j]);
			}
		}
		
		/***************** les 3 plus grosses chaines **************/
		
	}

	public void updateChaine(long[] data) {
		Malade m = new Malade(data);
		Boolean testUnkow = (m.getIdContaminedBy_() == -1);
		Boolean testSize = (chaine_.size() == 0);
		if (testUnkow || testSize) { // créer une nouvelle chaine de contamination
			chaine_.add(createNewChain(m));
			map.put(m.getId_(), chaine_.size() - 1);

		} else { // deux cas : soit on trouve le contaminant soit on ne le trouve pas (et donc il
					// y a un problème dans ce qui a été rentré

			Integer chainNumber = map.get(m.getIdContaminedBy_()); // recuperer le numéro de la chaine concernée
			if (chainNumber == null) { // alors on n'a pas trouvé notre contaminant dans la table de hash
				System.out.println(" La personne qui a pour ID : " + m.getIdContaminedBy_() + " qui a contaminé "
						+ m.getId_()
						+ " n'a pas été trouvée ! \n Une nouvelle chaine a été crée à partir de la personne d'ID : "
						+ m.getId_() + " \nMerci de faire attention à vous !");
				chaine_.add(createNewChain(m));
				map.put(m.getId_(), chaine_.size() - 1);
			} else {
				if (m.getIdPays_() != chaine_.get(chainNumber).get(0).getIdPays_()) {
					chaine_.add(createNewChain(m));
					map.put(m.getId_(), chaine_.size() - 1);
				} else {
					if (chaine_.get(chainNumber).size() >= 1) { // on met à jour le score
					//	int beforeLast = chaine_.get(chainNumber).size() - 2;
						m.setScore_(setScoreDate(m.getDateContamined_(),date_));
							//	chaine_.get(chainNumber).get(beforeLast).getDateContamined_())); V1
					}
					chaine_.get(chainNumber).add(m);
					map.put(m.getId_(), chainNumber);

				}

			}

		}
	}

	public void findLargestChains() { // plus longue chaine V1 : n'est pas utile ici
		/**
		 * Après avoir mis en place toutes les chaines, regarder quelles sont les plus
		 * longues map contient <id du malade, chaine pour laquelle il appartient>
		 * mapCountry contient <id du pays, chaine de malade du pays associé>
		 */
		int[] occurrence = new int[map.size()];
		for (Integer idChaine : map.values()) { // on parcourt tous les malades dans la hashmap
			// System.out.println("id de la chaine : " + idChaine);
			occurrence[idChaine] = occurrence[idChaine] + 1;

		}

		for (int i = 0; i < 3; i++) {
			int index = getIndexOfLargest(occurrence);
			three_largest_chains[i] = index;
			occurrence[index] = 0;
			System.out.println(" max index : " + index);
		}

	}
	public void findScoreOfAllChains() {
		setChainScore(new long[chaine_.size()]);
		for (int i=0;i<chaine_.size();i++) {
			chainScore[i]= countScoreInAChain(i);
		}
	}

	public long setScoreDate(long date1, long date2) {
		if (Math.abs(date1 - date2) > 604800 && Math.abs(date1 - date2) <= 1209600) {
			// we set the score at 4
			System.out.println("retourne 4");
			return 4;
		} else {
			// if this date is more than 14 days (exclusive)
			if (Math.abs(date1 - date2) > 1209600) {
				System.out.println(" retourne 0");
				return 0;
			}
		}
		System.out.println("retourne 10");
		return 10;

	}

	public int getIndexOfLargest(int[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}
	public int getIndexOfLargest(long[] array) {
		if (array == null || array.length == 0)
			return -1; // null or empty

		int largest = 0;
		for (int i = 1; i < array.length; i++) {
			if (array[i] > array[largest])
				largest = i;
		}
		return largest; // position of the first largest found
	}

	public long countScoreInAChain(int index) { // version : on compare le patient t avec le t-1
		/**
		 * compter le score d'une chaine à partir de l'index de celle-ci
		 */
		long score = 0;
		Iterator<Malade> it = chaine_.get(index).iterator();
		while (it.hasNext()) {
			score = score + it.next().getScore_();
		}
	//	System.out.println("score =" + score + " index = "+index);
		return score;
	}

	public long[] DataToQueue(int index) {

		/**
		 * on a l'index de la chaine que l'on veut envoyer à la queue, on va maintenant
		 * mettre les données en forme pour les mettres dans la queue
		 */
		long[] dataToSend = new long[3];
		// step 1 : get country of origin
		dataToSend[0] = chaine_.get(index).get(0).getIdPays_();
		// step 2 ; get the root of the chain (id of the first)
		dataToSend[1] = chaine_.get(index).get(0).getId_();
		// step 3 : get the score of the chain -> V1 : the length V2->score
		// dataToSend[2]=(long)chaine_.get(index).size();
		/******************************
		 * A AVANCER ICI
		 *****************************************************************/
		//dataToSend[2] = countScoreInAChain(index); // C'est la V1 ! score ~ patient t & patient t-1
		dataToSend[2]= countScoreInAChain(index); // C'est la V2 : score ~patient t->dateDeContamination & date ACTUELLE
		return dataToSend;

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

	public long[] getChainScore() {
		return chainScore;
	}

	public void setChainScore(long[] chainScore) {
		this.chainScore = chainScore;
	}

}
