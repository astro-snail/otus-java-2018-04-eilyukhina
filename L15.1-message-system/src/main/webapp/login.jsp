<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Log In</title>
</head>

<body>
	<div class="content">
		<div>
    		<img src="img/spring-logo.png"/>
		</div>
		
		<h3>Please enter your user name and password</h3>
		
		<h4 class="error">${message}</h4>
		
		<form method="post" action="login">
			<table>
				<tr>
					<td><label for="username">User name</label></td>
					<td><input type="text" id="username" name="username"/></td>				
				</tr>
				<tr>
					<td><label for="password">Password</label></td>
					<td><input type="password" id="password" name="password"/></td>				
				</tr>
				<tr>
					<td><a class="button secondary" href="${pageContext.request.contextPath}">Back</a></td>
					<td><input class="button primary" type="submit" value="Log In"></td>
				</tr>
			</table>
		</form>
	</div>
</body>

</html>