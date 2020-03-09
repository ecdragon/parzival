package com.ecdragon.parzival.features.maven;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MavenCoordinates implements 
		Comparable<MavenCoordinates> {

	private String groupId;
	private String artifactId;
	private String version;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getArtifactId() {
		return artifactId;
	}
	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	@Override
	public boolean equals(Object rhsObj) {
		if (rhsObj == null || !(rhsObj instanceof MavenCoordinates)) {
			return false;
		}
		MavenCoordinates rhs = (MavenCoordinates) rhsObj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.getArtifactId(), rhs.getArtifactId());
		equalsBuilder.append(this.getGroupId(), rhs.getGroupId());
		equalsBuilder.append(this.getVersion(), rhs.getVersion());
		boolean isEquals = equalsBuilder.isEquals();
		return isEquals;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.getArtifactId());
		hashCodeBuilder.append(this.getGroupId());
		hashCodeBuilder.append(this.getVersion());
		Integer hashCode = hashCodeBuilder.build();
		return hashCode;
	}
	
	@Override
	public int compareTo(MavenCoordinates o) {
		if (o == null) {
			throw new NullPointerException("Null argument passed into MavenCoordinates::compareTo method.");
		}
		int groupCompare = compareStringToStringNullLow(this.getGroupId(), o.getGroupId());
		if (groupCompare != 0) {
			return groupCompare;
		}
		int artifactIdCompare = compareStringToStringNullLow(this.getArtifactId(), o.getArtifactId());
		if (artifactIdCompare != 0) {
			return artifactIdCompare;
		}
		int versionCompare = compareStringToStringNullLow(this.getVersion(), o.getVersion());
		return versionCompare;
	}
	
	public static void main2(String[] args) {
		// Prints "test - 9"
		System.out.println("test - " + "onestring".compareTo(""));
		// Throws a NPE!!!
		System.out.println("test - " + "onestring".compareTo(null));
	}
	
	public static int compareStringToStringNullLow(String lhs, String rhs) {
		if (lhs == null) {
			lhs = "";
		}
		if (rhs == null) {
			rhs = "";
		}
		return lhs.compareTo(rhs);
	}
			
	public String convertToGradleDependencyString() {
		String result = null;
		if (this.getGroupId() != null && this.getArtifactId() != null) {
			StringBuilder gradleDependencyStringBuilder = new StringBuilder();
			gradleDependencyStringBuilder.append("group: '");
			gradleDependencyStringBuilder.append(this.getGroupId());
			gradleDependencyStringBuilder.append("', name: '");
			gradleDependencyStringBuilder.append(this.getArtifactId());
			if (this.getVersion() != null && !this.getVersion().isEmpty()) {
				gradleDependencyStringBuilder.append("', version: '");
				gradleDependencyStringBuilder.append(this.getVersion());
			}
			gradleDependencyStringBuilder.append("'");
			result = gradleDependencyStringBuilder.toString();
		}
		return result;
	}
			
	public static void main(String[] args) {
		MavenCoordinates mc1 = new MavenCoordinates();
//		mc1.setArtifactId(null);
		MavenCoordinates mc2 = new MavenCoordinates();
		boolean areEquals = mc1.equals(mc2);
		System.out.println("equal? : " + areEquals);
		mc2.setArtifactId("art1");
		areEquals = mc1.equals(mc2);
		System.out.println("equal? : " + areEquals);
		
		System.out.println("Should throw NPE: ");
		Integer i1 = 4;
		Integer i2 = null;
		try {
			i1.compareTo(i2);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Should throw NPE: ");
		MavenCoordinates mc3 = null;
		try {
			mc1.compareTo(mc3);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		mc1.setArtifactId("artifact1");
		mc2.setArtifactId("artifact1");
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be 0: " + (mc1.compareTo(mc2)));
		
		mc1.setArtifactId("artifact2");
		mc2.setArtifactId("artifact1");
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be >1: " + (mc1.compareTo(mc2)));
		
		mc1.setArtifactId("artifact1");
		mc2.setArtifactId("artifact2");
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be <1: " + (mc1.compareTo(mc2)));
		
		mc1.setArtifactId(null);
		mc2.setArtifactId("artifact2");
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be <1: " + (mc1.compareTo(mc2)));
		
		mc1.setArtifactId("artifact1");
		mc2.setArtifactId(null);
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be >1: " + (mc1.compareTo(mc2)));
		
		mc1.setArtifactId(null);
		mc2.setArtifactId(null);
		mc1.setGroupId("group1");
		mc2.setGroupId("group1");
		mc1.setVersion(null);
		mc2.setVersion(null);
		System.out.println("should be 0: " + (mc1.compareTo(mc2)));
		
		System.out.println("Need version tests!!!");
	}
}
