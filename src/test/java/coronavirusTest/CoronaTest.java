package coronavirusTest;

//import static org.junit.Assert.*;

import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;

import org.junit.Test;

public class CoronaTest {

	//@SuppressWarnings("null")
	@Test
	public void test() {
		//Path directory = Paths.get("/csv/20");
		//directory.getPath();
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/20";
		System.out.println(path);
		File[] paths;
		
		p = new File(path);
		paths = p.listFiles();
		for (int i=0; i < paths.length;i++)
		{
			System.out.println(paths[i]);
		}
	}

}
