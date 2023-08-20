package br.com.conversor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Conversor {

	public static void main(String[] args) throws SQLException {

				//FIREBIRD
		//		Conexao firebird = new Conexao("firebird", "sysdba", "masterkey", " C:/BASE.GDB " , "localhost", "NONE");
		//		firebird.conect();

				//SYBASE
		//		Conexao apolo = new Conexao("sybase", "internet", "codiubdsv", "172.17.0.112", "5000", "internet", "cp850");
		//		apolo.conect();

				//ORACLE
				Conexao oracle = new Conexao(DatabaseType.ORACLE, "agricultur"/*a"*/, "agric2016", "172.17.0.113", "", "pmudb1","");
				oracle.conect();		

				//SYBASE
		//		Conexao pmu   = new Conexao("sybase", "internet", "codiubdsv", "172.17.0.110", "5505", "controladoria", "cp850");
		//		pmu.conect();

			   //MYSQL
//				Conexao mysql = new Conexao("mysql", "root", "vertrigo", "localhost", "3306", "gcm_demo", "");
//				mysql.conect();

				oracle.beginTransaction();
				

				
				

					ResultSet resultTratores  = oracle.executeQuery(  "SELECT "
																	+ " ID, "							
																	+ " NOME, "
																	+ " DESCRICAO "
																	+ " FROM TRATORES "
																  ); 					
					
					while (resultTratores.next()) {						
							
							String id = resultTratores.getString("ID");
							String nome = resultTratores.getString("NOME");
							String descricao = resultTratores.getString("DESCRICAO");
							
							String str = "UPDATE TRATORES "
							+ " SET NOME   ='"+nome.toUpperCase()+"'"
							+ " ,DESCRICAO   ='"+descricao.toUpperCase()+"'"
							+ " WHERE ID="+id
							;
							
							System.err.println(oracle.executeQueryUpdate(str));
							System.err.println(""+str);
							
					}				
		
		oracle.commit();
		oracle.disconect();
		
		System.out.println("FIM !");

	}

	private static String getControl(String date){
		
		String[] dataHora = date.split(" ");
		String[] data = dataHora[0].split("-");
		
		return data[2]+"-"+data[1]+"-"+data[0];
		
	}
	

	//Uteis util = new Uteis();
	//util.duplicaDados(mysql,pmu,"empresas", "dbo.ml_empresas");
	//util.duplicaDados(mysql,pmu,"cargos", "dbo.ml_cargos");
}
