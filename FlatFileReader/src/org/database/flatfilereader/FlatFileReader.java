package org.database.flatfilereader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.database.parser.FlatFileReaderImpl;
import org.exercise.business.TableEntry;
import org.exercise.dao.FlatFileDao;

//Application to read from a file and write to the database
public class FlatFileReader {
	
	public static void main(String[] args) {
		//Sample files
		File inputFile = new File("sample_file");
		//File inputFile = new File("sample_file_alphaNumericNulls");
		//File inputFile = new File("sample_file_numericNulls");
		
		//Create an instance of the FlatFileReaderImpl
		FlatFileReaderImpl readerImpl = new FlatFileReaderImpl();

		//Create return list of entries
		List<TableEntry> entryList = new ArrayList<TableEntry>();
		entryList = readerImpl.parseInputFile(entryList, inputFile);
		
		//Create an instance of the FlatFileDao, and insert/update the entries for the file
		FlatFileDao dao = new FlatFileDao();
		dao.insertEntry(entryList);
	}

}
