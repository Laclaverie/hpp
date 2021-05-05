package coronavirusTrack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class Reader {

	private ArrayBlockingQueue<String> readerqueue_;
	private String directory_;
	private Date fin_date_;

	// Constructor
	
	Reader(Date date, ArrayBlockingQueue<String> readerqueue, String directory ){
		
		setReaderqueue_(readerqueue);
		setDirectory_(directory);
		setFin_date_(date);
		
	}
	
	// Functions
	
	public String Cut(String line) {
		
		String cut_line = null;
		
		
		
		return cut_line; 
	}
	
	public void Search() {
		// ----
		//String path = p.getAbsolutePath()+"/csv/20";
		// ----
		
		// Initialization
		File p = new File(".");
		File[] csv;
		p = new File(directory_);
		csv = p.listFiles();
		
		
		// Loop
		
		int [] i_line = new int[csv.length];
		java.util.Arrays.fill(i_line, 0);
		
		for (int i=0; i < csv.length;i++)
		{
			//System.out.println(csv[i]);
			
			
			
		}
	}
	
	public String Read(int n, String path )
	{
		 // The line number
	      String line = null;
	      try (BufferedReader br = new BufferedReader(new FileReader(path))) {
	          for (int i = 0; i < n; i++)
	              br.readLine();
	          line = br.readLine();
	          //System.out.println(line);
	      }
	      catch(IOException e){
	        System.out.println(e);
	      }
		return line;
	}
	
	
	
	
	
	
	// Get & Set
	
	public ArrayBlockingQueue<String> getReaderqueue_() {
		return readerqueue_;
	}

	public void setReaderqueue_(ArrayBlockingQueue<String> readerqueue_) {
		this.readerqueue_ = readerqueue_;
	}

	public String getDirectory_() {
		return directory_;
	}

	public void setDirectory_(String directory) {
		this.directory_ = directory;
	}



	public Date getFin_date_() {
		return fin_date_;
	}



	public void setFin_date_(Date fin_date_) {
		this.fin_date_ = fin_date_;
	}
	
}

