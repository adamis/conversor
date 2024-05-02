package br.com.conversor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class Conversor {

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
		Conexao oracle = new Conexao(DatabaseType.ORACLE, "PRD_BRFTS", "BRFTS#75214", "brhtl01p01c1-dbvm01.fiserv.one", "", "FTSPROD.fiserv.one","");
		oracle.conect();

		//oracle.beginTransaction();

		try {

			List<String> readTxtList = Utils.readTxtList("C:\\temp\\UsersAccessCertification.BRFTS.20231020 - Update.csv");
			List<String> readTxtListTMP = Utils.readTxtList("C:\\temp\\TMP.csv");

			StringBuilder sb = new StringBuilder(); 
			sb.append(" SELECT ");
			sb.append("     'APM0002673'                                                                           AS \"apm\", ");
			sb.append("     'BRFTS'                                                                                AS \"asset\", ");
			sb.append("     'BRFTS - Switch for transactions'                                                      AS \"ASSET DETAIL\", ");
			sb.append("     'PROD'                                                                                 AS \"ASSET NOTES\", ");
			sb.append("     TRIM(substr(sv_profile_common_name, ");
			sb.append("                 instr(sv_profile_common_name, ':') + 1, ");
			sb.append("                 instr(sv_profile_common_name, ',') - instr(sv_profile_common_name, ':') - 1))       AS \"First Name\", ");
			sb.append("     TRIM(substr(sv_profile_common_name, ");
			sb.append("                 instr(sv_profile_common_name, ',') + 1, ");
			sb.append("                 instr(sv_profile_common_name, ',', 1, 2) - instr(sv_profile_common_name, ',') - 1)) AS \"Last Name\", ");
			sb.append("     sv_name                                                                                AS \"User Id\", ");
			sb.append("     sv_employee_id                                                                         AS \"Employee ID\", ");
			sb.append("     CASE ");
			sb.append("         WHEN fk_ugp_id = 55 THEN ");
			sb.append("             'admin' ");
			sb.append("         WHEN fk_ugp_id = 5  THEN ");
			sb.append("             'system' ");
			sb.append("         WHEN fk_ugp_id = 53 THEN ");
			sb.append("             'root' ");
			sb.append("         WHEN fk_ugp_id = 57 THEN ");
			sb.append("             'admin.access' ");
			sb.append("         WHEN fk_ugp_id = 58 THEN ");
			sb.append("             'standard' ");
			sb.append("     END                                                                                    AS \"role\", ");
			sb.append("     regexp_substr(sv_profile_email, '[^ ]+$')                                              AS \"Email Address (if known)\" ");
			sb.append(" FROM ");
			sb.append("     tb_usr_users ");
			sb.append(" WHERE ");
			sb.append("     sv_generic_status = 'ACTIVE' " );
			sb.append(" AND  sv_profile_common_name is not null ");

		
			ResultSet resultTratores  = oracle.executeQuery( sb.toString() ); 					

			List<String> listFinal = new ArrayList<String>();
			listFinal.add("apm;asset;ASSET DETAIL;ASSET NOTES;First Name;Last Name;User Id;Employee ID;role;Email Address (if known)");
			
			while (resultTratores.next()) {						
				
				String apm = resultTratores.getString("apm");
				String asset = resultTratores.getString("asset");
				String assetDetail = resultTratores.getString("ASSET DETAIL");
				String assetNotes = resultTratores.getString("ASSET NOTES");
				String fName = resultTratores.getString("First Name");
				String lName = resultTratores.getString("Last Name");
				
				String userId = resultTratores.getString("User Id");
				String employeeId = resultTratores.getString("Employee ID");
				String role = resultTratores.getString("role");				
				String email = resultTratores.getString("Email Address (if known)");
				
				for (int i = 0; i < readTxtList.size(); i++) {
					
					String row = readTxtList.get(i);					
					String[] split = row.split(";");
					
					if((employeeId == null || employeeId.isEmpty()) && split[0].trim().equalsIgnoreCase(userId.trim())) {
						
						System.err.println("userId: "+userId + " employeeId:"+split[1]);
						
						if(split[1] != null) {
							employeeId = split[1];
						}
						break;
					}					
					
				}
				
				for (int i = 0; i < readTxtListTMP.size(); i++) {
					
					String row = readTxtListTMP.get(i);					
					String[] split = row.split(";");
					
					if((employeeId == null || employeeId.isEmpty()) && split[0].trim().equalsIgnoreCase(userId.trim())) {
						
						System.err.println("userId: "+userId + " employeeId:"+split[1]);
						
						if(split[1] != null) {
							employeeId = split[1];
						}
						break;
					}					
					
				}
				
				if(employeeId == null || employeeId.equals("null")) {
					employeeId = "";
				}
				
				listFinal.add(apm+";"+asset+";"+assetDetail+";"+assetNotes+";"+fName+";"+lName+";"+userId+";"+employeeId+";"+role+";"+email);

			}
			
			try {
				Utils.writeTxtList("C:\\temp\\BRFTS-Final.csv", listFinal, false);
			} catch (Exception e) {
				e.printStackTrace();
			}


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
