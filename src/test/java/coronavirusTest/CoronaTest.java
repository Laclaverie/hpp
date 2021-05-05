package coronavirusTest;

import java.io.BufferedReader;

//import static org.junit.Assert.*;

import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Test;

public class CoronaTest {

	//@SuppressWarnings("null")
	
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
	
	
	public void test2() throws IOException {
		//Path directory = Paths.get("/csv/20");
		//directory.getPath();
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/20";
		System.out.println(path);
		File[] paths;
		
		p = new File(path);
		paths = p.listFiles();
		String row;
		
		//String line = Files.read
		
		BufferedReader csvReader = new BufferedReader(new FileReader(paths[0]));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    for (int i = 0; i < data.length;i++ )
		    {
		    	System.out.println(data[i]);
		    }
		    
		    // do something with the data
		}
		csvReader.close();
	}
	
	@Test
	public void test3() throws IOException {
		//Path directory = Paths.get("/csv/20");
		//directory.getPath();
		File p = new File(".");
		String path = p.getAbsolutePath()+"/csv/20";
		System.out.println(path);
		File[] paths;
		
		p = new File(path);
		paths = p.listFiles();
		//String row;
		
		//String line = Files.read
		/*
		BufferedReader csvReader = new BufferedReader(new FileReader(paths[0]));
		while ((row = csvReader.readLine()) != null) {
		    String[] data = row.split(",");
		    for (int i = 0; i < data.length;i++ )
		    {
		    	System.out.println(data[i]);
		    }
		    
		    // do something with the data
		}
		csvReader.close();
		*/
		int n = 3; // The line number
	      String line;
	      try (Stream<String> lines = Files.lines(Paths.get(paths[0].toString()))) {
	          line = lines.skip(n).findFirst().get();
	          System.out.println(line);
	        }
	      catch(IOException e){
	        System.out.println(e);
	      }
		
	}
	
	

}
