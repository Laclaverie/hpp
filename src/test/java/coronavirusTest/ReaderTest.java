package coronavirusTest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

import coronavirusTrack.Reader;

public class ReaderTest {

	@Test
	public void test() throws ParseException {
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/20/France.csv";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse("2020-01-30 00:00:00");
		ArrayBlockingQueue<long[]> readerqueue_=new ArrayBlockingQueue<long[]>(20); // non utilisé dans un premier temps

		Reader monR =new Reader(date,readerqueue_,path);
		
		String test1 = monR.Read(3, path);
		System.out.println(test1);
		String test2 = monR.Cut(test1);
		System.out.println(test2);
		//assertEquals("13,1976-03-12 00:00:00,1587417223.6139328", test2);
		
		
	 
	}
}
