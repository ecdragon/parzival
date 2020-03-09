package com.ecdragon.parzival.utils;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.utils.URIBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerMapping;

public class UtilsParzival {
	
	private static final char[] alphaNumericChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
	private static final int alphaNumericCharsLength = alphaNumericChars.length;
	public static final String dateTimeFormatSortable = "yyyyMMdd_HHmmss";
	
	private static Path userHomeDirPath;
	private static String userHomeDirPathString;
	public static Path getUserHomeDirPath() {
		if (userHomeDirPath == null) {
			userHomeDirPath = Paths.get(System.getProperty("user.home")); 
		}
		return userHomeDirPath;
	}
	public static String getUserHomeDirPathString() {
		if (userHomeDirPathString == null) {
			Path userHomeDirPath = getUserHomeDirPath();
			if (userHomeDirPath != null) {
				userHomeDirPathString = userHomeDirPath.toString();
			}
		}
		return userHomeDirPathString;
	}
	
	public static Path getThisAppWorkingDirectoryPath() {
		try {
			Path thisAppWorkingDirectoryPath = Paths.get("").toAbsolutePath();
			return thisAppWorkingDirectoryPath;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateDateTimeNowSortableString() {
        LocalDateTime now = LocalDateTime.now();
		String dateTimeSortableString = generateDateTimeSortableString(now);
		return dateTimeSortableString;
	}
	
	public static String generateDateTimeSortableString(LocalDateTime localDateTime) {
		if (localDateTime == null) {
			return null;
		}
        DateTimeFormatter dateTimeDirectoryNamePrefixFormatter = DateTimeFormatter.ofPattern(dateTimeFormatSortable);
        String dateTimeSortableString = localDateTime.format(dateTimeDirectoryNamePrefixFormatter);
        return dateTimeSortableString;
	}
	
	public static String replaceTokens(
			String stringToMakeReplacementsIn, 
			List<String> replacementsList, 
			HashMap<String, String> replacementsMap) {
		
		String result = stringToMakeReplacementsIn;
		for (String token : replacementsList) {
			result = replaceToken(result, token, replacementsMap.get(token));
		}

		return result;
	}
	
	/**
	 * Wraps token string with ${ }, and uses Matcher.quoteReplacement(String) on replacement string
	 * Returns new string with substitutions
	 * if stringTokenToReplace or replacementString are null input, it just returns the stringToMakeReplacementsIn
	 */
	public static String replaceToken(String stringToMakeReplacementsIn, String stringTokenToReplace, String replacementString) {
		if (stringTokenToReplace == null || replacementString == null) {
			return stringToMakeReplacementsIn;
		}
		String stringWithReplacements = stringToMakeReplacementsIn.replaceAll("\\$\\{" + stringTokenToReplace + "\\}", Matcher.quoteReplacement(replacementString));
		return stringWithReplacements;
	}

	public static boolean isNullOrEmpty(String someString) {
		
		if (someString == null) {
			return true;
		}
		if (someString.isEmpty()) {
			return true;
		}
		return false;
	}
	
	public static void main2(String[] args) {
		
		List<String> someStrings = new ArrayList<>();
		
		someStrings.add(null);
		someStrings.add(null);
		someStrings.add(null);
		someStrings.add(null);
		someStrings.add(null);
		
		System.out.println("someStrings size: " + someStrings.size());
		
		for (String stringone : someStrings) {
			System.out.println("no null pointer - stringone" + stringone);
		}
		
		System.out.println("no exception!!!");
	}
	
	public static String convertListToNewlineSeparated(List<String> stringsToConvert) {
		String result = null;
		if (stringsToConvert == null) {
			return result;
		}
		StringBuilder resultBuilder = new StringBuilder();
		// Don't need commas or semicolons for Groovy / Gradle
//		boolean isFirst = true;
//		for (String stringToConvert : stringsToConvert) {
//			if (stringToConvert != null) {
//				if (!isFirst) {
//					resultBuilder.append(",");
//				}
//				resultBuilder.append(stringToConvert);
//				isFirst = false;
//			}
//		}
		stringsToConvert.forEach(
				stringToConvert -> {
					if (stringToConvert != null) {
						resultBuilder.append(stringToConvert + "\n");
					}
				});
		result = resultBuilder.toString();
		return result;
	}
	
	public static String createRandomString(int length) {
		if (length < 1) {
			return "";
		}
		String randomPasswordSalt = "";
		for (int i = 0; i < length; i++) {
			randomPasswordSalt += alphaNumericChars[(int)(Math.random() * alphaNumericCharsLength)];
		}
		return randomPasswordSalt;
	}
	
	public static Properties readProjectConfigurationProperties(Path pathToProperties) {
		if (pathToProperties == null) {
			return null;
		}
		try (InputStream inputStream = Files.newInputStream(pathToProperties)) {
			Properties result = new Properties();
			result.load(inputStream);
			return result;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Eats exceptions and just returns null instead
	 */
	public static Path convertStringToPath(String pathString) {
		if (pathString == null) {
			return null;
		}
		try {
			Path result = Paths.get(pathString);
			return result;
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static Path createDirectories(Path pathIncludingDirectoriesToCreate) {
		try {
			Path result = Files.createDirectories(pathIncludingDirectoriesToCreate);
			return result;
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static Path getPath(String... pathSegments) {
		if (pathSegments == null || pathSegments.length == 0) {
			return null;
		}
		try {
			Path pathResult = Paths.get(pathSegments[0]);
			boolean isFirst = true;
			for (String pathSegment: pathSegments) {
				if (!isFirst) {
					pathResult = concatenatePaths(pathResult, pathSegment);
				}
				isFirst = false;
			}
			return pathResult;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static List<File> listAllFilesInDirectoryPath(Path directoryPath) {
		if (directoryPath == null) {
			return null;
		}
		try {
			File directoryPathFile = directoryPath.toFile();
			if (directoryPathFile.isDirectory()) {
				
				List<File> directoryFiles = 
						Stream.of(directoryPathFile.listFiles())
						.sorted()
						.collect(Collectors.toList());
				
				return directoryFiles;
			}
			// We COULD use the file's parent dir...
//			else if (directoryPathFile.isFile()) {
//				File parentDirectory = directoryPathFile.getParentFile();
//			}
			return null;
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static List<File> listDirectoriesInDirectoryPath(Path directoryPath) {
		if (directoryPath == null) {
			return null;
		}
		try {
			// Get em all
			List<File> allFilesInDir = listAllFilesInDirectoryPath(directoryPath);
			if (allFilesInDir == null) {
				return null;
			}
			
			// Filter out all but directory files...
			List<File> allDirsInDir = new ArrayList<>();
			for (File file : allFilesInDir) {
				if (file.isDirectory()) {
					allDirsInDir.add(file);
				}
			}
			
			return allDirsInDir;
		} 
		catch (Exception e) {
			return null;
		}
	}

	public static List<File> listFilesInDirectoryPath(Path directoryPath) {
		if (directoryPath == null) {
			return null;
		}
		try {
			// Get em all
			List<File> allFilesInDir = listAllFilesInDirectoryPath(directoryPath);
			if (allFilesInDir == null) {
				return null;
			}
			
			// Filter out all but directory files...
			List<File> filesInDir = new ArrayList<>();
			for (File file : allFilesInDir) {
				if (file.isFile()) {
					filesInDir.add(file);
				}
			}
			
			return filesInDir;
		} 
		catch (Exception e) {
			return null;
		}
	}
	
	public static Path concatenatePaths(Path pathLeft, String pathRight) {
		Path result = null;
		if (pathRight == null) {
			result = pathLeft;
		}
		else if (pathLeft == null) {
			result = Paths.get(pathRight);
		}
		else {
			result = Paths.get(pathLeft.toString(), pathRight);
		}
		return result;
	}
	
	public static Path concatenatePaths(Path pathLeft, Path pathRight) {
		Path result = null;
		if (pathLeft == null && pathRight == null) {
			// Leave it null
		}
		else if (pathLeft == null) {
			result = pathRight;
		}
		else if (pathRight == null) {
			result = pathLeft;
		}
		else {
			result = concatenatePaths(pathLeft, pathRight.toString());
		}
		return result;
	}
	
	public static void addStringsToStringBuilder(StringBuilder stringBuilderToAddStringsTo, List<String> stringsToAdd) {
		if (stringBuilderToAddStringsTo == null || stringsToAdd == null || stringsToAdd.isEmpty()) {
			return;
		}
		for (String stringToAdd : stringsToAdd) {
			stringBuilderToAddStringsTo.append(stringToAdd);
		}
	}
	
	public static void rollbackTransaction(
			Transaction transaction, 
			Logger logger, 
			String methodLabel
			) {
		
		if (transaction != null) {
			try {
				transaction.rollback();
			} 
			catch (Exception transactionRollbackException) {
				if (logger != null) {
					logger.error(methodLabel + "Error rolling back transaction. Exception thrown.");
				}
//				transactionRollbackException.printStackTrace();
			}
		} 
		else {
			if (logger != null) {
				logger.error(methodLabel + "Error rolling back transaction. Null transaction.");
			}
		}
	}
	
	public static void closeSession(Session session, Logger logger, String methodLabel) {
		try {
			session.close();
		} 
		catch (Exception e) {
			if (logger != null) {
				logger.error(methodLabel + "Error rolling back transaction. Exception thrown.");
			}
		}
	}
	
	public static ArrayList<String> extractUrlPathNodesAsList(HttpServletRequest request) {
		
		try {
			String path = (String) request.getAttribute(
					HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			
			String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			
			AntPathMatcher antPathMatcher = new AntPathMatcher();
			String endPath = antPathMatcher.extractPathWithinPattern(bestMatchPattern, path);
			
			String[] splitEndPath = endPath.split("/");
			
			ArrayList<String> pathNodeNameList = new ArrayList<>();
//			String keyName = null;
			for (int i = 0; i < splitEndPath.length; i++) {
				if (splitEndPath[i] != null && !splitEndPath[i].isEmpty()) {
					pathNodeNameList.add(splitEndPath[i]);
				}
//				if (i < splitEndPath.length - 1) {
//					pathNodeNameList.add(splitEndPath[i]);
//				}
//				else {
//					keyName = splitEndPath[i];
//				}
			}
			if (pathNodeNameList.isEmpty()) {
				return null;
			}
			return pathNodeNameList;
		}
		catch (Exception e) {
			return null;
		}

	}
	
	public static Long convertStringToLong(String stringToConvert) {
		Long longResult = null;
		try {
			longResult = Long.parseLong(stringToConvert);
		} 
		catch (Exception e) {
			return null;
		}
		return longResult;
	}
	
	public static String convertLongToString(Long longToConvert) {
		String stringResult = null;
		try {
			stringResult = Long.toString(longToConvert);
//			stringResult = String.valueOf(longToConvert);
		} 
		catch (Exception e) {
			// Skip
		}
		return stringResult;
	}
	
	public static Boolean convertStringToBoolean(String booleanString) {
		if (booleanString == null) {
			return null;
		}
		booleanString = booleanString.trim();
		Long booleanLong = convertStringToLong(booleanString);
		if (booleanLong != null) {
			if (booleanLong == 1L) {
				return Boolean.TRUE;
			}
			if (booleanLong == 0L) {
				return Boolean.FALSE;
			}
		}
		if ("true".equalsIgnoreCase(booleanString.trim())) {
			return Boolean.TRUE;
		}
		if ("false".equalsIgnoreCase(booleanString.trim())) {
			return Boolean.FALSE;
		}
		if ("t".equalsIgnoreCase(booleanString.trim())) {
			return Boolean.TRUE;
		}
		if ("f".equalsIgnoreCase(booleanString.trim())) {
			return Boolean.FALSE;
		}
		return null;
	}
	
	public static Integer convertStringToInteger(String stringToConvert) {
		Integer integerResult = null;
		try {
			integerResult = Integer.parseInt(stringToConvert);
		} 
		catch (Exception e) {
			return null;
		}
		return integerResult;
	}
	
	public static BigDecimal convertStringToBigDecimal(String bigDecimalString) {
		if (bigDecimalString == null || bigDecimalString.isEmpty()) {
			return null;
		}
		BigDecimal bigDecimalResult = null;
		try {
			bigDecimalResult = new BigDecimal(bigDecimalString);
		}
		catch (Exception e) {
			// Do nothing
		}
		return bigDecimalResult;
	}
	
	public static int compareByPriorityThenName(
			Integer priorityLeft, 
			Integer priorityRight,
			String nameLeft,
			String nameRight) {
		if (priorityRight != null) {
			if (priorityLeft != null) {
				if (priorityLeft < priorityRight) {
					return -1;
				}
				else if (priorityLeft > priorityRight) {
					return 1;
				}
			}
			else {
				// If this priority is null but the input is not null, this should go to end (last)
				// In other words, treat no priority as the lowest priority
				return 1;
			}
		}
		else if (priorityLeft != null) {
			// Input priority is null, this is not, so this is higher...
			return -1;
		}
		// Only way to get here is if both priorities are null or the same
		//   so use the name to sort
		return nameLeft.compareTo(nameRight);
	}
	
	public static void appendNewline(StringBuilder stringBuilder, boolean isHtml) {
		if (stringBuilder == null) {
			return;
		}
		if (isHtml) {
			stringBuilder.append("<br>");
		}
		else {
			stringBuilder.append("\n");
		}
	}
	
	public static String addNewlinesAtEndIfNotEmpty(String stringToAddTo, int newlinesToAdd) {
		if (stringToAddTo != null && !stringToAddTo.isEmpty() && newlinesToAdd > 0) {
			for (int i = 0; i < newlinesToAdd; i++) {
				stringToAddTo += "\n";
			}
		}
		return stringToAddTo;
	}
	
	public static void appendTabsForIndentation(StringBuilder stringBuilder, Integer indentation) {
		if (stringBuilder == null || indentation == null || indentation < 1) {
			return;
		}
		for (int i = 0; i < indentation; i++) {
			stringBuilder.append("\t");
		}
		return;
	}
	
	public static boolean appendIfFirstHasBeenFound(boolean firstHasBeenFound, StringBuilder stringBuilder, String stringToAppend) {
		if (stringBuilder != null && stringToAppend != null && firstHasBeenFound) {
			stringBuilder.append(stringToAppend);
		}
		return true;
	}
	
	public static String lowerFirstLetter(String stringToLower) {
		if (stringToLower != null && stringToLower.length() > 0) {
			String afterFirstString = "";
			if (stringToLower.length() > 1) {
				afterFirstString = stringToLower.substring(1);
			}
			stringToLower = stringToLower.substring(0, 1).toLowerCase() + afterFirstString;
		}
		return stringToLower;
	}
	
	public static void main3(String[] args) {
		System.out.println(convertCamelCaseToSnakeCase("SomeString"));
		System.out.println(convertCamelCaseToSnakeCase("someString"));
		System.out.println(convertCamelCaseToSnakeCase("SomeSString", true));
		System.out.println(convertCamelCaseToSnakeCase("SomeSString", false));
	}
	
	public static String convertCamelCaseToSnakeCase(String stringToConvert) {
		String result = convertCamelCaseToSnakeCase(stringToConvert, false);
		return result;
	}
	
	public static String replacePeriodsWithForwardSlashes(String stringToReplace) {
		if (stringToReplace == null) {
			return null;
		}
		String result = stringToReplace.replace('.', '/');
		return result;
	}
	
	public static String convertCamelCaseToSnakeCase(String stringToConvert, boolean treatConsecutiveCapitalsAsAcronym) {
		if (stringToConvert != null) {
			StringBuilder resultStringBuilder = new StringBuilder();
			boolean lastCharWasCapital = false;
			for (int i = 0; i < stringToConvert.length(); i++) {
				char charIt = stringToConvert.charAt(i);
				if (Character.isUpperCase(charIt)) {
					String prefix = "_";
					if (i == 0 || (treatConsecutiveCapitalsAsAcronym && lastCharWasCapital)) {
						prefix = "";
					}
					resultStringBuilder.append(prefix + Character.toLowerCase(charIt));
					lastCharWasCapital = true;
				}
				else {
					resultStringBuilder.append(charIt);
					lastCharWasCapital = false;
				}
			}
			String result = resultStringBuilder.toString();
			return result;
		}
		return null;
	}
	
	public static HttpHeaders getStandardHttpHeadersJson() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		return headers;
	}
	
	public static URI createURI(String host, Integer port, String path, boolean useHttps) {
		try {
			URL url = createURL(host, port, path, useHttps);
			URI uri = url.toURI();
			return uri;
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public static URL createURL(String host, Integer port, String path, boolean useHttps) {
		try {
			URIBuilder builder = new URIBuilder();
			
			// Scheme
			if (useHttps) {
				builder.setScheme("https");
			}
			else {
				builder.setScheme("http");
			}
			
			// Host
			builder.setHost(host);
			
			// Port
			if (port != null) {
				builder.setPort(port);
			}
			
			// Path
			if (path != null && !path.isEmpty()) {
				if (path.substring(0, 1) != "/") {
					path = "/" + path;
				}
				builder.setPath(path);
			}
			
			// Lets add parameters later
//			builder.addParameter("abc", "xyz");
			
			URL url = builder.build().toURL();
			return url;
		}
		catch (Exception e) {
			return null;
		}
	}

	public static void printAllAttributesAndParametersInRequest(HttpServletRequest request) {
		
		if (request == null) {
			return;
		}
		
		try {
			// Get and print all attribute names...
			List<String> requestAttributeNamesList = new ArrayList<>();
			System.out.println("\n\nRequest attributes:\n");
			try {
				Enumeration<String> requestAttributeNamesEnumeration = request.getAttributeNames();
//				requestAttributeNamesList = new ArrayList<>();
				while(requestAttributeNamesEnumeration.hasMoreElements()) {
					requestAttributeNamesList.add(requestAttributeNamesEnumeration.nextElement());
				}
				for (String requestAttributeName : requestAttributeNamesList) {
					System.out.println(requestAttributeName);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			// Get and print all request param names...
			List<String> requestParameterNamesList = new ArrayList<>();
			System.out.println("\n\nRequest parameters:\n");
			try {
				Enumeration<String> requestParameterNamesEnumeration = request.getParameterNames();
//				requestParameterNamesList = new ArrayList<>();
				while(requestParameterNamesEnumeration.hasMoreElements()) {
					requestParameterNamesList.add(requestParameterNamesEnumeration.nextElement());
				}
				for (String requestParameterName : requestParameterNamesList) {
					System.out.println(requestParameterName);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
