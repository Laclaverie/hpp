package coronavirusTest;

import static org.junit.Assert.*;

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
		
		long[] m1 = {1,122555,150};
		long[] m2 = {2,1225545,151};
		long[] m3 = {3,111111,2};
	
		Malade M1=new Malade(m1);
		Malade M2 = new Malade(m2);
		Malade M3 = new Malade (m3);
		
		monP.updateChaine(m1);
		assertEquals(1,monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(1, monP.getChaine_().get(0).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(150, monP.getChaine_().get(0).get(0).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M1.getId_())); // vérification hashtable
		
		
		monP.updateChaine(m2);
		
		assertEquals(2, monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(2, monP.getChaine_().get(1).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(151, monP.getChaine_().get(1).get(0).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M2.getId_())); // vérification hashtable
		
		monP.updateChaine(m3);
		
		assertEquals(2, monP.getChaine_().size()); // nombre de chaines de contamination
		assertEquals(2, monP.getChaine_().get(1).get(0).getId_()); // vérification que c'est le bon ID
		assertEquals(2, monP.getChaine_().get(1).get(1).getIdContaminedBy_()); //Verification bon ID de contaminé
		assertEquals(true,monP.getMap().containsKey(M3.getId_())); // vérification hashtable
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*String s1 = "10,1552244,2";
		String s2 = "11,1544555,unknow";
		String s3 = "12,157996,10";
		String s4 = "12,157996,10";
		

		String r1[] = monP.getInformation(s1);
		assertEquals("10", r1[0]);
		assertEquals("1552244", r1[1]);
		assertEquals("2", r1[2]);

		// --- updateChaineArrayList --- 

		// ajouter un premier malade


		monP.updateChaineArrayList(s1);
		assertEquals(1, monP.getChaineArrayList_().size());
		assertEquals(10, (int) monP.getChaineArrayList_().get(0).get(0).get(0).get(0));
		
		
		//--- ajouter un premier malade qui ne connait pas son prédécésseur ---
		monP2.updateChaineArrayList(s2);
		assertEquals(1, monP2.getChaineArrayList_().size());
		assertEquals(11, (int) monP2.getChaineArrayList_().get(0).get(0).get(0).get(0));

		//--- Agrandir une chaine x1 ---
		monP.updateChaineArrayList(s3);
		assertEquals(1, monP.getChaineArrayList_().size());
		assertEquals(12, (int) monP.getChaineArrayList_().get(0).get(1).get(0).get(0));
		
		//--- Agrandir une chaine x2 ---
		monP.updateChaineArrayList(s4);
		assertEquals(1, monP.getChaineArrayList_().size());
		assertEquals(12, (int) monP.getChaineArrayList_().get(0).get(1).get(0).get(0));

		*/
	}

}
