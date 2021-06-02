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
		int numberofThread = 4;
		
		ArrayBlockingQueue<long[]> readerQueue=new ArrayBlockingQueue<long[]>(1000);
		ArrayBlockingQueue<long[]> writterQueue=new ArrayBlockingQueue<long[]>(3);
		HashMap<Long, Integer> map_=new HashMap<Long,Integer>();
		LinkedList<LinkedList<Malade>> chaine = new LinkedList<>() ;
		HashMap<Integer,Long> chainCountyMap=new HashMap<Integer,Long>();
		HashMap<Integer, Integer> chainIdPtr=new HashMap<Integer,Integer>();
		HashMap<Integer, LinkedList<Malade>> chainIndexLinkedList=new HashMap<Integer, LinkedList<Malade>>();
		
		long date = 1589238000;
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/1000000";
		System.out.println("d�but ");
		ExecutorService service = Executors.newFixedThreadPool(numberofThread);
		
		service.execute(new Reader(date, readerQueue, path));
		for(int i=0;i<1;i++) {
			service.execute(new Process(readerQueue, writterQueue, date,map_,chaine,chainCountyMap,chainIdPtr,chainIndexLinkedList));
		}

		
		shutdownAndAwaitTermination(service);
		System.out.println(" fin ");
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
