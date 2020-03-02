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
		${lineChartBase}
 	</script> 	
 
 	<script type="text/javascript">
		${lineChartOne}
 	</script>	
	
	<style>
		${styles}
	</style>
</head>
	<body>
		<input id="chart1Title" type="hidden" value="Credit Card">

			<span id="holder1"></span>
			
			<table>
				<tr>
					<td>Year-Month</td>
					<td>Account Balance</td>
					<td>Currency</td>
				</tr>
				<#list reportData as row>
					<tr>
						<td>${row.yr}-${row.mth}</td>
						<td>${row.actBalance}</td>
					</tr>		
				</#list>
				
			</table>
	</body>
</html>