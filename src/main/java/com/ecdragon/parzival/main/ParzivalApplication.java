package com.ecdragon.parzival.main;

import java.nio.file.Path;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import com.ecdragon.parzival.features.files.output_file.OutputFile;
import com.ecdragon.parzival.features.files.template_file.TemplateFile;

@Controller
@EnableAutoConfiguration 
(
		exclude = {
				HibernateJpaAutoConfiguration.class, 
				DataSourceAutoConfiguration.class, 
				JdbcTemplateAutoConfiguration.class,
				DataSourceTransactionManagerAutoConfiguration.class,
				TransactionAutoConfiguration.class
				})
@ComponentScan(basePackages = {"com.ecdragon.parzival"})
public class ParzivalApplication {
	
	private String groupId;
	private Path groupIdDirectoryPathSegment;
	private Path outputDirectoryRoot;
	private Path templateDirectoryPackagePathXxxxApplication;
	private Path templatesDirectoryRoot;
	private String projectNamePascalCase;
	
	private TemplateFile templateFile;
	private OutputFile outputFile;
	
	public static void main(String[] args) {
		SpringApplication.run(ParzivalApplication.class, args);
	}
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public Path getGroupIdDirectoryPathSegment() {
		return groupIdDirectoryPathSegment;
	}
	public void setGroupIdDirectoryPathSegment(Path groupIdDirectoryPathSegment) {
		this.groupIdDirectoryPathSegment = groupIdDirectoryPathSegment;
	}
	public Path getOutputDirectoryRoot() {
		return outputDirectoryRoot;
	}
	public void setOutputDirectoryRoot(Path outputDirectoryRoot) {
		this.outputDirectoryRoot = outputDirectoryRoot;
	}
	public Path getTemplateDirectoryPackagePathXxxxApplication() {
		return templateDirectoryPackagePathXxxxApplication;
	}
	public void setTemplateDirectoryPackagePathXxxxApplication(Path templateDirectoryPackagePathXxxxApplication) {
		this.templateDirectoryPackagePathXxxxApplication = templateDirectoryPackagePathXxxxApplication;
	}
	public Path getTemplatesDirectoryRoot() {
		return templatesDirectoryRoot;
	}
	public void setTemplatesDirectoryRoot(Path templatesDirectoryRoot) {
		this.templatesDirectoryRoot = templatesDirectoryRoot;
	}
	public TemplateFile getTemplateFile() {
		return templateFile;
	}
	public void setTemplateFile(TemplateFile templateFile) {
		this.templateFile = templateFile;
	}
	public OutputFile getOutputFile() {
		return outputFile;
	}
	public void setOutputFile(OutputFile outputFile) {
		this.outputFile = outputFile;
	}
	public String getProjectNamePascalCase() {
		return projectNamePascalCase;
	}
	public void setProjectNamePascalCase(String projectNamePascalCase) {
		this.projectNamePascalCase = projectNamePascalCase;
	}
}