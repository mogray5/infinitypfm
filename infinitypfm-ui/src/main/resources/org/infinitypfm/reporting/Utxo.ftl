<html>
<head>

 <h1>${title}</h1>

	<script type="text/javascript">
		${raphael}
 	</script>

	<script type="text/javascript">
		${jslib}
 	</script> 	
	
	<style>
		${styles}
		
		#tblRegister  {
		width:90%;
		}
		
	</style>
</head>
	<body>
			
<div class="tblWrapper">			
		<table id="tblRegister">
				
		<#list reportData as row>
		<tr>
			<td>${wordHeight}</td>
			<td>${row.height}</td>
		</tr>
		<tr>
			<td>${wordPosition}</span>
			<td>${row.position}</span>
		</tr>
		<tr>
			<td>${wordHash}</span>
			<td>${row.hash}</span>
		</tr>
		<tr>
			<td>${wordValue}</span>
			<td>${row.value}</span>
		</tr>
		<tr>
			<td>${wordScript}</span>
			<td>${row.script}</span>
		</tr>
		<tr>
			<td>${wordPath}</span>
			<td>${row.path}</span>
		</tr>
		<tr>
			<td>${wordAddress}</span>
			<td>${row.address}</span>
		</tr>
		<tr>
			<td><br/></span>
			<td><br/></span>
		</tr>
		</#list>
	</table>				
</div>
	</body>
</html>