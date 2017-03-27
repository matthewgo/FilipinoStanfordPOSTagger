package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

	public void appendToFile(String line) throws IOException {
		fileWriter = new FileWriter(filename, true);
		bufferWriter = new BufferedWriter(fileWriter);
		bufferWriter.write(line + "\n");
	}

	public void writeToFile(String line) throws IOException {
		bufferWriter.write(line + "\n");
	}

	public void close() throws IOException {
		bufferWriter.close();
	}

	public static List<String> readFileISO88591(File f) throws IOException {
		List<String> lines = new ArrayList<>();
		InputStream fr = new FileInputStream(f);
		byte[] buffer = new byte[4096];
		while (true) {
			int byteCount = fr.read(buffer, 0, buffer.length);
			if (byteCount <= 0) {
				break;
			}

			String s = new String(buffer, 0, byteCount, "ISO-8859-1");
			System.out.println(s);
			lines.add(s);
		}
		return lines;
	}

	public static List<String> readFile(File f) throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		}

		return lines;
	}

	public static List<File> getFiles(File rootFolder, String filetype) {
		List<File> files = new ArrayList<>();
		for (File file : rootFolder.listFiles()) {
			if (file.getPath().substring(file.getPath().length() - 3).toLowerCase().equals(filetype))
				files.add(file);
			else if (file.isDirectory()) {
				files.addAll(getFiles(file, filetype));
			}
		}
		return files;
	}
}