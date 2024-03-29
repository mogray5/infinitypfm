/*
 * Copyright (c) 2005-2011 Wayne Gray All rights reserved
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
            values2 = [],
            labels2 = [];
        $("#chart1 tr").each(function () {
            values1.push(parseInt($("td", this).text().replace(/,/g, ''), 10));
            labels1.push($("th", this).text());
        });
        $("#chart1").hide();
       
        $("#chart2 tr").each(function () {
        	values2.push(parseInt($("td", this).text().replace(/,/g, ''), 10));
        	labels2.push($("th", this).text());
        });
        $("#chart2").hide();
       
        var chart1Title = $("#chart1Title").val();
        var chart2Title = $("#chart2Title").val();
        raphael("holder2", 375, 275).pieChart(180, 135, 85, values2, labels2, "#7995b7", chart2Title);
        raphael("holder1", 375, 275).pieChart(180,135, 85, values1, labels1, "#7995b7", chart1Title);
        
        
        
    });
})(Raphael.ninja());
