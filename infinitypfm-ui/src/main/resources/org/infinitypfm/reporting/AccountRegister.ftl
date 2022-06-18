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
		<h2>${account}</h2>
			
<div class="tblWrapper">									
		<table id="tblRegister">
			<tr>
				<th>${wordTranDate}</th>
				<th>${wordMemo}</th>
				<th>${wordDebit}</th>
				<th>${wordCredit}</th>
			</tr>
			<#list reportData as row>
				<tr>
					<td>${row.transactionDateFormatted}</td>
					<td>${row.memo}</td>
					<td>${row.debitFormatted}</td>
					<td>${row.creditFormatted}</td>
				</tr>		
			</#list>
			
		</table>
</div>
	</body>
</html>