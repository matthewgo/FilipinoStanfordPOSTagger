package data_cleaner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import utils.FileManager;

public class XMLToTXT {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		File newsFolder = new File("data/News and Opinion Corpus/News");
		File opinyonFolder = new File("data/News and Opinion Corpus/Opinyon");

		List<File> files = FileManager.getFiles(newsFolder, "xml");
		files.addAll(FileManager.getFiles(opinyonFolder, "xml"));

		FileManager sentencesFile = new FileManager("data/News and Opinion Corpus/sentences2.txt");
		sentencesFile.createFile();
		for (File f : files) {
			List<String> lines = FileManager.readFile(f);
			for (String l : lines) {
				if (l.contains("<body>")) {
					l = l.replace("<body>", "").replace("</body>", "").trim();
					l = clean(l);
					sentencesFile.writeToFile(l);
					System.out.println(l);
				}
			}
		}
		sentencesFile.close();
	}

	private static String clean(String l) {
		l = l.replaceAll("&ntilde;", "n");
		l = l.replaceAll("&quot;", "'");
		l = l.replaceAll("&#146;", "'");
		l = l.replaceAll("&amp;", "&");
		l = l.replaceAll("&nbsp;", "");
		l = l.replaceAll("MANILA, Philippines - ", "");
		return l;
	}

}
