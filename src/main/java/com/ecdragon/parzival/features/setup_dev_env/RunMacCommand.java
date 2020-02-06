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
//				BufferedReader stdErrBufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				BufferedWriter stdInBufferedWriter = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
				) {
			
			for (String command : commandsList) {
				
				System.out.println("About to execute command: " + command);
				String readLineString = null;
				StringBuilder stdOutStringBuilder = new StringBuilder();

//				stdInBufferedWriter.write(command + "\n");
				// From https://stackoverflow.com/questions/3643939/java-process-with-input-output-stream
				// (" + input + ") && echo --EOF--
//				stdInBufferedWriter.write("(" + command + ") && echo --EOF--\n");
				stdInBufferedWriter.write(command + "\n");
				stdInBufferedWriter.flush();
				stdInBufferedWriter.write("echo " + tokenString + "\n");
				stdInBufferedWriter.flush();
				
				// Wait a smidge for the command to execute
//				Thread.sleep(15L);
				
				// read the output from the command
				// 10 minute timeout waiting for output
				long msTimeout = 600000L;
				long waitSoFar = 0L;
				long waitTime = 50L; 
				do {
					Thread.sleep(waitTime);
					waitSoFar += waitTime;
//					System.out.println("waitSoFar: " + waitSoFar);
					while (stdOutBufferedReader.ready() && (readLineString = stdOutBufferedReader.readLine()) != null) {
						stdOutStringBuilder.append(readLineString + "\n");
						System.out.println(readLineString);
						waitSoFar = 0L;
					}
//					if (readLineString != null) {
//						Boolean isTrue = !(readLineString.trim().equalsIgnoreCase(tokenString));
//						System.out.println("is it true?? " + isTrue);
//					}
//					System.out.println("(" + (readLineString != null) + " && " + !(readLineString.trim().equalsIgnoreCase(tokenString)) + ") && " + (waitSoFar < msTimeout) + " = "
//							+ ((readLineString != null 
//							&& !(readLineString.trim().equalsIgnoreCase(tokenString)))
//									&& waitSoFar < msTimeout)
//							);
				}
				while ((readLineString != null 
							&& !(readLineString.trim().equalsIgnoreCase(tokenString)))
						&& waitSoFar < msTimeout);
				
				if (waitSoFar >= msTimeout) {
					throw new Exception("timed out waiting for command to output next line. command: " + command);
				}
				
				// read any errors from the attempted command
//				while (stdErrBufferedReader.ready() && (readLineString = stdErrBufferedReader.readLine()) != null) {
//					stdErrStringBuilder.append(readLineString + "\n");
//				}
				
				String currentOutput = commandResults.getOutput();
				String nextOutput = stdOutStringBuilder.toString();
//				System.out.println("nextoutput: " + nextOutput);
				System.out.println("\n");
				commandResults.setOutput(currentOutput + "\n\n" + nextOutput);
				
				// This doesn't work! I fed it "ls -la" with -v as /bin/bash switch, and it outputted "ls -la" in the error stream!!
//				String stdErrString = stdErrStringBuilder.toString();
//				commandResults.setErrorOutput(commandResults.getErrorOutput() + stdErrString);
//				Boolean success = (stdErrString == null || stdErrString.isEmpty()) ? Boolean.FALSE : Boolean.TRUE;
//				commandResults.setSuccess(success);
//				process.
//				if (!success) {
//					return commandResults;
//				}
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
