<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
</head>
<body>
	<h2>${message}</h2>
	<form method="post" action="login">
		<input type="text" name="username"/><br>
		<input type="password" name="password"/><br>
		<input type="submit" value="Submit">
	</form>
</body>
</html>