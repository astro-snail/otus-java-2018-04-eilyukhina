<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>User List</title>
</head>

<body>
	<div class="content">
		<div>
    		<img src="img/spring-logo.png"/>
		</div>
		<h3>Users</h3>
		<c:forEach items="${users}" var="user">
    		${user}<br>
		</c:forEach>
		<a class="button" href="user-list">Refresh</a>
	</div>
</body>

</html>
