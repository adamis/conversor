package br.com.conversor;

import java.sql.ResultSet;
import java.sql.SQLException;



public class UpdateCitys {

	public static void main(String[] args) throws SQLException {

		//FIREBIRD
		//		Conexao firebird = new Conexao("firebird", "sysdba", "masterkey", " C:/BASE.GDB " , "localhost", "NONE");
		//		firebird.conect();

		//SYBASE
		//		Conexao apolo = new Conexao("sybase", "internet", "codiubdsv", "172.17.0.112", "5000", "internet", "cp850");
		//		apolo.conect();

		//SYBASE
		//		Conexao pmu   = new Conexao("sybase", "internet", "codiubdsv", "172.17.0.110", "5505", "controladoria", "cp850");
		//		pmu.conect();

		//MYSQL
		//		Conexao mysql = new Conexao("mysql", "root", "vertrigo", "localhost", "3306", "gcm_demo", "");
		//		mysql.conect();

		//ORACLE
		Conexao oracle = new Conexao(DatabaseType.ORACLE, "uat_hotfix_brtmp", "uat_hotfix_brtmp201805", "brsao02t01c1-dbvm01.fiserv.one", "", "TMPCAT.fiserv.one","");
		oracle.conect();

		oracle.beginTransaction();

		try {
			StringBuilder sb = new StringBuilder(); 
			sb.append(" SELECT ");
			sb.append("     nl_id, ");
			sb.append("     sv_name, ");
			sb.append("     sv_name_normalized, ");
			sb.append("     sv_acronym, ");
			sb.append("     fk_geolocation ");
			sb.append(" FROM ");
			sb.append("     tb_address_states ");

			ResultSet resultStates  = oracle.executeQuery( sb.toString() ); 	

			while (resultStates.next()) {						
				
				String id = resultStates.getString("nl_id");
				
				System.err.println("ID> "+id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
}
