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
		${pieChartBase}
 	</script> 	
 
	 <script type="text/javascript">
		${pieChartTwo}
 	</script>	
			
	<style>
		${styles}
	</style>
</head>
	<body>
	<div>
		<span class="totals">${wordIncomeTotal}:</span>
		<span class="totals-dat">${incomeTotal!"0"}</span>
	</div>

	<div>
		<span class="totals">${wordExpenseTotal}:</span>
		<span class="totals-dat">${expenseTotal!"0"}</span>
	</div>
	<div>
		<span class="totals">${wordLiabilityTotal}:</span>
		<span class="totals-dat">${liabilityTotal!"0"}</span>
	</div>

	<div class="chartArea">
		<table id="chart1">
			<#list reportDataIncome as row>
				<tr>
					<th>${row.actName}</th>
					<td class="barVal1">${(row.actBalance!0 / incomeTotalRaw)*100}</td>
				</tr>		
			</#list>
		</table>
	
	
		<table id="chart2">
			<#list reportDataExpense as row>
				<tr>
					<th>${row.actName}</th>
					<!-- <td class="barVal1">${(row.actBalance!0 / expenseTotalRaw)*100}</td> -->
					<td class="barVal1">(${row.actBalance!0} / ${expenseTotalRaw})*100</td>
				</tr>		
			</#list>
		</table>
	
		 <input id="chart1Title" type="hidden" value="${wordIncome}">
		 <input id="chart2Title" type="hidden" value="${wordExpenses}">
		
		<span id="holder1"></span>
		<span id="holder2"></span>
	</div>		
									
			<table>
				<tr>
					<th>${wordYearMonth}</th>
					<th>${wordAccountType}</th>
					<th>${wordAccountName}</th>
					<th>${wordAccountBalance} (${reportData[0].isoCode})</th>
				</tr>
				<#list reportData as row>
					<tr>
						<td>${row.yrString}-${row.mth}</td>
						<td>${row.actTypeName}</td>
						<td>${row.actName}</td>
						<td>${row.actBalanceFmt}</td>
					</tr>		
				</#list>
				
			</table>
	</body>
</html>