package coronavirusTrack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Process implements Runnable {
	private ArrayBlockingQueue<long[]> readerqueue_; // non utilisé dans un premier temps
	private ArrayBlockingQueue<long[]> writterqueue_; // non utilisé dans un premier temps
	private LinkedList<LinkedList<Malade>> chaine_;
	private HashMap<Long, Integer> map; // <Id du malade,chaine associée>
	private HashMap<Integer,Long> chainCountyMap; // <id de la chaine, id du pays>
	private HashMap<Integer,LinkedList<Malade>> chainIndexLinkedList; // <index, chaine de numéro index> 
	private HashMap<Integer,Long> chainScoreMap; // <index, score de la chaine de numéro index>
	private int[] three_largest_chains = new int[3];
	private long[] chainScore;
	long date_ = 0;

	// utilitaire
	ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	Lock readLock = readWriteLock.readLock();
	Lock writeLock = readWriteLock.writeLock();

	public Process(ArrayBlockingQueue<long[]> readerqueue, ArrayBlockingQueue<long[]> writterqueue, 
			long date,
			HashMap<Long, Integer> map_,
			LinkedList<LinkedList<Malade>> chaine,
			HashMap<Integer, Long> chainCountyMap_,
			HashMap<Integer, LinkedList<Malade>> chainIndexLinkedList_,
			HashMap<Integer,Long> chainScoreMap_
			) {
		/**
		 * Dans la HashMap <ID de la personne infectée, ID de la première personne de la
		 * chaine>
		 */
		this.setReaderqueue_(readerqueue);
		this.setWritterqueue_(writterqueue);
		this.date_ = date;
		this.map = map_;
		this.chaine_ = chaine;
		this.setChainCountyMap(chainCountyMap_);
		this.setChainIndexLinkedList(chainIndexLinkedList_);
		this.setChainScoreMap(chainScoreMap_);
	}

	@Override
	public void run() {
		parseQueue();
		putIntoQueue();

	}

	public void parseQueue() {
		boolean stop = false;
		do {
			try {
				long[] data = readerqueue_.take();
				if (isPOISON_PILL(data)) {
					readerqueue_.put(data);
					stop = true;
				} else {
					updateChaine(data);
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!stop);
	}

	public void putIntoQueue() {

		System.out.println("find score");
		findScoreOfAllChains();
		System.out.println("score ok");
		
		for (int i = 0; i < 3; i++) {
			int maxIndex = getIndexOfLargest(chainScore);
			long[] datatoQueue = DataToQueue(maxIndex);
			chainScore[maxIndex] = -1; // enlever le score le plus élevé
			try {
				writterqueue_.put(datatoQueue);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		long[] poison= {-1,-1,-1};
		try {
			writterqueue_.put(poison);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(" finiiiiiiiiiiiiiiiii");
	}

	public void updateChaine(long[] data) {
		Malade m = new Malade(data);
		// m.printMalade();
		//System.out.println("Id du malade = " + m.getId_());
		int chaineSize = chaine_.size(); // pas besoin de le calculer plusieurs fois
		Boolean testUnkow = (m.getIdContaminedBy_() == -1);
		Boolean testSize = (chaineSize == 0);
		if (testUnkow || testSize) { // créer une nouvelle chaine de contamination car vide ou contaminant inconnu
			readLock.lock();
			chaine_.add(createNewChain(m));
			map.put(m.getId_(), chaineSize);
			chainCountyMap.put(chaineSize, m.getIdPays_());
			chainIndexLinkedList.put(chaineSize, createNewChain(m));
			chainScoreMap.put(chaineSize,setScoreDate(m.getDateContamined_(), date_));
			readLock.unlock();

		} else { // deux cas : soit on trouve le contaminant soit on ne le trouve pas (et donc il
					// y a un problème dans ce qui a été rentré

			Integer chainNumber = map.get(m.getIdContaminedBy_()); // recuperer le numéro de la chaine concernée
			if (chainNumber == null) { // alors on n'a pas trouvé notre contaminant dans la table de hash
				/*
				 * System.out.println(" La personne qui a pour ID : " + m.getIdContaminedBy_() +
				 * " qui a contaminé " + m.getId_() +
				 * " n'a pas été trouvée ! \n Une nouvelle chaine a été crée à partir de la personne d'ID : "
				 * + m.getId_() + " \nMerci de faire attention à vous !");
				 */
				readLock.lock();
				chaine_.add(createNewChain(m));
				map.put(m.getId_(), chaineSize);
				chainCountyMap.put(chaineSize, m.getIdPays_());
				chainIndexLinkedList.put(chaineSize, createNewChain(m));
				chainScoreMap.put(chainNumber,setScoreDate(m.getDateContamined_(), date_));
				readLock.unlock();
			} else { // créé une chaine
				if (m.getIdPays_() != chainCountyMap.get(chainNumber)) {
					readLock.lock();
					chaine_.add(createNewChain(m));
					map.put(m.getId_(), chaineSize);
					chainCountyMap.put(chaineSize, m.getIdPays_());
					chainIndexLinkedList.put(chaineSize, createNewChain(m));
					chainScoreMap.put(chainNumber,setScoreDate(m.getDateContamined_(), date_));
					readLock.unlock();
				} else { // update une chaine
					//m.setScore_(setScoreDate(m.getDateContamined_(), date_));
					readLock.lock();
					// chaine_.get(chainNumber).add(m);
					chainIndexLinkedList.get(chainNumber).add(m);
					map.put(m.getId_(), chainNumber);
					chainCountyMap.put(chainNumber, m.getIdPays_());
					chainScoreMap.put(chainNumber, chainScoreMap.get(chainNumber)+setScoreDate(m.getDateContamined_(), date_));
					readLock.unlock();

				}

			}

		}
	}

	public void findLargestChains() { // plus longue chaine V1 : n'est pas utile ici 
		/**
		 * Après avoir mis en place toutes les chaines, regarder quelles sont les plus
		 * longues map contient <id du malade, chaine pour laquelle il appartient>
		 * mapCountry contient <id du pays, chaine de malade du pays associé>
		 * Cette fonction n'est pas utile dans le sens où ce n'est pas la bonne méthode pour calculer le poids d'une chaine
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
			// System.out.println(" max index : " + index);
		}

	}

	public void findScoreOfAllChains() {
		setChainScore(new long[chaine_.size()]);
		for (int i = 0; i < chaine_.size(); i++) {
			chainScore[i] = countScoreInAChain(i);
		}
	}
public void applyRule6() {
	// not implemeted yet
}
	public long setScoreDate(long date1, long date2) {
		if (Math.abs(date1 - date2) > 604800 && Math.abs(date1 - date2) <= 1209600) {
			// we set the score at 4
			// System.out.println("retourne 4");
			return 4;
		} else {
			// if this date is more than 14 days (exclusive)
			if (Math.abs(date1 - date2) > 1209600) {
				// System.out.println(" retourne 0");
				return 0;
			}
		}
		// System.out.println("retourne 10");
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

	public int getIndexOfLargest(long[] array) { //surcharge de la fonction précédente pour s'adapter aux deux cas
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
		
		// System.out.println("score =" + score + " index = "+index);
		return chainScoreMap.get(index);
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
		// dataToSend[2] = countScoreInAChain(index); // C'est la V1 ! score ~ patient t
		// & patient t-1
		dataToSend[2] = countScoreInAChain(index); // C'est la V2 : score ~patient t->dateDeContamination & date
													// ACTUELLE
		return dataToSend;

	}

	public LinkedList<Malade> createNewChain(Malade m) {
		LinkedList<Malade> newChain = new LinkedList<Malade>();
		newChain.add(m);
		return newChain;
	}

	public boolean isPOISON_PILL(long[] data) {
		if (data[0] == -1 && data[1] == -1 && data[2] == -1) {
			return true;
		}
		return false;
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

	public HashMap<Integer, Long> getChainCountyMap() {
		return chainCountyMap;
	}

	public void setChainCountyMap(HashMap<Integer, Long> chainCountyMap) {
		this.chainCountyMap = chainCountyMap;
	}

	public HashMap<Integer, LinkedList<Malade>> getChainIndexLinkedList() {
		return chainIndexLinkedList;
	}

	public void setChainIndexLinkedList(HashMap<Integer, LinkedList<Malade>> chainIndexLinkedList) {
		this.chainIndexLinkedList = chainIndexLinkedList;
	}

	public HashMap<Integer,Long> getChainScoreMap() {
		return chainScoreMap;
	}

	public void setChainScoreMap(HashMap<Integer,Long> chainScoreMap) {
		this.chainScoreMap = chainScoreMap;
	}

}
