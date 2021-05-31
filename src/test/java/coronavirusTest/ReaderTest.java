package coronavirusTest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
//import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import coronavirusTrack.Reader;

public class ReaderTest {

	@Test
	public void test() throws ParseException, IOException {
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/20";
		//DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		//Date date = format.parse("2020-01-30");
		ArrayBlockingQueue<long[]> readerqueue_=new ArrayBlockingQueue<long[]>(20); // non utilisé dans un premier temps
		int date = 1587769422;

		Reader monR =new Reader(date,readerqueue_,path);
		
		//String test1 = monR.Read(3, path);
		//System.out.println(test1);
		//String test2 = monR.Cut(test1);
		//System.out.println(test2);
		//assertEquals("13,1976-03-12 00:00:00,1587417223.6139328", test2);
		
		System.out.println("Test Ultime");
		
		monR.Search();
		
		
		
	 
	}
}
