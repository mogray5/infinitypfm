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
		${barChartBase}
 	</script> 	
 
 	<script type="text/javascript">
		${barChartOne}
 	</script>	
	
	<style>
		${styles}
	</style>
</head>
	<body>
		<h3>Using Budget: ${reportData[0].budgetName}</h3>
	

<table id="chart1">
	<#list reportData as row>
		<tr>
			<th>${row.yrString}-${row.mth}</th>
			<td class="barVal">${row.expenseBalanceFmt!0}</td>
			<td class="guideVal">${row.budgetBalanceFmt!0}</td>
		</tr>		
	</#list>
</table>


<span id="holder1"></span>
<div class="tblWrapper">									
			<table>
				<tr>
					<th>${wordYearMonth}</th>
					<th>${wordExpenseBalance}</th>
					<th>${wordBudgetBalance}</th>
					<th>${wordBudgetName}</th>
				</tr>
				<#list reportData as row>
					<tr>
						<td>${row.yrString}-${row.mth}</td>
						<td>${row.expenseBalanceFmt!0}</td>
						<td>${row.budgetBalanceFmt!0}</td>
						<td>${row.budgetName}</td>
					</tr>		
				</#list>
				
			</table>
</div>
	</body>
</html>