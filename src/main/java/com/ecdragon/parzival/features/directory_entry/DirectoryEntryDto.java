package com.ecdragon.parzival.features.directory_entry;

import java.io.File;

public class DirectoryEntryDto {

	private String name;
	
	public DirectoryEntryDto() {
		// default no-arg constructor
	}
	
	public DirectoryEntryDto(String name) {
		this.name = name;
	}
	
	public static DirectoryEntryDto convertFromStringNameToDirectoryEntryDto(String name) {
		DirectoryEntryDto directoryEntryDto = new DirectoryEntryDto() {{
			setName(name);
		}};
		return directoryEntryDto;
	}
	
	public static DirectoryEntryDto convertFromFileToDirectoryEntryDto(File file) {
		if (file == null) {
			return null;
		}
		try {
			DirectoryEntryDto directoryEntryDto = new DirectoryEntryDto() {{
				setName(file.getName());
			}};
			return directoryEntryDto;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
