package org.database.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.exercise.business.TableEntry;

public class FlatFileReaderImpl {
	//Parse the input file
	public List<TableEntry> parseInputFile(List<TableEntry> entryList, File file) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		BufferedReader br = null;
		String fileIdSubString = "";
		
		try {
			br = new BufferedReader(new FileReader(file));
			String currentLine = br.readLine();
			if(currentLine.length() > 3) {
				fileIdSubString = currentLine.substring(currentLine.length() - 3);
			} else {
				throw new ParseException("File Id Missing", 0);
			}
			while((currentLine = br.readLine()) != null) {
				TableEntry entry = new TableEntry();
				//Set the fileId
				Long fileId = Long.parseLong(fileIdSubString);
				entry.setFileId(fileId);
				
				//Split the current line to get the different values for the table entry
				String[] values = currentLine.split("\t", -1);
				
				//Parse file until IMATRL line in file
				if(values.length != 1) {
					//Set the Load Id
					if(values[0] != null) {
						entry.setLoadId(Long.parseLong(values[0]));
					} else {
						entry.setLoadId(0);
					}
					//Get the date value from the file
					String dateString = values[1] + "-" + values[2] + "-" + values[3];
					Date asOfDate = new Date(df.parse(dateString).getTime());
					entry.setAsOfDate(asOfDate);
					if(values[4] != null) {
						entry.setFundId(values[4]);
					} else {
						entry.setFundId(null);
					}
					if(!values[5].isEmpty()) {
						entry.setMdmRegId(Long.parseLong(values[5]));
					} else {
						entry.setMdmRegId(0);
					}
					if(!values[6].isEmpty()) {
						entry.setUpdateId(values[6]);
					} else {
						entry.setUpdateId(null);
					}
					if(!values[7].isEmpty()) {
						entry.setNavValNumber(Double.parseDouble(values[7]));
					} else {
						entry.setNavValNumber(0);
					}
					entryList.add(entry);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return entryList;
	}

}
