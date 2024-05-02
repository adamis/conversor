package br.com.conversor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.text.MaskFormatter;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mysql.cj.xdevapi.JsonNumber;
import com.mysql.cj.xdevapi.JsonString;



public class Utils {

	public static String getFileName(String endpoint) {
		return String.valueOf(Paths.get(FilenameUtils.normalize(endpoint)).getFileName());
	}

	public static String bytesToHR(long bytes, boolean si) {

		int unit = si ? 1000 : 1024;
		if (bytes < unit)
			return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");

		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}

	public static String longToCurrency(Long value) {

		NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);

		return (nf.format(value / 100.0));
	}

	public static String[] split(String arg0, Integer... arg1) {

		StringBuffer mascara = new StringBuffer();
		for (int j = 0; j < arg1.length; j++) {

			mascara.append("(");

			for (int j2 = 0; j2 < arg1[j]; j2++) {
				mascara.append(".");
			}

			mascara.append(")");

		}
		

		Pattern re = Pattern.compile(mascara.toString());
		Matcher m = re.matcher(arg0.toString());

		while (m.find()) {

			String[] c = new String[m.groupCount()];

			for (int groupIdx = 1; groupIdx < (m.groupCount() + 1); groupIdx++) {
				c[groupIdx - 1] = m.group(groupIdx);
			}

			return c;
		}

		return null;
	}

	public static StringBuilder[] splitBuilder(String arg0, Integer... arg1) {

		StringBuffer mascara = new StringBuffer();
		for (int j = 0; j < arg1.length; j++) {

			mascara.append("(");

			for (int j2 = 0; j2 < arg1[j]; j2++) {
				mascara.append(".");
			}

			mascara.append(")");

		}

		Pattern re = Pattern.compile(mascara.toString());
		Matcher m = re.matcher(arg0.toString());

		while (m.find()) {

			StringBuilder[] c = new StringBuilder[m.groupCount()];

			for (int groupIdx = 1; groupIdx < (m.groupCount() + 1); groupIdx++) {
				c[groupIdx - 1] = new StringBuilder(m.group(groupIdx));
			}

			return c;
		}

		return null;
	}

	public static String encoding(String algorithm, String input) throws Exception {

		MessageDigest m = MessageDigest.getInstance(algorithm);
		m.update(input.getBytes(), 0, input.length());

		return new BigInteger(1, m.digest()).toString(16);
	}

	public static String encodingSHA256(String input) throws Exception {
		return encoding("SHA-256", input);
	}

	public static String encodingMD5(String input) throws Exception {
		return encoding("MD5", input);
	}

	
	
	

	public static String toJSON(Object object) {

		Gson gson = new Gson();

		return gson.toJson(object);

//		try {
//
//			Field[] declaredFields = object.getClass().getDeclaredFields();
//
//			JsonObjectBuilder builder = Json.createObjectBuilder();
//
//			for (Field field : declaredFields) {
//
//				field.setAccessible(true);
//
//				if (!field.getName().equals("createUser")
//						&& !field.getName().equals("lastChangeUser")
//							&& !field.getName().equals("serialVersionUID")
//								&& field.get(object) != null) {
//
//					builder.add(field.getName(), String.valueOf(field.get(object)));
//
//				}
//
//				field.setAccessible(false);
//
//			}
//
//			return builder.build().toString();
//
//		} catch (Exception e) {
//		}
//
//		return "";
	}

	public static <T> T fromJSON(String json, Class<T> classOfT) {

		Gson gson = new Gson();

		return gson.fromJson(json, classOfT);
	}

	public static <T> T jsonToObject(String json, Class<T> classOfT) {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		return gson.fromJson(json, classOfT);
	}

	public static HashMap<Integer, String[]> jsonToHashMap(String json) {

		Gson gson = new Gson();
		Type type = new TypeToken<HashMap<Integer, String[]>>() {
		}.getType();

		return gson.fromJson(json, type);
	}

	public static String dateToString(Calendar date, String mask) {

		if (date == null) {
			return "";
		}

		SimpleDateFormat sdf = new SimpleDateFormat(mask);

		return sdf.format(date.getTime());
	}

	public static Date stringToDate(String date, String mask) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat(mask);

			return sdf.parse(date);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return Calendar.getInstance().getTime();
	}
	
	public static Date stringToDateFromMicroServices(String date, String mask) throws Exception {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat(mask);

			return sdf.parse(date);

		} catch (Exception e) {
			throw new Exception("Date format not valid " + date );
		}

	}
	
	public static Calendar stringToCalendarFromMicroServices(String date, String mask) throws Exception {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringToDateFromMicroServices(date, mask));

		return calendar;
	}

	public static Calendar stringToCalendar(String date, String mask) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(stringToDate(date, mask));

		return calendar;
	}
	

	public static String format(Object arg0, int length, char position, char complement, boolean normalizer) {

		String value = arg0 == null ? "" : arg0.toString();

		if (arg0 != null && normalizer) {

			value = Normalizer.normalize(arg0.toString(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

		}

		return format(value.replaceAll("[^a-zA-Z0-9 -:\\[\\](){}]", ""), length, position, complement);

	}

	public static String format(Object arg0, int length, char position, char complement) {

		if (arg0 == null) {
			arg0 = "";
		}

		int tamanhoOriginal = arg0.toString().length();
		StringBuffer sb = new StringBuffer(arg0.toString());

		if (tamanhoOriginal > length) {
			if (position == 'D' || position == 'R') {
				sb.setLength(length);
			} else if (position == 'E' || position == 'L') {
				sb.delete(0, tamanhoOriginal - length);
			}
		} else {
			if (position == 'D' || position == 'R') {
				for (int i = tamanhoOriginal; i < length; i++) {
					sb.append(complement);
				}
			} else if (position == 'E' || position == 'L') {
				for (int i = tamanhoOriginal; i < length; i++) {
					sb.insert(0, complement);
				}
			}
		}

		return sb.toString();
	}
	
	public static Calendar convertDateToCalendar(Date date) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
		return null;
	}

	public static String formatTLV(String tag, Object value) {

		return formatTLV(tag, String.valueOf(value));
	}

	public static String formatTLV(String tag, String value) {

		return tag + Utils.format((value == null ? 0 : value.length()), 3, 'L', '0') + (value == null ? "" : value);
	}

	public static String formatTLV(String tag, Calendar value, String mask) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(mask);

		return formatTLV(tag, dateFormat.format(value.getTime()));
	}

	public static Long getHoursBetween(Calendar startDate, Calendar endDate) {

		// milliseconds
		long different = endDate.getTime().getTime() - startDate.getTime().getTime();

//	    logger.info("startDate : " + startDate.getTime());
//	    logger.info("endDate : "+ endDate.getTime());
//	    logger.info("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
//	    long daysInMilli = hoursInMilli * 24;

		// long elapsedDays = different / daysInMilli;
		// different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

//	    long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

//	    long elapsedSeconds = different / secondsInMilli;

//	    System.out.printf(
//	        "%d hours, %d minutes, %d seconds%n", 
//	        elapsedHours, elapsedMinutes, elapsedSeconds);

		return elapsedHours;
	}

	public static String clearSpecialChars(String arg0) {

//		if (arg0 != null) {
//			return arg0.replaceAll("[^\\dA-Za-z\\-:;./& ]", "");
//		}
		
		if (arg0 != null) {

			String replace = "AaAaAaAaCcEeEeIiOoOoOoUu";
	
			StringBuilder response = new StringBuilder(1024);
	
			char[] chars = arg0.toCharArray();
			for (char c : chars) {
	
				System.out.print((c + 0) + ", ");
	
				int index = Arrays.asList(193, 225, 195, 227, 194, 226, 192, 224, 199, 231, 201, 233, 202, 234, 205, 237,
						211, 243, 212, 244, 213, 245, 218, 250).indexOf((c + 0));
				if (index > -1) {
					c = replace.charAt(index);
				}
	
				if ((c + 0) < 32 || (c + 0) > 126) {
	
					response.append(" ");
	
				} else {
	
					response.append(c);
	
				}
	
			}
	
			return response.toString();
		}
		return null;
	}

	public static String mask(String arg0, String mask) {

		try {

			MaskFormatter mf = new MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);

			return mf.valueToString(arg0);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return arg0;
	}

	public static String getTagValue(String additionalData, String tag) {

		try {

			if (additionalData != null) {

//				logger.debug("");
//				logger.debug("tag\tlen\tvalue");

				for (int i = 0; i < additionalData.length();) {

					String t = additionalData.substring(i, i + 4);
					Integer l = Integer.parseInt(additionalData.substring(i + 4, i + 7));
					String v = additionalData.substring(i + 7, i + 7 + l);

//					logger.debug(t + "\t" + l + "\t'" + v + "'");

					i = i + 7 + l;

					if (t.equals(tag)) {

						return v;
					}

				}

//				logger.debug("");

			}

		} catch (Exception e) {

		}

		return null;
	}

	public static String formatTLV(TagTLV tagTlv, String value) {
		return Utils.formatTLV(tagTlv.tag(), value);
	}

	public static String getTagValue(String additionalData, TagTLV tagTlv) {
		return Utils.getTagValue(additionalData, tagTlv.tag());
	}

	public static String plainText(String input) {

		if (!Objects.isNull(input)) {

			input = StringUtils.stripAccents(input);

			input = Normalizer

					.normalize(input, Normalizer.Form.NFD)

					.replaceAll("[^\\p{ASCII}]", "")

					.replaceAll("[^a-zA-Z0-9]", " ")

					.toUpperCase()

					.trim();

			input = String.join(" ", Arrays.asList(input.split(" ")).stream().filter(string -> !string.trim().isEmpty())
					.collect(Collectors.toList())); // removes unnecessary whitespace
		} else {
			input = "";
		}

		return input;
	}
	
	public static String plainTextNoneCase(String input) {

		if (!Objects.isNull(input)) {

			input = StringUtils.stripAccents(input);

			input = Normalizer

					.normalize(input, Normalizer.Form.NFD)

					.replaceAll("[^\\p{ASCII}]", "")

					.replaceAll("[^a-zA-Z0-9]", " ")

					.trim();

			input = String.join(" ", Arrays.asList(input.split(" ")).stream().filter(string -> !string.trim().isEmpty())
					.collect(Collectors.toList())); // removes unnecessary whitespace
		} else {
			input = "";
		}

		return input;
	}

	public static String moneyToBrazillianFormat(BigDecimal money) {
		if (money == null) {
			return "";
		}

		DecimalFormat moneyFormat = new DecimalFormat("'R$ ' 0.00");
		return moneyFormat.format(money);
	}

	public static String formatMoneyDecimal(BigDecimal money, int length) {

		if (money == null) {
			money = new BigDecimal(0);
		}

		DecimalFormat moneyFormat = new DecimalFormat("0.00");
		String moneyString = moneyFormat.format(money);
		return format(moneyString, length, 'L', '0');

	}

	public static String formatEmail(String input) {

		if (!Objects.isNull(input)) {

			input = StringUtils.stripAccents(input);

			input = Normalizer

					.normalize(input, Normalizer.Form.NFD)

					.replaceAll("[^\\p{ASCII}]", "")

					.replace(" ", "")

					.toLowerCase()

					.trim();

		} else {
			input = "";
		}

		return input;

	}

	public static Calendar localDateTimeToCalendar(LocalDateTime ldt) {
		return ldt == null ? null : GregorianCalendar.from(ZonedDateTime.of(ldt, ZoneId.systemDefault()));
	}

	public static LocalDateTime calendarToLocalDateTime(Calendar calendar) {
		return calendar == null ? null : LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	public static String calendarToString(Calendar calendar, String pattern) {
		
		if(Objects.isNull(calendar)) {
			return "";
		}
		
		if(Objects.isNull(pattern)) {
			throw new IllegalArgumentException("Pattern cannot be NULL");
		}
		
		return new SimpleDateFormat(pattern).format(calendar.getTime());
	}
	
	/**
	 * Calcula Tempo(milis) com a hora atual e converte milis em Hora:Minuto:Segundo:milis?
	 *@author F5K5WQI
	 *5 de mai. de 2023 
	 *@param mili
	 *@param miliStart
	 *@return
	 */
	public static String calcTime( boolean mili , Long miliStart){

	    miliStart = System.currentTimeMillis() - miliStart;

	    long hours = miliStart / 1000 / 60 / 60;
	    miliStart -= hours * 1000 * 60 * 60;

	    long minutes = miliStart / 1000 / 60;
	    miliStart -= minutes * 1000 * 60;

	    long seconds = miliStart / 1000;
	    miliStart -= seconds * 1000;

	    StringBuffer time = new StringBuffer();
	    if( hours > 0 ) {
	      time.append( hours + ":" );
	    } else {
	      time.append( "00:" );
	    }

	    if( hours > 0 && minutes < 10 ) {
	      time.append( "0" );
	    }

	    if(minutes > 0 ) {
	      time.append( minutes + ":" );
	    }else {
	      time.append( "00:" );
	    }

	    if( seconds < 10 ) {
	      time.append( "0" );
	    }

	    if( seconds > 0 ) {
	      time.append( seconds );
	    }else {
	      time.append( "0" );
	    }

	    if( mili )
	    {
	      time.append( "." );
	      if( miliStart < 100 )
	        time.append( "0" );
	      if( miliStart < 10 )
	        time.append( "0" );
	      time.append( miliStart );
	    }

	    return time.toString();
	  }
	
	/**
	 * Converte milis em Hora:Minuto:Segundo:milis?
	 *@author F5K5WQI
	 *5 de mai. de 2023 
	 *@param mili
	 *@param miliStart
	 *@return
	 */
	public static String convertTime( boolean mili , Long miliStart){

	    long hours = miliStart / 1000 / 60 / 60;
	    miliStart -= hours * 1000 * 60 * 60;

	    long minutes = miliStart / 1000 / 60;
	    miliStart -= minutes * 1000 * 60;

	    long seconds = miliStart / 1000;
	    miliStart -= seconds * 1000;

	    StringBuffer time = new StringBuffer();
	    if( hours > 0 ) {
	      time.append( hours + ":" );
	    } else {
	      time.append( "00:" );
	    }

	    if( hours > 0 && minutes < 10 ) {
	      time.append( "0" );
	    }

	    if(minutes > 0 ) {
	      time.append( minutes + ":" );
	    }else {
	      time.append( "00:" );
	    }

	    if( seconds < 10 ) {
	      time.append( "0" );
	    }

	    if( seconds > 0 ) {
	      time.append( seconds );
	    }else {
	      time.append( "0" );
	    }

	    if( mili )
	    {
	      time.append( "." );
	      if( miliStart < 100 )
	        time.append( "0" );
	      if( miliStart < 10 )
	        time.append( "0" );
	      time.append( miliStart );
	    }

	    return time.toString();
	  }
	
	/**
	 * Verifica se o CNPJ contém 14 caracteres e somente números.
     * @param cnpj
     * @return True se o CNPJ for válido, false se o CNPJ for inválido.
     * @author F9KDA7S
	 */
	public static boolean isValidCNPJ(String cnpj) {
		cnpj = cnpj.replaceAll("\\.", "").replaceAll("\\-", "").replaceAll("/", "").trim();
		if(cnpj == null || cnpj.length() != 14 || cnpj.isEmpty())
			return false;

		try {
			Long.parseLong(cnpj);
			return true;
		} catch (NumberFormatException e) { // CNPJ does not have only numbers
			return false;
		}
	}
	
	public static boolean isCNPJ(String CNPJ) {
		
		CNPJ = removeCaracteresEspeciais(CNPJ);
		
		// considera-se erro CNPJ's formados por uma sequencia de numeros iguais
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") || CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") || CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") || CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") || CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") || (CNPJ.length() != 14))
			return (false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		// "try" - protege o código para eventuais erros de conversao de tipo (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 11; i >= 0; i--) {
				// converte o i-ésimo caractere do CNPJ em um número:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posição de '0' na tabela ASCII)
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else
				dig13 = (char) ((11 - r) + 48);

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 2;
			for (i = 12; i >= 0; i--) {
				num = (int) (CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else
				dig14 = (char) ((11 - r) + 48);

			// Verifica se os dígitos calculados conferem com os dígitos informados.
			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}
	
	public static String removeCaracteresEspeciais(String doc) {
		if (doc.contains(".")) {
			doc = doc.replace(".", "");
		}
		if (doc.contains("-")) {
			doc = doc.replace("-", "");
		}
		if (doc.contains("/")) {
			doc = doc.replace("/", "");
		}
		return doc;
	}
	
	public static boolean isValidCPF(String CPF) {
        // considera-se erro CPF's formados por uma sequencia de numeros iguais
        if (CPF.equals("00000000000") ||
            CPF.equals("11111111111") ||
            CPF.equals("22222222222") || CPF.equals("33333333333") ||
            CPF.equals("44444444444") || CPF.equals("55555555555") ||
            CPF.equals("66666666666") || CPF.equals("77777777777") ||
            CPF.equals("88888888888") || CPF.equals("99999999999") ||
            (CPF.length() != 11))
            return(false);

        char dig10, dig11;
        int sm, i, r, num, peso;

        // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
        try {
        // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i=0; i<9; i++) {
        // converte o i-esimo caractere do CPF em um numero:
        // por exemplo, transforma o caractere '0' no inteiro 0
        // (48 eh a posicao de '0' na tabela ASCII)
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                dig10 = '0';
            else dig10 = (char)(r + 48); // converte no respectivo caractere numerico

        // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for(i=0; i<10; i++) {
            num = (int)(CPF.charAt(i) - 48);
            sm = sm + (num * peso);
            peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11))
                 dig11 = '0';
            else dig11 = (char)(r + 48);

        // Verifica se os digitos calculados conferem com os digitos informados.
            if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                 return(true);
            else return(false);
                } catch (InputMismatchException erro) {
                return(false);
            }
    }
	
	
	public static Pattern getPattern() {
		final String regularExpression = "([\\w\\:\\\\w ./-]+\\w+(\\.)?\\w+)";
		return Pattern.compile(regularExpression);
	}
	
	/**
	 * Checks if the parameter contains only numbers, commas or dots/periods.
     * @param d
     * @return True if the parameter has only numbers, commas or periods/full stops. False otherwise.
     * @author F9KDA7S
	 */
	public static boolean isValidDouble(String d) {
		
		final String doubleValido = "[\\d,.]*";
		
		if(d.matches(doubleValido)) {
			return true;
		} else {
			return false;
		}

	}
	
	public static boolean isValidString(String validar) {
		if(validar == null) {
			return false;
		}
		if(validar.isEmpty()) {
			return false;
		}
		return true;
	}
	
	

	public static Date getMenorData(Date... datas) {
		Date menorData = null;
		try {
			List<Date> ldt =  Arrays.asList(datas);
			ldt = ldt.stream().filter(x -> x != null).collect(Collectors.toList());
			if (ldt.size() > 1) {
				ldt.sort((d1,d2) -> d1.compareTo(d2));
				menorData = ldt.get(0);
			} else if (ldt.size() == 1) {
				menorData = ldt.get(0);
			}
		} catch(Exception e) {			
		}
		return menorData;
	}	
	
	public static String loadHyperlink(String text) {
		StringBuilder line = new StringBuilder(1024);
		if (text.contains("https")) {
			List<String> hyperlinks = Arrays.asList(text.split(" "));
			for (String texto : hyperlinks) {
				if (!texto.contains("https"))
					line.append(texto);
				else
					line.append("<a href=\""+texto+"\" target=\"_blank\" >"+texto+"</a>");
			}
			return line.toString();
		} else {
			return text;
		}
	}
	
	public static String celPhoneFormated(String numero) {
		String numeroLimpo = numero.replaceAll("\\D+", "");

		if (numeroLimpo.length() >= 9) {
		    String codigoArea = numeroLimpo.substring(0, 2);

		    String numeroTelefone = numeroLimpo.substring(2);

		    StringBuilder numeroFormatado = new StringBuilder();
		    numeroFormatado.append("(").append(codigoArea).append(") ").append(numeroTelefone);

		    return numeroFormatado.toString();
		} 
		return "";
	}
	
	public static void writeTxt(String file, String text, boolean replaceFile) throws Exception {

		file = file.trim();

//		System.err.println("File: " + file);
//		System.err.println("text: " + text);
//		System.err.println("Replace: " + replaceFile);

		new File(file).mkdirs();

		if (replaceFile && new File(file).exists()) {
			new File(file).delete();
		}

		// FileWriter arq = new FileWriter(file);
		PrintWriter gravarArq = new PrintWriter(new File(file), "UTF-8");

		// System.err.println(""+text);

		// gravarArq.printf(text);
		gravarArq.write(text);
		gravarArq.close();
	}

	public static void writeTxtList(String file, List<String> textList, boolean replaceFile) throws Exception {
		if (replaceFile && new File(file).exists()) {
			new File(file).delete();
		}
		FileWriter arq = new FileWriter(file,true);
		PrintWriter gravarArq = new PrintWriter(arq);

		for (int i = 0; i < textList.size(); i++) {
			gravarArq.print(textList.get(i) + "\r\n");
		}
		arq.close();
	}
	
	public static String readTxt(String url) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(url));
		String text = "";
		while (br.ready()) {
			text += br.readLine();
		}
		br.close();
		return text;
	}

	public static List<String> readTxtList(String url) throws IOException {
		System.err.println("URL>> "+url);
		List<String> lista = new ArrayList<>();

		final BufferedReader br = new BufferedReader(new FileReader(url));

		while (br.ready()) {
			lista.add(br.readLine());
		}
		br.close();
		return lista;
	}
	
	/**
	 * (AlunosCursos)
	 * 
	 * @param text
	 * @return
	 */
	public static String normalizerStringCaps(String text) {
		//System.err.println("TEXT: " + text);

		text = text.replace("_", "-");
		String[] split = text.toLowerCase().split("-");

		for (int i = 0; i < split.length; i++) {

			if (i > 0) {

				text += (Character.toUpperCase(split[i].charAt(0)) + split[i].substring(1));

			} else {

				text = split[i];

			}
		}

		return (Character.toUpperCase(text.charAt(0)) + text.substring(1));
	}
	
}
