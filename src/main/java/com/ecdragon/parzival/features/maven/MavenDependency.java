package com.ecdragon.parzival.features.maven;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MavenDependency
		implements Comparable<MavenDependency> {

	private MavenCoordinates mavenCoordinates;
	private MavenDependencyScopeEnum mavenScope;
	private Boolean isManaged = Boolean.FALSE;
	
	public void appendAsChildElement(Document document, Element elementToAppendTo) {
		
		try {
			
			if (this.mavenCoordinates == null 
					|| this.mavenCoordinates.getGroupId() == null
					|| this.mavenCoordinates.getGroupId().isEmpty()
					|| this.mavenCoordinates.getArtifactId() == null
					|| this.mavenCoordinates.getArtifactId().isEmpty()
					) {
				return;
			}
			
			// dependency element
			Element elementDependency = document.createElement("dependency");
			elementToAppendTo.appendChild(elementDependency);

			// add group id element
			Element elementGroupId = document.createElement("groupId");
			elementGroupId.appendChild(document.createTextNode(this.mavenCoordinates.getGroupId()));
			elementDependency.appendChild(elementGroupId);
			
			// add artifact id element
			Element elementArtifactId = document.createElement("artifactId");
			elementArtifactId.appendChild(document.createTextNode(this.mavenCoordinates.getArtifactId()));
			elementDependency.appendChild(elementArtifactId);
			
			if (this.mavenCoordinates.getVersion() != null
					&& !this.mavenCoordinates.getVersion().isEmpty()
					) {
				
				// add version element
				Element elementVersion = document.createElement("version");
				elementVersion.appendChild(document.createTextNode(this.mavenCoordinates.getVersion()));
				elementDependency.appendChild(elementVersion);
			}
			
			if (this.getMavenScope() != null) {
				
				// add scope element
				Element elementScope = document.createElement("scope");
				elementScope.appendChild(document.createTextNode(this.getMavenScope().getXmlTextValue()));
				elementDependency.appendChild(elementScope);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public String convertToGradleDependencyString() {
		String result = null;
		StringBuilder gradleDependencyStringBuilder = new StringBuilder();
		if (this.getMavenScope() == null || this.getMavenScope() == MavenDependencyScopeEnum.compile) {
			gradleDependencyStringBuilder.append("compile ");
		}
		else {
			gradleDependencyStringBuilder.append("testCompile ");
		}
		String mavenCoordinatesGradleDependencyString = this.getMavenCoordinates().convertToGradleDependencyString();
		if (mavenCoordinatesGradleDependencyString == null) {
			return null;
		}
		gradleDependencyStringBuilder.append(mavenCoordinatesGradleDependencyString);
		result = gradleDependencyStringBuilder.toString();
		return result;
	}
	
	@Override
	public boolean equals(Object rhsObj) {
		if (rhsObj == null || !(rhsObj instanceof MavenCoordinates)) {
			return false;
		}
		MavenDependency rhs = (MavenDependency) rhsObj;
		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(this.getMavenCoordinates(), rhs.getMavenCoordinates());
		boolean isEquals = equalsBuilder.isEquals();
		return isEquals;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(this.getMavenCoordinates());
		Integer hashCode = hashCodeBuilder.build();
		return hashCode;
	}

	@Override
	public int compareTo(MavenDependency o) {
		if (o == null 
				|| o.getMavenCoordinates() == null
				|| this.getMavenCoordinates() == null
				) {
			throw new NullPointerException("Null argument passed into MavenDependency::compareTo method.");
		}
		int scopeCompare = 
				MavenCoordinates.compareStringToStringNullLow(
						(this.mavenScope == null ? null : this.mavenScope.getXmlTextValue()), 
						(o.getMavenScope() == null ? null : o.getMavenScope().getXmlTextValue()));
		if (scopeCompare != 0) {
			return scopeCompare;
		}
		int compareCoordinatesResult = this.getMavenCoordinates().compareTo(o.getMavenCoordinates());
		return compareCoordinatesResult;
	}
	
	public MavenCoordinates getMavenCoordinates() {
		return mavenCoordinates;
	}
	public void setMavenCoordinates(MavenCoordinates mavenCoordinates) {
		this.mavenCoordinates = mavenCoordinates;
	}
	public MavenDependencyScopeEnum getMavenScope() {
		return mavenScope;
	}
	public void setMavenScope(MavenDependencyScopeEnum mavenScope) {
		this.mavenScope = mavenScope;
	}
	public Boolean getIsManaged() {
		return isManaged;
	}
	public void setIsManaged(Boolean isManaged) {
		this.isManaged = isManaged;
	}
}
