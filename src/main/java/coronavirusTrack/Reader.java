package coronavirusTrack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class Reader implements Runnable {

	private ArrayBlockingQueue<long[]> readerqueue_;
	private String directory_;
	private long fin_date_;
	private Scanner[] scan_tab_;
	

	// Constructor

	public Reader(long date, ArrayBlockingQueue<long[]> readerqueue, String directory) {

		setReaderqueue_(readerqueue);
		setDirectory_(directory);
		setFin_date_(date);

	}

	// Functions

	@Override
	public void run() {
		try {
			Search();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void Search() throws ParseException, IOException {
		// ----
		// String path = p.getAbsolutePath()+"/csv/20";
		// ----

		// Initialization
		File p = new File(".");
		File[] csv;
		p = new File(directory_);
		csv = p.listFiles();
		// scan_tab_ = new BufferedReader[csv.length];
		scan_tab_ = new Scanner[csv.length];
		int[] i_line = new int[csv.length];
		String[] csv_line = new String[csv.length];
		int line_a_modif = 0;
		java.util.Arrays.fill(i_line, 0);
		int[] end_line = new int[csv.length];
		for (int i = 0; i < csv.length; i++) {
			end_line[i] = (int) Files.lines(csv[i].toPath()).count();

			// scan_tab_[i] = new BufferedReader(new FileReader(csv[i]));
			scan_tab_[i] = new Scanner(csv[i]);
			// scan_tab_[i].useDelimiter(",");

		}

		boolean flag = true;
		boolean poison = true;
		boolean first = false;

		for (int j = 0; j < csv.length; j++) {

			csv_line[j] = Read(j, i_line);

		}

		// Loop

		while (flag) {
			// for (int i=0; i < csv.length;i++)
			// {
			// System.out.println(csv[i]);
			if (first) {
				csv_line[line_a_modif] = Read(line_a_modif, i_line);
			} else {
				first = true;
			}

			line_a_modif = Compare(csv_line, i_line);
			// System.out.println(line_a_modif);

			if (line_a_modif == -1) {
				for (int k = 0; k < i_line.length; k++) {
					if (i_line[k] != -1) {
						poison = false;
					}
				}

				if (poison == true) {
					// envoie du colis piègé
					flag = false;
					long[] poisonPILL = { -1, -1, -1 };
					PutIntoQ(poisonPILL);
				}
				poison = true;
			} else {
				Cut(csv_line[line_a_modif], line_a_modif);
				i_line[line_a_modif]++;
			}

			// }

		}
	}

	public String Read(int n, int[] i_line) throws IOException {
		// The line number
		String line = null;

		if (n != -1) {
			if (scan_tab_[n].hasNextLine()) {
				line = scan_tab_[n].nextLine();

				i_line[n] += 1;
			} else {
				i_line[n] = -1;
				// System.out.println("Fichier n:" +n+ "est fini !");

			}
		} else {
			line = "none";
		}

		return line;
	}

	public int Compare(String t_line[], int i_line[]) throws ParseException {
		int modif = -1;
		long date[] = new long[t_line.length];
		long mem_date = 0;

		for (int i = 0; i < t_line.length; i++) {
			if (i_line[i] != -1) {

				String[] split = t_line[i].split(", ");
				String[] split_date = split[4].split("\\.");
				date[i] = Long.valueOf(split_date[0]);
				if (fin_date_ >= date[i]) {
					if (mem_date == 0) {
						mem_date = date[i];
						modif = i;
					} else {
						if (mem_date > date[i]) {
							mem_date = date[i];
							modif = i;
						}
					}
				} else {
					i_line[i] = -1;
				}
			}
		}

		return modif;
	}

	public void Cut(String line, int pays) {

		String[] split = line.split(", ");
		long tmp;

		String[] split_date = split[4].split("\\.");

		long[] chaine = new long[4];
		chaine[0] = Long.valueOf(split[0]);
		chaine[1] = Long.valueOf(split_date[0]);
		if (split[5].equals("unknown")) {
			tmp = -1;
		} else {
			tmp = Long.valueOf(split[5]);
		}
		chaine[2] = tmp;
		chaine[3] = Long.valueOf(pays);
		// String cut_line = split[0]+","+split_date[0]+","+tmp+","+pays;
		PutIntoQ(chaine);
		// System.out.println(cut_line);

	}

	public void PutIntoQ(long chaine[]) {
		try {
			readerqueue_.put(chaine);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void Init_Mono() throws IOException
	{
		/**
		 * Initialiser les variables pour le cas où nous nous mettons en monothread
		 */
		File p = new File(".");
		File[] csv;
		p = new File(directory_);
		csv = p.listFiles();
		int[] i_line = new int[csv.length];
		java.util.Arrays.fill(i_line, 0);
		int[] end_line = new int[csv.length];
		
		
		
		// scan_tab_ = new BufferedReader[csv.length];
		scan_tab_ = new Scanner[csv.length];
		
		for (int i = 0; i < csv.length; i++) {
			end_line[i] = (int) Files.lines(csv[i].toPath()).count();

			// scan_tab_[i] = new BufferedReader(new FileReader(csv[i]));
			scan_tab_[i] = new Scanner(csv[i]);
			// scan_tab_[i].useDelimiter(",");

		}
	}
	
	public long[] Cut_mono(String line, int pays) {
/**
 * Ressemble en tout point à Cut, sauf qu'un long [] est renvoyé, on ne touche pas à la queue
 */
		String[] split = line.split(", ");
		long tmp;

		String[] split_date = split[4].split("\\.");

		long[] chaine = new long[4];
		chaine[0] = Long.valueOf(split[0]);
		chaine[1] = Long.valueOf(split_date[0]);
		if (split[5].equals("unknown")) {
			tmp = -1;
		} else {
			tmp = Long.valueOf(split[5]);
		}
		chaine[2] = tmp;
		chaine[3] = Long.valueOf(pays);
		// String cut_line = split[0]+","+split_date[0]+","+tmp+","+pays;
		return chaine;
		// System.out.println(cut_line);

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

	public long getFin_date_() {
		return fin_date_;
	}

	public void setFin_date_(long date) {
		this.fin_date_ = date;
	}

}
