package coronavirusTrack;

import java.util.concurrent.ArrayBlockingQueue;

public class Writer {

	
	
	private ArrayBlockingQueue<long[]> writerqueue_;
	private String directory_;

	// Constructor
	
	public Writer(ArrayBlockingQueue<long[]> writerqueue, String directory){
		
		setWriterqueue_(writerqueue);
		setDirectory_(directory);
		
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
}
