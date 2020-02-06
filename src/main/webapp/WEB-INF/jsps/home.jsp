<html>

	<head>
		
	</head>
	
	<body>
	
		<hr>
		<p><a href="/">GO HOME</a></p>
		<p>Test Something<br></p>
		<form action="/api/main/test" 
				enctype="multipart/form-data" 
				method="post">
			<!--<p>Select a file to input: <input type="file" name="file"/></p>-->
			<p>Input: <input type="text" name="input" size="150" value="${input}"></p>
			<p><input type="submit" value="Test Me"></p>
		</form>
		<hr>
		<p>Results:</p>
${previousResults}

	</body>
	
</html>