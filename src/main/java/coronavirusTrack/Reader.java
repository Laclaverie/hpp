package coronavirusTrack;

import java.nio.file.Path;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class Reader {

	private ArrayBlockingQueue<String> readerqueue_;
	private Path directory_;
	private Date fin_date_;

	// Constructor
	
	Reader(Date date, ArrayBlockingQueue<String> readerqueue, Path directory ){
		
		setReaderqueue_(readerqueue);
		setDirectory_(directory);
		setFin_date_(date);
		
	}
	
	
	
	
	
	
	
	// Get & Set
	
	public ArrayBlockingQueue<String> getReaderqueue_() {
		return readerqueue_;
	}

	public void setReaderqueue_(ArrayBlockingQueue<String> readerqueue_) {
		this.readerqueue_ = readerqueue_;
	}

	public Path getDirectory_() {
		return directory_;
	}

	public void setDirectory_(Path directory_) {
		this.directory_ = directory_;
	}



	public Date getFin_date_() {
		return fin_date_;
	}



	public void setFin_date_(Date fin_date_) {
		this.fin_date_ = fin_date_;
	}
	
}

