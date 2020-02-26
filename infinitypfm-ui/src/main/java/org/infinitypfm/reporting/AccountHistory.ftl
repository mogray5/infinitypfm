<html>
<head>
 <title>${title}</title>

	<script type="text/javascript">
		${raphael}
 	</script>

	<script type="text/javascript">
		${jquery}
 	</script> 	
 	
	<script type="text/javascript">
		${barchartbase}
 	</script> 	
 
 	<script type="text/javascript">
		${barchart}
 	</script>	
	
	<style>
		${styles}
	</style>
</head>
	<body>
		<h3 style="color:#2f7991;">Account History</h3>
		<input id="chart1Title" type="hidden" value="Credit Card">
			<table id="chart1">
				<#list chartrows as chartrow>
					<tr>
						<th>${chartrow.xlabel}</th>
						<td class="barVal">${chartrow.xval}</td>
					</tr>
				</#list>
			</table>
			
			<span id="holder1"></span>
			
			<table border="1" cellspacing="1" style="width:100%;background-color:#004f78;font-size:12px;">
				<tr>
					<td style="color:white;">Year-Month</td>
					<td style="color:white;">Account Balance</td>
					<td style="color:white;">Currency</td>
					<td style="color:white;">Account Name</td>
				</tr>
				<#list reportrows as row>
					<tr style="background-color:#ffffff;color:#000000;">
						<td>${row.val1}</td>
						<td>${row.val2}</td>
						<td>${row.val3}</td>
						<td>${row.val4}</td>
					</tr>		
				</#list>
				
			</table>
	</body>
</html>