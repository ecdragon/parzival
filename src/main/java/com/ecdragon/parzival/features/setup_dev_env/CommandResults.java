package com.ecdragon.parzival.features.setup_dev_env;

public class CommandResults {

	private String output;
	private String errorOutput;
	private Boolean success;
	
	public void prettySysOut() {
		System.out.println("success: " + (success == null ? "false" : success.toString()));
		System.out.println("stdOut: " + output);
	}
	
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getErrorOutput() {
		return errorOutput;
	}
	public void setErrorOutput(String errorOutput) {
		this.errorOutput = errorOutput;
	}
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
}
