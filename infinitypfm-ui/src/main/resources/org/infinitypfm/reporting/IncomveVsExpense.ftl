<html>
<head>
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
	<article>
	<header>
		<h1>${title}</h1>
	</header>
	<main>
		<div class="chartArea">
			<table id="chart1">
				<#if reportData??>
				<#list reportData as row>
					<tr>
						<th>${row.yrString}-${row.mth}</th>
						<td class="barVal1">${row.incomeBalanceFmt!0}</td>
						<td class="barVal2">
						<#if row.liabilityPlusExpenseBalance lt 0>
							0
						<#else>
							${row.liabilityPlusExpenseBalanceFmt!0}
						</#if>
						</td>
					</tr>		
				</#list>
				</#if>
			</table>
			
			
			<span id="holder1"></span>
		</div>	
		<div class="tblWrapper">									
					<table>
						<tr>
							<th>${wordYearMonth}</th>
							<th>${wordIncomeBalance}</th>
							<th>${wordLiabilityBalance}</th>
							<th>${wordExpenseBalance}</th>
						</tr>
						<#if reportData??>
						<#list reportData as row>
							<tr>
								<td>${row.yrString}-${row.mth}</td>
								<td>${row.incomeBalanceFmt!0}</td>
								<td>${row.liabilityBalanceFmt!0}</td>
								<td>${row.expenseBalanceFmt!0}</td>
							</tr>		
						</#list>
						</#if>
					</table>
		</div>
		</main>
	</article>
	</body>
</html>