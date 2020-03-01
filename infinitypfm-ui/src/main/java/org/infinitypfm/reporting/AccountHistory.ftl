<html>
<head>
 <title>${title}</title>

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
		<h3 style="color:#2f7991;">Account History</h3>
		<input id="chart1Title" type="hidden" value="Credit Card">

			<span id="holder1"></span>
			
			<table border="1" cellspacing="1" style="width:100%;background-color:#004f78;font-size:12px;">
				<tr>
					<td style="color:white;">Year-Month</td>
					<td style="color:white;">Account Balance</td>
					<td style="color:white;">Currency</td>
					<td style="color:white;">Account Name</td>
				</tr>
				<#list reportData as row>
					<tr style="background-color:#ffffff;color:#000000;">
						<td>${row.actName}</td>
						<td>${row.actBalance}</td>
					</tr>		
				</#list>
				
			</table>
	</body>
</html>