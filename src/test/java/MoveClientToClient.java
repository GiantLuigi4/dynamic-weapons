import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MoveClientToClient {
	public static void main(String[] args) {
		File server = new File("src/main/resources/data/dynamic_weaponry/weaponry/materials");
		
		ArrayList<File> files = listAll(server);
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		for (File file : files) {
			String text = read(file);
			JsonObject object = gson.fromJson(text, JsonObject.class);
			
			JsonObject object1 = new JsonObject();
			
			boolean needsClientWrite = false;
			
			if (object.has("color")) {
				object1.add("color", object.remove("color"));
				needsClientWrite = true;
			}
			if (object.has("borderColor")) {
				object1.add("borderColor", object.remove("borderColor"));
				needsClientWrite = true;
			}
			
			object1.add("item", object.get("item"));
			
			File client = new File(file.toString().replace("data", "assets"));
			if (client.exists()) {
				try {
					JsonObject object2 = gson.fromJson(read(client), JsonObject.class);
					for (Map.Entry<String, JsonElement> stringJsonElementEntry : object2.entrySet()) {
						object1.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
					}
				} catch (Throwable err) {
					System.out.println(file.toString());
					System.out.println(client.toString());
					throw new RuntimeException(err);
				}
			}
			
			if (needsClientWrite) write(client, gson.toJson(object1));
			write(file, gson.toJson(object));
		}
	}
	
	public static String read(File file) {
		byte[] bytes = new byte[0];
		try {
			FileInputStream stream = new FileInputStream(file);
			bytes = new byte[stream.available()];
			stream.read(bytes);
			stream.close();
		} catch (Throwable ignored) {
		}
		return new String(bytes);
	}
	
	public static void write(File file, String text) {
		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream stream = new FileOutputStream(file);
			stream.write(text.getBytes());
			stream.close();
		} catch (Throwable ignored) {
		}
	}
	
	public static ArrayList<File> listAll(File file) {
		ArrayList<File> returnVal = new ArrayList<>();
		List<File> iter = Arrays.asList(file.listFiles());
		ArrayList<File> toIter = new ArrayList<>();
		
		while (!iter.isEmpty()) {
			for (File file1 : iter) {
				if (file1.isDirectory()) {
					for (File listFile : file1.listFiles()) {
						if (listFile.isFile()) toIter.add(listFile);
						else returnVal.add(listFile);
					}
				} else if (!returnVal.contains(file1)) returnVal.add(file1);
			}
			
			iter = toIter;
			toIter = new ArrayList<>();
		}
		
		return returnVal;
	}
}
