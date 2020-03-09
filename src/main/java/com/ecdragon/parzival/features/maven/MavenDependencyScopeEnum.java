package com.ecdragon.parzival.features.maven;

import java.util.EnumSet;

public enum MavenDependencyScopeEnum {

	compile("compile"),
	provided("provided"),
	runtime("runtime"),
	test("test"),
	_import("import")
	;
	
	private static EnumSet<MavenDependencyScopeEnum> allThese = EnumSet.allOf(MavenDependencyScopeEnum.class);
	public static EnumSet<MavenDependencyScopeEnum> getAllThese() {
		return allThese;
	}
	
	private String xmlTextValue;
	public String getXmlTextValue() {
		return xmlTextValue;
	}
	
	public static MavenDependencyScopeEnum findByXmlTextValue(String xmlTextValue) {
		if (xmlTextValue == null) {
			return null;
		}
		for (MavenDependencyScopeEnum mavenDependencyScopeEnum : allThese) {
			if (mavenDependencyScopeEnum.getXmlTextValue().equalsIgnoreCase(xmlTextValue)) {
				return mavenDependencyScopeEnum;
			}
		}
		return null;
	}
	
	private MavenDependencyScopeEnum(String xmlTextValue) {
		this.xmlTextValue = xmlTextValue;
	}
}
