package coronavirusTrack;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Calcul du temps :

		long startTime = System.nanoTime();
		ArrayBlockingQueue<long[]> readerQueue = new ArrayBlockingQueue<long[]>(1000);
		ArrayBlockingQueue<long[]> writterQueue = new ArrayBlockingQueue<long[]>(3);
		HashMap<Long, Integer> map_ = new HashMap<Long, Integer>();
		LinkedList<LinkedList<Malade>> chaine = new LinkedList<>();
		HashMap<Integer, Long> chainCountyMap = new HashMap<Integer, Long>();
		HashMap<Integer, LinkedList<Malade>> chainIndexLinkedList = new HashMap<Integer, LinkedList<Malade>>();
		HashMap<Integer, Long> chainScoreMap_ = new HashMap<Integer, Long>();

		long date = 1587905290;
		File p = new File(".");
		String path = p.getAbsolutePath() + "/csv/1000000";
		System.out.println("début ");

		boolean multithread = true;

		if (multithread) {

			int numberofThread = 4;
			ExecutorService service = Executors.newFixedThreadPool(numberofThread);

			service.execute(new Reader(date, readerQueue, path));
			for (int i = 0; i < 1; i++) {
				service.execute(new Process(readerQueue, writterQueue, date, map_, chaine, chainCountyMap,
						chainIndexLinkedList, chainScoreMap_));
			}
			service.execute(new Writer(writterQueue, path));
			shutdownAndAwaitTermination(service);

			long estimatedTime = System.nanoTime() - startTime;

			System.out.println(" Temps d'execution : " + estimatedTime / 1e9);
		} else {
			Reader monR = new Reader(date, readerQueue, path);
			Process monP = new Process(readerQueue, writterQueue, date, map_, chaine, chainCountyMap,
					chainIndexLinkedList, chainScoreMap_);
			Writer monW = new Writer(writterQueue, path);

			// monP.updateChaine(data);

			// monP.findScoreOfAllChains();
			for (int i = 0; i < 3; i++) {
				int maxIndex = monP.getIndexOfLargest(monP.getChainScore());
				long[] datatoQueue = monP.DataToQueue(maxIndex);
				monP.getChainScore()[maxIndex] = -1; // enlever le score le plus élevé

			}

		}
	}

	static void shutdownAndAwaitTermination(ExecutorService pool) {
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
