package org.database.flatfilereader;

import org.exercise.dao.FlatFileDao;

public class DatabaseResponse {

	public static void main(String[] args) {
		FlatFileDao dao = new FlatFileDao();
		dao.getValidEntries();
	}
}
