<html>
<head>
 <h1>${title}</h1>

	<script type="text/javascript">
		${raphael}
 	</script>

	<script type="text/javascript">
		${jslib}
 	</script> 	
 	
	<script type="text/javascript">
		${lineChartBaseSingle}
 	</script> 	
 
 	<script type="text/javascript">
		${lineChartSingleOne}
 	</script>	
	
	<style>
		${styles}
	</style>
</head>
	<body>
		<h2>${account}</h2>
	

<table id="chart1">
	<#list reportData as row>
		<tr>
			<th>${row.yr}-${row.mth}</th>
			<td class="barVal1">${row.actBalanceFormattted}</td>
		</tr>		
	</#list>
</table>


<span id="holder1"></span>
									
			<table>
				<tr>
					<th>Year-Month</td>
					<th>Account Balance</td>
				</tr>
				<#list reportData as row>
					<tr>
						<td>${row.yr}-${row.mth}</td>
						<td>${row.actBalanceFormattted}</td>
					</tr>		
				</#list>
				
			</table>
	</body>
</html>