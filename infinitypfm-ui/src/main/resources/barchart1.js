/*
 * Copyright (c) 2005-2013 Wayne Gray All rights reserved
 * 
 * This file is part of Infinity PFM.
 * 
 * Infinity PFM is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Infinity PFM is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Infinity PFM.  If not, see <http://www.gnu.org/licenses/>.
*/
(function (raphael) {
    $(function () {
        
    	var values1 = [],
            labels1 = [],
            guideLineVals = [],
            guideLineLbl;
        
    	guideLineLbl = $("#chart1").attr("guideLineLbl");
    	
        $("#chart1 tr").each(function () {
            values1.push(parseInt($("td.barVal", this).text().replace(/,/g, ''), 10));
            labels1.push($("th", this).text());
            
            if ($("td.guideVal").length > 0) {
            	guideLineVals.push(parseInt($("td.guideVal", this).text().replace(/,/g, ''), 10));
            }
            
        });
        $("#chart1").hide();
              
        var chart1Title = $("#chart1Title").val();
        
        raphael("holder1", 650, 350).barChart(100,50,550,250,values1, labels1, chart1Title, guideLineLbl, guideLineVals);
         

    });
})(Raphael.ninja());
