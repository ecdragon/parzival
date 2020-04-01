package com.ecdragon.parzival.features.setup_dev_env;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
 
public class RunMacCommand {
 
	public static String run(String[] command) {
		String readLineString = null;
		StringBuilder resultStringBuilder = new StringBuilder();
 
		try {
 
			Process p = Runtime.getRuntime().exec(command);
 
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
 
			// read the output from the command
//			System.out.println("Here is the standard output of the command:\n");
			while ((readLineString = stdInput.readLine()) != null) {
				resultStringBuilder.append(readLineString + "\n");
//				System.out.println(readLineString);
			}
 
			// read any errors from the attempted command
//			System.out.println("Here is the standard error of the command (if any):\n");
			while ((readLineString = stdError.readLine()) != null) {
				resultStringBuilder.append(readLineString + "\n");
//				System.out.println(readLineString);
			}
 
			return resultStringBuilder.toString();
		} 
		catch (IOException e) {
			resultStringBuilder.append("exception happened - here's what I know: " + e.getMessage());
			e.printStackTrace();
			return resultStringBuilder.toString();
		}
	}
	
	public static CommandResults runCommands(String... commands) {
		
//		StringBuilder stdOutStringBuilder = new StringBuilder();
//		StringBuilder stdErrStringBuilder = new StringBuilder();
		CommandResults commandResults = new CommandResults();
		commandResults.setOutput("");
		commandResults.setErrorOutput("");
		String tokenString = "---ENDSTRING---";
		
		Process process = null;
		try {
//			ProcessBuilder builder = new ProcessBuilder("/bin/bash -v");
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-v");
			builder.redirectErrorStream(true);
			process = builder.start();
		} 
		catch (Exception e) {
			e.printStackTrace();
			commandResults.setSuccess(Boolean.FALSE);
			return commandResults;
		}
		
		// Add source commands to get local profile stuff
		List<String> commandsList = new ArrayList<>();
//		commandsList.add("echo $PATH");
//		commandsList.add("env");
		commandsList.add("set +v");
		commandsList.add("source /etc/profile");
//		commandsList.add("echo $PATH");
//		commandsList.add("env");
		String homePathString = System.getProperty("user.home");
		Path bashProfilePath = Paths.get(homePathString + "/.bash_profile");
		if (Files.exists(bashProfilePath)) {
			commandsList.add("source " + bashProfilePath);
//			commandsList.add("echo $PATH");
//			commandsList.add("env");
		}
		commandsList.add("set -v");
		for (String command : commands) {
			commandsList.add(command);
		}
		
		try (
				BufferedReader stdOutBufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				BufferedWriter stdInBufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				) {
			
			for (String command : commandsList) {
				
				System.out.println("About to execute command: " + command);
				StringBuilder stdOutStringBuilder = new StringBuilder();

				stdInBufferedWriter.write(command + "\n");
				stdInBufferedWriter.flush();
				stdInBufferedWriter.write("echo " + tokenString + "\n");
				stdInBufferedWriter.flush();
				
				flushOutput(stdOutBufferedReader);
				
				String currentOutput = commandResults.getOutput();
				String nextOutput = stdOutStringBuilder.toString();
				System.out.println("\n");
				commandResults.setOutput(currentOutput + "\n\n" + nextOutput);
			}
			
			commandResults.setSuccess(Boolean.TRUE);
		} 
		catch (Exception e) {
			e.printStackTrace();
			commandResults.setSuccess(Boolean.FALSE);
		}
		
		return commandResults;
//		
//		String stdOutString = stdOutStringBuilder.toString();
//		String stdErrString = stdErrStringBuilder.toString();
//		Boolean success = (stdErrString == null || stdErrString.isEmpty()) ? Boolean.FALSE : Boolean.TRUE;
//		commandResults.setOutput(stdOutString);
//		commandResults.setErrorOutput(stdErrString);
//		commandResults.setSuccess(success);
//		
//		return commandResults;
	}
	
	private static void flushOutput(BufferedReader stdOutBufferedReader) throws Exception {
		// read the output from the command
		// 10 minute timeout waiting for output
		long msTimeout = 600000L;
		long waitSoFar = 0L;
		long waitTime = 50L;
		String readLineString = null;
		StringBuilder stdOutStringBuilder = new StringBuilder();
		String tokenString = "---ENDSTRING---";
		
		do {
			Thread.sleep(waitTime);
			waitSoFar += waitTime;
			while (stdOutBufferedReader.ready() && (readLineString = stdOutBufferedReader.readLine()) != null) {
				stdOutStringBuilder.append(readLineString + "\n");
				System.out.println(readLineString);
				waitSoFar = 0L;
			}
			if (waitSoFar >= msTimeout) {
				break;
			}
		}
		while (readLineString == null
			|| !readLineString.trim().equalsIgnoreCase(tokenString));
		
		if (waitSoFar >= msTimeout) {
			throw new Exception("timed out waiting for command to output next line.");
		}
	}
	
	public static Process startProcess() {
		try {
//			ProcessBuilder builder = new ProcessBuilder("/bin/bash -v");
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-v");
			Process process = builder.start();
			return process;
		} 
		catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
