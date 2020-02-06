<html>

	<head>
		
	</head>
	
	<body>
	
		<hr>
		<p>Transform a POM File<br></p>
		<form action="/api/mavenPom/transformMavenPomXmlFileToDependenciesGradleFile" 
				enctype="multipart/form-data" 
				method="post">
			<p>Select a POM file to transform: <input type="file" name="file"/></p>
			<%-- <p>Input: <input type="text" name="input" size="150" value="${input}"></p> --%>
			<p><input type="submit" value="Transform POM"></p>
		</form>
		<hr>

	</body>
	
</html>