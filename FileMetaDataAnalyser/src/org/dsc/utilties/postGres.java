package org.dsc.utilties;

import java.sql.*;


public class postGres {
    private String url = "jdbc:postgresql://127.0.0.1:5432/MetaData";
    public void setUrl(String host, String port ) {
		this.url = "jdbc:postgresql://"+host+":5432/MetaData";
	}
	public void setUser(String user) {
		this.user = user;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setCon() throws SQLException {
			//System.out.println(url);
			this.con = DriverManager.getConnection(url, user, password);

	}
	private String user = "postgres";
    private String password = "1";
    private Connection con;


    private static final String SQL_INSERT = "INSERT INTO \"MetaData\".\"MetaData\"(\"FileName\", \"MetaDataKey\", \"Value\")VALUES (?, ?, ?)";
    public void runStatement (String file, String metaDataKey, String value) throws ClassNotFoundException, SQLException {
        PreparedStatement st = con.prepareStatement(SQL_INSERT);
        st.setString(1, file);
        st.setString(2, metaDataKey);
        st.setString(3, value.replaceAll("\u0000", ""));
            Class.forName("org.postgresql.Driver");
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            st.executeUpdate();
            stmt.close();
    }
    public void pgCloseConection() throws SQLException {
        con.close();
    }
    public void commit() throws SQLException {
        con.commit();
    }
    
}

