<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>User Information</title>
</head>

<body>
	<div class="content">
		<div>
    		<img src="img/spring-logo.png"/>
		</div>
		<h3>User</h3>
		<table>
			<tr>
    			<th>Attribute</th>
    			<th>Value</th>
			</tr>
			<tr>
    			<td>User ID</td>
    			<td>${id}</td>
			</tr>
			<tr>
    			<td>Name</td>
    			<td>${name}</td>
			</tr>
			<tr>
    			<td>Address</td>
    			<td>${streetAddress}</td>
			</tr>
			<tr>
    			<td>Phone Number</td>
    			<td>${phoneNumber}</td>
			</tr>
		</table>
		<a class="button" href="user-info">Refresh</a>
	</div>
</body>

</html>
