<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<link rel="stylesheet" type="text/css" href="css/styles.css">
<title>Message System</title>
</head>

<body>
	<div class="content">
		<div>
			<img src="img/spring-logo.png" />
		</div>
		<h3>${message}</h3>
		<ul>
			<li><a class="button primary" href="login">Log In</a></li>
			<li><a class="button primary" href="user-list">User List</a></li>
			<li><a class="button primary" href="cache-info">Cache Info</a></li>
			<li><a class="button primary" href="logout">Log Out</a></li>
		</ul>
	</div>
</body>

</html>