<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8"/>
    <link rel="stylesheet" type="text/css" href="css/styles.css">
    <title>Cache Information</title>
</head>

<body>
	<div class="content">
		<div>
    		<img src="img/spring-logo.png"/>
		</div>
		
		<h3>Cache Parameters</h3>
		
		<table class="border">
			<tr>
    			<th width="50%" class="border">Attribute</th>
    			<th width="50%" class="border">Value</th>
			</tr>
			<tr>
    			<td class="border">Max.capacity</td>
    			<td class="border">${cacheParameters["maxCapacity"]}</td>
			</tr>
			<tr>
    			<td class="border">Life time</td>
    			<td class="border">${cacheParameters["lifeTime"]}</td>
			</tr>
			<tr>
    			<td class="border">Idle time</td>
    			<td class="border">${cacheParameters["idleTime"]}</td>
			</tr>
			<tr>
    			<td class="border">Eternal</td>
    			<td class="border">${cacheParameters["isEternal"]}</td>
			</tr>
			<tr>
    			<td class="border">Elements in cache</td>
    			<td class="border">${cacheParameters["size"]}</td>
			</tr>
			<tr>
    			<td class="border">Hit count</td>
    			<td class="border">${cacheParameters["hitCount"]}</td>
			</tr>
			<tr>
    			<td class="border">Miss count</td>
    			<td class="border">${cacheParameters["missCount"]}</td>
			</tr>
		</table>
		<table>
			<tr>
				<td><a class="button secondary" href="${pageContext.request.contextPath}">Back</a></td>
				<td><a class="button primary" href="cache-info">Refresh</a></td>
			</tr>
		</table>
	</div>
</body>

</html>
