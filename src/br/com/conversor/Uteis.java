package br.com.conversor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Uteis {

	private static Integer SIZE_PAGINA = 1000;
	private static Integer SIZE_PAGINA_COMMIT = 100;

	/**
	 * @param conexaoFind = connection find   all data
	 * @param conexaoFrom = connection record all data
	 * @param tableFind   = table find   all data
	 * @param tableFrom   = table record all data
	 * @return true       = success or false = fail
	 * @throws SQLException 
	 */
	public void duplicaDados(Conexao conexaoFind,Conexao conexaoFrom,String tableFind, String tableFrom) throws Exception{

		conexaoFind.conect();
		conexaoFrom.conect();

		Integer totalRegistro = conexaoFind.countAll(tableFind);
		Integer paginas = (totalRegistro/SIZE_PAGINA) + ( (totalRegistro%SIZE_PAGINA) > 0 ? 1 : 0);

		for (int i = 0; i < paginas; i++) {
			String sql = "select * from "+tableFind;
			sql = montaPaginacao(conexaoFind, sql, i);
			ResultSet resultBanco = conexaoFind.executeQuery(sql);

			int montaSQLInsert = montaSQLInsertAndExecute(resultBanco, tableFrom, conexaoFrom);
			if(montaSQLInsert >= 999) {
				continue;
			}else {
				throw new Exception("Falha ao copiar de uma tabela para outra!");					
			}
		}

		conexaoFrom.disconect();

	}

	/**
	 * Monta Paginação no SQL
	 * @param conexaoFind
	 * @param sql
	 * @param pagina
	 * @return
	 */
	private String montaPaginacao(Conexao conexaoFind, String sql, Integer pagina) {

		if(conexaoFind.getDatabaseType() != null) {

			if( (conexaoFind.getDatabaseType() == DatabaseType.MYSQL) || (conexaoFind.getDatabaseType() == DatabaseType.POSTGRESS) ) {

				sql = sql+" LIMIT "+SIZE_PAGINA+" OFFSET "+(pagina == 0 ? 0 : pagina*SIZE_PAGINA);

				//			LIMIT
				//			  10 -- Only return 10 rows
				//			OFFSET
				//			  10 -- Skip the first 10 row
			}else if(conexaoFind.getDatabaseType() == DatabaseType.ORACLE) {

				sql = "SELECT * FROM ( " +
						"SELECT a.*, ROWNUM rnum FROM ( " +
						sql +
						" ) a WHERE ROWNUM <= " + (pagina == 0 ? SIZE_PAGINA : (pagina*SIZE_PAGINA)+SIZE_PAGINA) +
						" ) WHERE rnum >= "+ (pagina == 0 ? 0 : pagina*SIZE_PAGINA)
						;
			}

		} else {
			if(conexaoFind.getDatabaseTypeFile() == DatabaseTypeFile.SQLITE) {
				sql = sql + " LIMIT "+pagina*SIZE_PAGINA+", "+SIZE_PAGINA;
			}
		}
		return sql;
	}

	private int montaSQLInsertAndExecute(ResultSet resultBanco, String tableFrom, Conexao conexaoFrom ) throws SQLException {

		StringBuilder stringBuilder = new StringBuilder();

		ResultSetMetaData rsmd = resultBanco.getMetaData();  

		// retorna o numero total de colunas  
		int numColumns = rsmd.getColumnCount();  

		int cont = 0;
		int contSucess = 0;

		while (resultBanco.next()) {

			stringBuilder = new StringBuilder();			
			stringBuilder.append("INSERT INTO "+tableFrom+" VALUES(");
			for (int i = 0; i < numColumns; i++) {

				if(i == 0){
					if(	       rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("INTEGER")
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("INT") 
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DOUBLE") 
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DOUBLE PRECISION")
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("SMALLINT")
							){
						stringBuilder.append(resultBanco.getString(rsmd.getColumnName(i + 1)));
						//despId = resultBanco1.getString(rsmd.getColumnName(i + 1));
					}else if( rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("VARCHAR")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("TEXT")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("CHAR")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("BLOB")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("BLOB SUB_TYPE 1")
							){
						stringBuilder.append(Conexao.checkString(resultBanco.getString(rsmd.getColumnName(i + 1))));

					}else if(rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DATE")){
						stringBuilder.append(Conexao.checkDate(resultBanco.getDate(rsmd.getColumnName(i + 1))));

					}else if((rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("TIMESTAMP"))||(rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DATETIME"))){
						stringBuilder.append(Conexao.checkDate(resultBanco.getTimestamp(rsmd.getColumnName(i + 1))));
					}

				}else{

					if( 	   rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("INTEGER")
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("INT") 
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DOUBLE")
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DOUBLE PRECISION")
							|| rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("SMALLINT")
							){
						stringBuilder.append(","+resultBanco.getString(rsmd.getColumnName(i + 1)));

					}else if( rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("VARCHAR")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("TEXT")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("CHAR")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("BLOB")
							||rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("BLOB SUB_TYPE 1")
							){

						String temp = resultBanco.getString(rsmd.getColumnName(i + 1)) == null?"null":resultBanco.getString(rsmd.getColumnName(i + 1));
						stringBuilder.append(","+Conexao.checkString(temp));

					}else if(rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DATE")){
						stringBuilder.append(","+Conexao.checkDate(resultBanco.getDate(rsmd.getColumnName(i + 1))));

					}else if((rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("TIMESTAMP"))
							||(rsmd.getColumnTypeName (i + 1).equalsIgnoreCase("DATETIME"))){
						stringBuilder.append(","+Conexao.checkDate(resultBanco.getTimestamp(rsmd.getColumnName(i + 1))));
					}
				}
			}

			stringBuilder.append(")");	

			String executeQueryUpdate = conexaoFrom.executeQueryUpdate(stringBuilder.toString());
			System.err.println("executeQueryUpdate>> "+executeQueryUpdate);

			if(executeQueryUpdate.equals("OK")) {
				contSucess++;
			}

			cont++;

			if(cont >= SIZE_PAGINA_COMMIT) {
				conexaoFrom.commit();
				conexaoFrom.disconect();
				conexaoFrom.conect();
				cont = 0;
			}


		}

		conexaoFrom.commit();
		conexaoFrom.disconect();
		conexaoFrom.conect();

		return contSucess;
	}
}
