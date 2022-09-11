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

	<div>
		<span class="totals">${wordAccountTotal}:</span>
		<span class="totals-dat">${accountTotal!"0"}</span>
	</div>
			
<table id="chart1">
	<#list reportData as row>
		<tr>
			<th>${row.yrString}-${row.mth}</th>
			<td class="barVal1">${row.actBalanceFmt}</td>
		</tr>		
	</#list>
</table>

<span id="holder1"></span>
<div class="tblWrapper">									
		<table>
			<tr>
				<th>${wordYearMonth}</th>
				<th>${wordAccountBalance} (${reportData[0].isoCode})</th>
			</tr>
			<#list reportData as row>
				<tr>
					<td>${row.yrString}-${row.mth}</td>
					<td>${row.actBalanceFmt}</td>
				</tr>		
			</#list>
			
		</table>
</div>
	</body>
</html>