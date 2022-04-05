package com.projectkorra.core.collision;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang.Validate;

import com.projectkorra.core.util.CollisionUtil;
import com.projectkorra.core.util.CollisionUtil.CollisionParseException;

public class CollisionFile {

	private File file;
	
	public CollisionFile(File file) {
		Validate.isTrue(file.getName().endsWith(".txt"), "A collision file must have a txt extension!");
		this.file = file;
		this.init();
	}
	
	private void init() {
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Reads through the file for any valid collisions
	 * @return Set of the valid CollisionData parsed
	 */
	public Set<CollisionData> read() {
		Set<CollisionData> datas = new HashSet<>();
		
		try {
			Scanner reader = new Scanner(file);
			while (reader.hasNextLine()) {
				CollisionData data;
				try {
					data = CollisionUtil.parse(reader.nextLine());
				} catch (CollisionParseException e) {
					e.printStackTrace();
					continue;
				}
				datas.add(data);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return datas;
	}
	
	/**
	 * Reads through the file for any valid collisions and
	 * passes them to the given Consumer
	 * @param consumer what to do with the valid parsed CollisionData
	 */
	public void readAnd(Consumer<CollisionData> consumer) {
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = reader.readLine()) != null) {
				CollisionData data;
				try {
					data = CollisionUtil.parse(line);
				} catch (CollisionParseException e) {
					e.printStackTrace();
					continue;
				}
				consumer.accept(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes information about a collision to the file
	 * @param data collision to write
	 * @return false on IO exception, true otherwise	
	 */
	public boolean write(CollisionData data) {
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(CollisionUtil.stringify(data));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
