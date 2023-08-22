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
		
		#tblPlanner  {
		width:90%;
		}
		
	</style>
</head>
	<body>
		<section>	
			<div class="tblWrapper">								
					<table>
						<tr>
							<th>${wordAge}</th>
							<th>${wordBalance}</th>
							<th>${wordDistribution}</th>
							<th>${wordEarnInvest}</th>
							<th>${wordIncome}</th>
							<th>${wordContributions}</th>
							<th>${wordIncomeTaxes}</th>
							<th>${wordNetEarnings}</th>
						</tr>
						<#if reportData??>
						<#list reportData as row>
							<tr>
								<td>${row.age}</td>
								<td>${row.remainingFormatted}</td>
								<td>${row.drawFormatted}</td>
								<td>${row.earnInvestFormatted}</td>
								<td>${row.earnWageFormatted}</td>
								<td>${row.contributionFormatted}</td>
								<td>${row.taxFormatted}</td>
								<td>${row.netEarningsFormatted}</td>
							</tr>		
						</#list>
						</#if>
						
					</table>
			</div>
		</section>
	</body>
</html>