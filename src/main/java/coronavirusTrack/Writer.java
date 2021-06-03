package coronavirusTrack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.ArrayBlockingQueue;

public class Writer implements Runnable {

	
	
	private ArrayBlockingQueue<long[]> writerqueue_;
	private String directory_;

	// Constructor
	
	public Writer(ArrayBlockingQueue<long[]> writerqueue, String directory){
		
		setWriterqueue_(writerqueue);
		setDirectory_(directory);
		
	}
	
	public void WriteIn2File() throws IOException, InterruptedException
	{
		FileWriter write = new FileWriter("Score_pays.csv");
		File p = new File(".");
		File[] csv;
		p = new File(directory_);
		csv = p.listFiles();
		String line;
		long [] data = new long[3];
		

		
		for (int i=0; i < csv.length ;i++)
		{
			data = writerqueue_.take();
			String [] split = csv[(int) data[0]].getName().split("\\.");
			//System.out.println(split[0]);
			line = split[0] + ", "+data[1]+ ", "+ data[2]+"\n";
			//System.out.println(line);
			write.append(line);
		}
		write.close();
	}

	public ArrayBlockingQueue<long[]> getWriterqueue_() {
		return writerqueue_;
	}

	public void setWriterqueue_(ArrayBlockingQueue<long[]> writerqueue_) {
		this.writerqueue_ = writerqueue_;
	}

	public String getDirectory_() {
		return directory_;
	}

	public void setDirectory_(String directory_) {
		this.directory_ = directory_;
	}

	@Override
	public void run() {
		try {
			WriteIn2File();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
