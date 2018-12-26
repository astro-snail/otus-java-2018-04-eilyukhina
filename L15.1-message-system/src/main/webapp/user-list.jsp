<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<link rel="stylesheet" type="text/css" href="css/styles.css">
<title>User List</title>
</head>

<body>
	<div class="content">
		<div>
			<img src="img/spring-logo.png" />
		</div>

		<h3>User List</h3>

		<h4 class="information">${message}</h4>

		<!-- Set up an insert link -->
		<c:url var="insertLink" value="user-info">
			<c:param name="operation" value="SAVE"></c:param>
			<c:param name="userId" value=""></c:param>
		</c:url>

		<!-- List of users -->
		<table>
			<tr>
				<th width="5%">ID</th>
				<th width="25%">Name</th>
				<th width="5%">Age</th>
				<th width="25%">Address</th>
				<th width="20%">Phone</th>
				<th width="20%" colspan="2">Action</th>
			</tr>

			<c:forEach var="user" items="${users}">

				<!-- Set up an update link -->
				<c:url var="updateLink" value="user-info">
					<c:param name="operation" value="SAVE"></c:param>
					<c:param name="userId" value="${user.id}"></c:param>
				</c:url>

				<!-- Set up a delete link -->
				<c:url var="deleteLink" value="user-info">
					<c:param name="operation" value="DELETE"></c:param>
					<c:param name="userId" value="${user.id}"></c:param>
				</c:url>

				<tr>
					<td>${user.id}</td>
					<td>${user.name}</td>
					<td>${user.age}</td>
					<td>${user.address.street}</td>
					<td><c:forEach var="phone" items="${user.phones}">
							${phone.number}<br>
						</c:forEach></td>
					<td><a href="${updateLink}">Update</a></td>
					<td><a href="${deleteLink}">Delete</a></td>
				</tr>
			</c:forEach>
		</table>
		<table>
			<tr>
				<td><a class="button secondary"
					href="${pageContext.request.contextPath}">Back</a></td>
				<td><a class="button primary" href="${insertLink}">Add User</a></td>
			</tr>
		</table>
	</div>
</body>

</html>
