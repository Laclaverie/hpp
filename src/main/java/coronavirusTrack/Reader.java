package coronavirusTrack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

public class Reader {

	private ArrayBlockingQueue<long[]> readerqueue_;
	private String directory_;
	private Date fin_date_;

	// Constructor
	
	public Reader(Date date, ArrayBlockingQueue<long[]> readerqueue, String directory ){
		
		setReaderqueue_(readerqueue);
		setDirectory_(directory);
		setFin_date_(date);
		
	}
	
	// Functions
	
	
	
	public void Search() throws ParseException, IOException {
		// ----
		//String path = p.getAbsolutePath()+"/csv/20";
		// ----
		
		// Initialization
		File p = new File(".");
		File[] csv;
		p = new File(directory_);
		csv = p.listFiles();
		int [] i_line = new int[csv.length];
		String [] csv_line = new String[csv.length];
		int line_a_modif = 0;
		java.util.Arrays.fill(i_line, 0);
		int [] end_line = new int[csv.length];
		for (int i=0; i < csv.length;i++)
		{
			end_line[i] = (int) Files.lines(csv[i].toPath()).count();
		}
		
		
		
		// Loop
		
		
		
		for (int i=0; i < csv.length;i++)
		{
			//System.out.println(csv[i]);
			for (int j=0; j < csv.length;j++) {
				if (end_line[j] == i_line[j])
				{
					i_line[j] = -1;
				}
			}
			
			for (int j=0; j < csv.length;j++) {
				if (i_line[j] == -1)
				{
					csv_line[j] = "none";
				}
				else
				{
					csv_line[j] = Read(i_line[j], csv[j].toString());
				}	
			}
			
			line_a_modif = Compare(csv_line, end_line, i_line);
			if (line_a_modif == -1)
			{
				//envoie du colis piègé 
			}
			else
			{
				Cut(csv_line[line_a_modif]);
			}
			
			
			
		}
	}
	
	
	public String Read(int n, String path )
	{
		 // The line number
	      String line = null;
	      try (Stream<String> lines = Files.lines(Paths.get(path))) {
	          line = lines.skip(n).findFirst().get();
	        }
	      catch(IOException e){
	        System.out.println(e);
	      }
		return line;
	}
	
	@SuppressWarnings("null")
	public int Compare(String t_line[], int end_line[], int i_line[]) throws ParseException
	{
		int modif = -1;
		Date date[] = null;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date mem_date = null;
		
		for (int i=0; i < t_line.length; i++)
		{
			if (i_line[i] != -1)
			{
				String[] split = t_line[i].split(",");
				date[i] = format.parse(split[3]);
				if (fin_date_.compareTo(date[i]) < 0)
				{
					if (mem_date == null)
					{
						mem_date = date[i];
						modif = i;
					}
					else
					{
						if (mem_date.compareTo(date[i]) < 0)
						{
							mem_date = date[i];
							modif = i;
						}
					}
				}
				else
				{
					i_line[i] = -1;
				}
			}
		}
		
		
		return modif;
	}
	
	public void Cut(String line) {
		

		String[] split = line.split(", ");
		
		String cut_line = split[0]+","+split[3]+","+split[4];
		
		 
	}
	
	public void PutIntoQ(long chaine[]) 
	{
		
	}
	
	
	
	
	
	
	
	// Get & Set
	
	public ArrayBlockingQueue<long[]> getReaderqueue_() {
		return readerqueue_;
	}

	public void setReaderqueue_(ArrayBlockingQueue<long[]> readerqueue) {
		this.readerqueue_ = readerqueue;
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



	public void setFin_date_(Date calendar) {
		this.fin_date_ = calendar;
	}
	
}

