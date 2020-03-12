/*
 * Copyright (c) 2005-2020 Wayne Gray All rights reserved
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
Raphael.fn.lineChart = function(posX, posY, w, h, values1, labels, title) {

	var paper = this, chartH = h, chartW = w, chartX = posX, chartY = posY + h,
		pathSeg1 = "", pathSeg2 = "";
	
	chart = this.set();

	var seriesColor1 = "rgb(83, 156, 255)";

	
	if (title === undefined) {
		title = "";
	}
	var txtTitle = paper.text(80, 15, title).attr( {
		fill : "rgb(27,95,138)",
		"font-family" : 'Fontin-Sans, Arial',
		"font-size" : "18px"
	});

	paper.rect(chartX, posY, 0.5, chartH);
	paper.rect(chartX, chartY, chartW, 0.5);

	var pointGap = chartW / values1.length;
	var x, y;
	var start = .05, bcolor, color;
	
	var scaleMax = 0;
	for ( var j = 0; j < values1.length; j++) {
		if (values1[j] > scaleMax) {
			scaleMax = values1[j];
		}
		
	}
	
	var scaleMaxOrig = scaleMax;
	var scaleInc = scaleMax / 10;
	scaleInc = Math.round(scaleInc / 100) * 100;
	scaleMax = scaleInc * 10;

	if (scaleMax == 0) {
		if (scaleMaxOrig < 100) {
			scaleMax = 100;
		} else {
			scaleMax = 500;
		}
	}

	process = function(i) {
		var x = chartX + (i * (pointGap));
		var point1H = (values1[i] * chartH) / scaleMax;
		
		var y1 = chartY - point1H;
		
		var x1 = x + (pointGap / 2);
		
		if (i==0) {
			pathSeg1 += "M," + Math.round(x1) + ","  + y1
		} else if (i==1) {
			
			pathSeg1 += " L," + Math.round(x1) + "," + y1
		
		} else {
			pathSeg1 += "," + Math.round(x1) + "," + y1
		}
		
		var pointMarker1 = paper.circle(x1, y1, 5).attr( {
			fill : seriesColor1,
			stroke: seriesColor1
		});
		
		var txt1 = paper.text(x1, y1 - 15, values1[i]);
		txt1.attr("opacity", 0).attr( {
			stroke : "none",
			opacity : 0,
			"font-family" : 'Fontin-Sans, Arial',
			"font-size" : "12px"
		});
		
	
		pointMarker1.mouseover(function(event) {

			pointMarker1.animate( {
				scale : [ 1.01, 1.01, x1, y1 ]
			}, 2000, "elastic");
			txt1.animate( {
				opacity : 1
			}, 2000, "elastic");
		}).mouseout(function(event) {
			pointMarker1.animate( {
				scale : [ 1, 1, x1, y1 ]
			}, 2000, "elastic");
			txt1.animate( {
				opacity : 0
			}, 2000);
		});
		
			
		var t = paper.text(x + (pointGap / 2), chartY + 20, labels[i]);
		t.rotate(55);
		
	};

	for ( var i = 0; i < labels.length; i++) {

		process(i);
	}

	var seriesLine1 = paper.path(pathSeg1).attr({stroke: seriesColor1, "stroke-width": 4, "stroke-linejoin": "round"});
		
	animateElement(seriesLine1);
		
	var scaleInc = scaleMax / 10;
	var scaleVal;

	for ( var k = 1; k < 10; k++) {
		scaleVal = ((k * scaleInc) * chartH) / scaleMax;
		y = chartY - scaleVal;
		var scaleLabelMax = paper
				.text(chartX - 15, y, Math.round(k * scaleInc));
		paper.rect(chartX - 5, y, 10, 1);

	}

	return chart;
};

function animateElement (e) {
	
	e.mouseover(function(event) {

		e.animate( {
			scale : [ 1.01, 1.01, x, y ]
		}, 500, "elastic");
		
	}).mouseout(function(event) {
		e.animate( {
			scale : [ 1, 1, x, y ]
		}, 500, "elastic");
		
	});
	
}
