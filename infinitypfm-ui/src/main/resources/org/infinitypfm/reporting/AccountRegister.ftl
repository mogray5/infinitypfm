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
	<article>
		<header>
			<div>
				<span class="totals">${wordAccountName}:</span>
				<span class="totals-dat">${account}</span>
			</div>
			<div>
				<span class="totals">${wordAccountTotal}:</span>
				<span class="totals-dat">${accountTotal!"0"}</span>
			</div>
		<br/>
		</header>
		
	<div class="tblWrapper">									
		<table id="tblRegister">
			<tr>
				<th>${wordTranDate}</th>
				<th>${wordMemo}</th>
				<th>${wordDebit}</th>
				<th>${wordCredit}</th>
			</tr>
			<#if reportData??>
			<#list reportData as row>
				<tr>
					<td>${row.transactionDateFormatted}</td>
					<td>${row.memo}</td>
					<td>${row.debitFormatted}</td>
					<td>${row.creditFormatted}</td>
				</tr>		
			</#list>
			</#if>
			
		</table>
	</div>
	</article>
	</body>
</html>