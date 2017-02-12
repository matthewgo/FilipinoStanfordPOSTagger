package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	FileWriter fileWriter;
	BufferedWriter bufferWriter;
	String filename;

	public FileManager(String filename) {
		this.filename = filename;
	}

	public void createFile() throws IOException {
		File file = new File(filename);
		if (file.exists())
			file.delete();

		file.createNewFile();
		fileWriter = new FileWriter(file);
		bufferWriter = new BufferedWriter(fileWriter);
	}

	public void writeToFile(String line) throws IOException {
		bufferWriter.write(line + "\n");
	}

	public void close() throws IOException {
		bufferWriter.close();
	}

	public static List<String> readFile(File f) throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}

		return lines;
	}
}