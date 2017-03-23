package brown_cluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import utils.FileManager;

public class BrownClusterOutputFormatter {

	public void toStanfordFormat(String outputFile, String stanfordOutputFile)
			throws FileNotFoundException, IOException {
		List<String> lines = FileManager.readFile(new File(outputFile));
		FileManager stanfordOutput = new FileManager(stanfordOutputFile);
		stanfordOutput.createFile();
		String prev = "";
		int classNumber = 1;
		for (String line : lines) {
			String[] split = line.split("\\s+");
			if (!split[0].equals(prev) && !prev.equals(""))
				classNumber++;
			prev = split[0];
			stanfordOutput.writeToFile(split[1] + "\t" + classNumber);
		}
	}
}
