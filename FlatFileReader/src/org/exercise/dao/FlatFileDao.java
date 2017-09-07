package org.exercise.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.exercise.business.FlatFileSPResponse;
import org.exercise.business.TableEntry;

import oracle.jdbc.OracleTypes;

public class FlatFileDao {
	private static final String insertEntrySP = "call insert_entry(?, ?, ?, ?, ?, ?, ?)";
	private static final String validateEntriesSP = "call validate_entries(?, ?)";
	private static final String getvalidEntriesSP = "call get_valid_entries(?)";
	private static final String url = "jdbc:oracle:thin:@localhost:1521/ORCLPDB";
	private static final String user = "HR";
	private static final String password = "hr";
	
	private static Connection conn = null;
	private static CallableStatement cstmt = null;
	
	public void insertEntry(List<TableEntry> entryList) {
		getConnection();
		
		try {
			cstmt = conn.prepareCall(insertEntrySP);
			for(TableEntry entry : entryList) {
				cstmt.setLong(1, entry.getLoadId());
				cstmt.setLong(2, entry.getFileId());
				if(StringUtils.isBlank(entry.getFundId())) {
					cstmt.setString(3, null);
				} else {
					cstmt.setString(3, entry.getFundId());
				}
				cstmt.setDate(4, entry.getAsOfDate());
				cstmt.setDouble(5, entry.getNavValNumber());
				cstmt.setLong(6, entry.getMdmRegId());
				if(StringUtils.isBlank(entry.getUpdateId())) {
					cstmt.setString(7, null);
				} else {
					cstmt.setString(7, entry.getUpdateId());
				}
				cstmt.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public FlatFileSPResponse validateEntries(Long fileId) {
		getConnection();
		FlatFileSPResponse spResponse = new FlatFileSPResponse();
		
		try {
			cstmt = conn.prepareCall(validateEntriesSP);
			cstmt.setLong(1, fileId);
			cstmt.registerOutParameter(2, Types.VARCHAR);
			cstmt.registerOutParameter(3, Types.VARCHAR);
			cstmt.execute();
			spResponse.setSpNullStatusMessage(cstmt.getString(2));
			spResponse.setSpVarianceStatusMessage(cstmt.getString(3));		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return spResponse;
	}
	
	public List<TableEntry> getValidEntries() {
		getConnection();
		
		try {
			cstmt = conn.prepareCall(getvalidEntriesSP);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet) cstmt.getObject(1);
			System.out.println("Result Set");
			while(rs.next()) {
				long loadId = rs.getLong("LOAD_ID");
				long fileId = rs.getLong("FILE_ID");
				String fundId = rs.getString("FUND_ID");
				Date asOfDate = rs.getDate("AS_OF_DATE");
				double navValNumber = rs.getDouble("NAV_VAL");
				long mdmRegId = rs.getLong("MDM_REG_ID");
				String updateId = rs.getString("UPDATE_ID");
				System.out.println(loadId);
				System.out.println(fileId);
				System.out.println(fundId);
				System.out.println(asOfDate);
				System.out.println(navValNumber);
				System.out.println(mdmRegId);
				System.out.println(updateId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
