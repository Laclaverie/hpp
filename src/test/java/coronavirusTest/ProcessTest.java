package coronavirusTest;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import coronavirusTrack.Malade;
import coronavirusTrack.Process;

public class ProcessTest {

	@Test
	public void test() {
		ArrayBlockingQueue<long[]> readerqueue_ = new ArrayBlockingQueue<long[]>(20); // non utilisé dans un premier
																						// temps
		ArrayBlockingQueue<long[]> writterqueue_ = new ArrayBlockingQueue<long[]>(20); // non utilisé dans un premier
																						// temps
		// --- get information --- 
		Process monP = new Process(readerqueue_, writterqueue_);
		Process monP2 = new Process(readerqueue_, writterqueue_);
		
		long[] m1 = {1,122555,150,0};
		long[] m2 = {2,1225545,151,1};
		long[] m3 = {3,111111,2,2};
		long[] m4 = {4,122555,1,0};
		long[] m5 = {5,1225547,2,1};
		long[] m6 = {6,111112,2,2};
		long[] m7 = {7,122556,1,0};
		long[] m8 = {8,1225546,1,1};
		long[] m9 = {9,111113,2,2};
	
		Malade M1=  new Malade(m1);
		Malade M2 = new Malade(m2);
		Malade M3 = new Malade(m3);
		Malade M4=  new Malade(m4);
		Malade M5 = new Malade(m5);
		Malade M6 = new Malade(m6);
		Malade M7=  new Malade(m7);
		Malade M8 = new Malade(m8);
		Malade M9 = new Malade(m9);
		System.out.println("\nCas1 ");
		monP.updateChaine(m1);

		assertEquals(1,monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(1, monP.getChaine_().get(0).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(150, monP.getChaine_().get(0).get(0).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M1.getId_())); // vérification hashtable
	//	assertEquals((Integer)0, monP.getMapCountryChain().get(M1.getIdPays_()));
		
		System.out.println("\nCas 2 :");
		monP.updateChaine(m2);

		assertEquals(2, monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(2, monP.getChaine_().get(1).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(151, monP.getChaine_().get(1).get(0).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M2.getId_())); // vérification hashtable
	//	assertEquals((Integer)1, monP.getMapCountryChain().get(M2.getIdPays_()));

		System.out.println("\nCas 3 : ");
		monP.updateChaine(m3);

		assertEquals(3, monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(3, monP.getChaine_().get(2).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(2, monP.getChaine_().get(2).get(0).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M3.getId_())); // vérification hashtable
	//	assertEquals((Integer)2, monP.getMapCountryChain().get(M3.getIdPays_()));

		System.out.println(" largest Chain \n");
		monP.updateChaine(m4);
		monP.updateChaine(m5);
		monP.updateChaine(m6);
		monP.updateChaine(m7);
		monP.updateChaine(m8);
		monP.updateChaine(m9);
		
		monP.findLargestChains();
		monP.putIntoQueue();
		
	}
}
