package coronavirusTrack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class main {

	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub
		// Calcul du temps :

		long startTime = System.nanoTime();
		ArrayBlockingQueue<long[]> readerQueue = new ArrayBlockingQueue<long[]>(1000000);
		ArrayBlockingQueue<long[]> writterQueue = new ArrayBlockingQueue<long[]>(3);
		HashMap<Long, Integer> map_ = new HashMap<Long, Integer>();
		LinkedList<LinkedList<Malade>> chaine = new LinkedList<>();
		HashMap<Integer, Long> chainCountyMap = new HashMap<Integer, Long>();
		HashMap<Integer, LinkedList<Malade>> chainIndexLinkedList = new HashMap<Integer, LinkedList<Malade>>();
		HashMap<Integer, Long> chainScoreMap_ = new HashMap<Integer, Long>();

		//-------------------------------
		
		long date = 1589238000;
		
		//-------------------------------
		
		
		File p = new File(".");
		String path = p.getAbsolutePath() + "/csv/1000000";
		System.out.println("début ");
 /* ----------------- C'est ici qu'on choisit si multithread ou non !!!! -------------*/
		// -----------------------------
		
		boolean multithread = true;
		
		// -----------------------------

		if (multithread) {

			int numberofThread = 4;
			ExecutorService service = Executors.newFixedThreadPool(numberofThread);

			service.execute(new Reader(date, readerQueue, path));
			//for (int i = 0; i < 1; i++) {
				service.execute(new Process(readerQueue, writterQueue, date, map_, chaine, chainCountyMap,
						chainIndexLinkedList, chainScoreMap_));
			//}
			service.execute(new Writer(writterQueue, path));
			shutdownAndAwaitTermination(service);

			long estimatedTime = System.nanoTime() - startTime;

			System.out.println(" Temps d'execution : " + estimatedTime / 1e9);
		} else {
			Reader monR = new Reader(date, readerQueue, path);
			Process monP = new Process(readerQueue, writterQueue, date, map_, chaine, chainCountyMap,
					chainIndexLinkedList, chainScoreMap_);
			

			// Initialization
			
			monR.Init_Mono();
			
			File[] csv;
			p = new File(path);
			csv = p.listFiles();
			int[] i_line = new int[csv.length];
			String[] csv_line = new String[csv.length];
			int line_a_modif = 0;
			java.util.Arrays.fill(i_line, 0);
			boolean flag = true;
			boolean poison = true;
			boolean first = false;
			

				for (int j = 0; j < csv.length; j++) {

					csv_line[j] = monR.Read(j, i_line);

				}

				// Loop

				while (flag) {

					// System.out.println(csv[i]);
					if (first) {
						csv_line[line_a_modif] = monR.Read(line_a_modif, i_line);
					} else {
						first = true;
					}

					line_a_modif = monR.Compare(csv_line, i_line);
					// System.out.println(line_a_modif);

					if (line_a_modif == -1) {
						for (int k = 0; k < i_line.length; k++) {
							if (i_line[k] != -1) {
								poison = false;
							}
						}

						if (poison == true) {
							// envoie du colis piègé
							flag = false;

						}
						poison = true;
					} else {
						monP.updateChaine(monR.Cut(csv_line[line_a_modif], line_a_modif));
						i_line[line_a_modif]++;
					}

					

				}

			
			
			
			// monP.updateChaine(data);
			FileWriter write = new FileWriter("Score_pays.csv");
			monP.findScoreOfAllChains();
			for (int i = 0; i < csv.length; i++) {		
			
				String line;
				int maxIndex = monP.getIndexOfLargest(monP.getChainScore());
				long[] datatoQueue = monP.DataToQueue(maxIndex);
				String [] split = csv[(int) datatoQueue[0]].getName().split("\\.");
				//System.out.println(split[0]);
				line = split[0] + ", "+datatoQueue[1]+ ", "+ datatoQueue[2]+"\n";
				//System.out.println(line);
				write.append(line);
				
				monP.getChainScore()[maxIndex] = -1; // enlever le score le plus élevé

			}
			write.close();
			long estimatedTime = System.nanoTime() - startTime;
			System.out.println(" Temps d'execution : " + estimatedTime / 1e9);
		}
	}

	static void shutdownAndAwaitTermination(ExecutorService pool) { // TD multithread
		pool.shutdown(); // Disable new tasks from being submitted
		try {
			// Wait a while for existing tasks to terminate
			if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
				pool.shutdownNow(); // Cancel currently executing tasks
				// Wait a while for tasks to respond to being cancelled
				if (!pool.awaitTermination(60, TimeUnit.SECONDS))
					System.err.println("Pool did not terminate");
			}
		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
			// Preserve interrupt status
			Thread.currentThread().interrupt();
		}
	}
}
