package train;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import utils.Constants;
import utils.FileManager;

public class GetListOfTags {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<String> trainTags = FileManager.readFile(new File(Constants.TRAIN_HPOST_TAGS));
		HashSet<String> taglist = new HashSet<>();
		for (int i = 0; i < trainTags.size(); i++) {
			String[] tags = trainTags.get(i).trim().split(" ");
			for (String t : tags) {
				taglist.add(t);
			}
		}
		System.out.println(taglist.size());
		for (String t : taglist) {
			System.out.print(t + " ");
		}
	}
}
