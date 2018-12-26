<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8" />
<link rel="stylesheet" type="text/css" href="css/styles.css">
<title>User Information</title>
</head>

<body>
	<div class="content">
		<div>
			<img src="img/spring-logo.png" />
		</div>
		<h3>User ${user.name}</h3>
		<h4 class="information">${message}</h4>
		<form action="user-info" method="POST">

			<input type="hidden" name="operation" value="${operation}" /> <input
				type="hidden" name="userId" value="${user.id}" /> <input
				type="hidden" name="addressId" value="${user.address.id}" />

			<table>
				<tbody>
					<tr>
						<td><label for="name">Name</label></td>
						<td><input type="text" id="name" name="name"
							value="${user.name}" /></td>
					</tr>
					<tr>
						<td><label for="age">Age</label></td>
						<td><input type="text" id="age" name="age"
							value="${user.age}" /></td>
					</tr>
					<tr>
						<td><label for="address">Address</label></td>
						<td><input type="text" id="address" name="address"
							value="${user.address.street}" /></td>
					</tr>
					<c:forEach var="phone" items="${user.phones}">
						<input type="hidden" name="phoneId" value="${phone.id}" />
						<tr>
							<td><label for="phone_${phone.id}">Phone Number</label></td>
							<td><input type="text" id="phone_${phone.id}" name="phone"
								value="${phone.number}" /><br></td>
						</tr>
					</c:forEach>
					<tr>
						<td><a class="button secondary" href="user-list">Back</a></td>
						<td><input class="button primary" type="submit"
							value="${operation.name}" /></td>
					</tr>
				</tbody>
			</table>

		</form>

	</div>
</body>

</html>
