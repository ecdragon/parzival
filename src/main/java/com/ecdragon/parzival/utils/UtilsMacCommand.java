package com.ecdragon.parzival.utils;
 
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
 
public class UtilsMacCommand {
 
	public static String run(String[] command) {
		String readLineString = null;
		StringBuilder resultStringBuilder = new StringBuilder();
 
		try {
 
//			Process p = Runtime.getRuntime().exec("ps -few");
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
	
	public static String runCleanQuarantineFlagCommandForDirectory(Path directoryPathToClean) {
		if (directoryPathToClean == null) {
			return null;
		}
		// Command to run: "find . -iname '*' -print0 | xargs -0 xattr -d com.apple.quarantine"
		String[] cleanQuarantineFlagCommandStringArray = {
				"/bin/sh",
				"-c",
				"cd " + directoryPathToClean.toString(),
//				"for f in *; do xattr -dr com.apple.quarantine \"$f\" 2>/dev/null; done"
//				"for f in *; do echo \"$f\" 2>/dev/null; done"
				"ls",
				"pwd"
//				"find " 
//					+ directoryPathToClean.toString()
//					+ " -iname '*' -print0 | xargs -0 xattr -d com.apple.quarantine"
				};
//		System.out.println("About to clean quarantine flag from all files (recursively through subdirs) in dir: " + eclipseAppFileLocationPath.toString());
		String cleanQuarantineFlagCommandResults = UtilsMacCommand.run(cleanQuarantineFlagCommandStringArray);
		System.out.println("cleanQuarantineFlagCommandResults: " + cleanQuarantineFlagCommandResults);
		
		return cleanQuarantineFlagCommandResults;
	}
	
	public static void main(String[] args) {
		try {
			String macCommandResults = 
					runCleanQuarantineFlagCommandForDirectory(Paths.get("/Users/user/Data/projects/d/20010101_100000_parzival/tmp005"));
			System.out.println(macCommandResults);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static MacCommandResults runCommands(String... commands) {
		
//		StringBuilder stdOutStringBuilder = new StringBuilder();
//		StringBuilder stdErrStringBuilder = new StringBuilder();
		MacCommandResults macCommandResults = new MacCommandResults();
		macCommandResults.setOutput("");
		macCommandResults.setErrorOutput("");
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
			macCommandResults.setSuccess(Boolean.FALSE);
			return macCommandResults;
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
				
				String currentOutput = macCommandResults.getOutput();
				String nextOutput = stdOutStringBuilder.toString();
//				System.out.println("nextoutput: " + nextOutput);
				System.out.println("\n");
				macCommandResults.setOutput(currentOutput + "\n\n" + nextOutput);
				
				// This doesn't work! I fed it "ls -la" with -v as /bin/bash switch, and it outputted "ls -la" in the error stream!!
//				String stdErrString = stdErrStringBuilder.toString();
//				macCommandResults.setErrorOutput(macCommandResults.getErrorOutput() + stdErrString);
//				Boolean success = (stdErrString == null || stdErrString.isEmpty()) ? Boolean.FALSE : Boolean.TRUE;
//				macCommandResults.setSuccess(success);
//				process.
//				if (!success) {
//					return macCommandResults;
//				}
			}
			
			macCommandResults.setSuccess(Boolean.TRUE);
		} 
		catch (Exception e) {
			e.printStackTrace();
			macCommandResults.setSuccess(Boolean.FALSE);
		}
		
		return macCommandResults;
//		
//		String stdOutString = stdOutStringBuilder.toString();
//		String stdErrString = stdErrStringBuilder.toString();
//		Boolean success = (stdErrString == null || stdErrString.isEmpty()) ? Boolean.FALSE : Boolean.TRUE;
//		macCommandResults.setOutput(stdOutString);
//		macCommandResults.setErrorOutput(stdErrString);
//		macCommandResults.setSuccess(success);
//		
//		return macCommandResults;
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
