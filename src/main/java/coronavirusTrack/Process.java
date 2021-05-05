package coronavirusTrack;

import java.awt.List;
import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

public class Process {
	private ArrayBlockingQueue<String> readerqueue_; // non utilisé dans un premier temps
	private ArrayBlockingQueue<String> writterqueue_; // non utilisé dans un premier temps
	private LinkedList<LinkedList<String>> chaine_ = null;
	private ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> chaineArrayList_ = null;

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

	public void updateChaineArrayList(String s) {
		/**
		 * Entrée @param s :{@code String} : un string du type "id,date,idContaminedby"
		 */
		String data[] = getInformation(s); // data[0] = id, data[1]=dateContamined, data[2] = idContaminedBy
		Integer id = Integer.parseInt(data[0]);
		Integer dateContamined = Integer.parseInt(data[1]);
		Integer idContaminedBy;
		int index = -1;
		if (data[2] == "unknow") {
			idContaminedBy = -1;
		} else {
			idContaminedBy = Integer.parseInt(data[2]);
		}

		if (chaineArrayList_.size() == 0 || idContaminedBy == -1) {// on assume le fait que le premier malade n'a été
																	// contaminé par personne
			creerNouvelleChaineArrayList(id, dateContamined);

		} else {

			int i = 0;
			int j = 0;
			int k = 0;
			boolean contains = false;
			int taille = chaineArrayList_.size();

			do {
				while (j < chaineArrayList_.get(i).size()) { // parcourir la ième chaine de contamination.
					while (k < chaineArrayList_.get(i).get(j).size()) {// Parcourir la liste des malades de la chaine

						if (chaineArrayList_.get(i).get(j).get(k).get(0) == idContaminedBy) {
							contains = true;
							index = i;
						}
						k++;
					}

					j++;

				}
				i++;

			} while (!contains && i < taille); // si jamais la bonne chaine est trouvée, on finit de parcourir la chaine
												// actuelle, mais on ne parcours pas les chaines suivantes

			if (contains) { // rajouter le malade en fin de chaine
				chaineArrayList_.get(index).add(creerNouveauMaladeArrayList(id, dateContamined));
			}
		}

	}

	public void updateChaineLinkedList(String s) {
		String data[] = getInformation(s); // data[0] = id, data[1]=dateContaminedBy, data[2] = idContaminedBy

		if (chaine_.size() == 0) {
			// la personne considérée est la première contaminée
		}
	}

	public ArrayList<ArrayList<Integer>> creerNouveauMaladeArrayList(Integer id, Integer date) {
		ArrayList<ArrayList<Integer>> nouveauMalade = new ArrayList<ArrayList<Integer>>(2);

		nouveauMalade.add(new ArrayList<Integer>()); // id du malade
		nouveauMalade.add(new ArrayList<Integer>()); // date pour laquelle il a été malade
		nouveauMalade.get(0).add(id);
		nouveauMalade.get(1).add(date);

		return nouveauMalade;
	}

	public void creerNouvelleChaineArrayList(Integer id, Integer date) {

		ArrayList<ArrayList<ArrayList<Integer>>> nouvelleChaineContamination = new ArrayList<ArrayList<ArrayList<Integer>>>();
		nouvelleChaineContamination.add(creerNouveauMaladeArrayList(id, date));

		chaineArrayList_.add(nouvelleChaineContamination);
	}

	public String[] getInformation(String s) {
		/**
		 * Entree @param : s a {@code String} du type "id,diagnosed_ts,contaminated_by"
		 * Sortie
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

	public LinkedList<LinkedList<String>> getChaine() {
		return chaine_;
	}

	public void setChaine(LinkedList<LinkedList<String>> chaine) {
		this.chaine_ = chaine;
	}

	public ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> getChaineArrayList_() {
		return chaineArrayList_;
	}

	public void setChaineArrayList_(ArrayList<ArrayList<ArrayList<ArrayList<Integer>>>> chaineArrayList_) {
		this.chaineArrayList_ = chaineArrayList_;
	}
}
