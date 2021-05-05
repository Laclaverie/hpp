package coronavirusTest;

import static org.junit.Assert.*;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;
import coronavirusTrack.Process;

public class ProcessTest {

	@Test
	public void test() {
		 ArrayBlockingQueue<String> readerqueue_; // non utilisé dans un premier temps
		 ArrayBlockingQueue<String> writterqueue_; // non utilisé dans un premier temps

		Process monP =new Process(readerqueue_,writterqueue_);
		String s1 = "10,1552244,2";
	 
	}

}
