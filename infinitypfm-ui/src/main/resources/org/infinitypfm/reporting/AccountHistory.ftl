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
	<#if reportData??>
	<#list reportData as row>
		<tr>
			<th>${row.yrString}-${row.mth}</th>
			<td class="barVal1">${row.actBalanceFmt}</td>
		</tr>		
	</#list>
	</#if>
</table>

<span id="holder1"></span>
<div class="tblWrapper">									
		<table>
			<tr>
				<th>${wordYearMonth}</th>
				<th>${wordAccountBalance} (<#if reportData[0]??> ${reportData[0].isoCode}</#if>)</th>
			</tr>
			<#if reportData??>
			<#list reportData as row>
				<tr>
					<td>${row.yrString}-${row.mth}</td>
					<td>${row.actBalanceFmt}</td>
				</tr>		
			</#list>
			</#if>
		</table>
</div>
	</body>
</html>