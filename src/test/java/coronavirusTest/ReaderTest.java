package coronavirusTest;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import coronavirusTrack.Reader;

public class ReaderTest {

	@Test
	public void test() throws ParseException, IOException {
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/1000000";
		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = format.parse("2020-01-30");
		ArrayBlockingQueue<long[]> readerqueue_=new ArrayBlockingQueue<long[]>(1000001); // non utilisé dans un premier temps
		long date = 1589238000;

		Reader monR =new Reader(date,readerqueue_,path);
		
		//String test1 = monR.Read(3, path);
		//System.out.println(test1);
		//String test2 = monR.Cut(test1);
		//System.out.println(test2);
		//assertEquals("13,1976-03-12 00:00:00,1587417223.6139328", test2);
		
		System.out.println("Test Ultime");
		System.out.println("Test en cours ...");
		
		monR.Search();
		
		System.out.println("Test Fini !");
		
	 
	}
}
