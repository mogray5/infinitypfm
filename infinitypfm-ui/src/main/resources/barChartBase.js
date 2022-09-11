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
Raphael.fn.barChart = function(posX, posY, w, h, values, labels, title, guidelineLabel, guidelineVals) {

	var paper = this, chartH = h, chartW = w, chartX = posX, chartY = posY + h, barGap = 5,
		pathSeg = "";
	chart = this.set();

	if (title != undefined) {
		var txtTitle = paper.text(80, 15, title).attr( {
			fill : "rgb(27,95,138)",
			"font-family" : 'Fontin-Sans, Arial',
			"font-size" : "18px"
		});
	}
	paper.rect(chartX, posY, 0.5, chartH);
	paper.rect(chartX, chartY, chartW, 0.5);

	var barW = (chartW - (barGap * values.length)) / values.length;
	var barH;
	var guideX, guideY;
	var x, y;
	var start = .05, bcolor, color, 
		warnColor = "rgb(253, 118, 143)", 
		warnBColor = "rgb(253, 118, 143)";
	var hasGuide = guidelineVals.length > 0;
	var scaleMax = 0;
	for ( var j = 0; j < values.length; j++) {
		if (values[j] > scaleMax) {
			scaleMax = values[j];
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
		var x = chartX + (i * (barW + barGap));
		var barH = (values[i] * chartH) / scaleMax;
		var y = chartY - barH;
		
		
		if (hasGuide) {
			
			if (Math.round(guidelineVals[i]) < Math.round(values[i])) {
				
				bcolor = warnColor;
				color = warnBColor;
				
			} else {
			
				bcolor = "rgb(83, 156, 255)";
				color = "rgb(83, 156, 255)";
			}
			
		} else {
			
			bcolor = "hsb(" + start + ", 1, 1)";
			color = "hsb(" + start + ", 1, .5)";
		}
		
		// alert("x:" + x + " y:" + y + " barW:" + barW + " barH:" + barH);
		var r = paper.rect(x, y, barW, barH).attr( {
			gradient : "90-" + bcolor + "-" + color
		});
		
		var txt = paper.text(x + barW / 2, y - 5, values[i]);
		txt.attr("opacity", 0).attr( {
			//fill : bcolor,
			stroke : "none",
			opacity : 0,
			"font-family" : 'Fontin-Sans, Arial',
			"font-size" : "12px"
		});
		
		if (hasGuide) {
			
			guideY = (guidelineVals[i] * chartH) / scaleMax;
			guideY = chartY - guideY;
			guideX = x;
			
			if (i==0) {
				pathSeg += "M," + x + ","  + guideY + " L," + Math.round(x+barW) + "," + guideY
			} else {
				pathSeg += "," + Math.round(x) + ","  + guideY + "," +  Math.round(x+barW) + "," + guideY
			}
			
		}
		
		
		r.mouseover(function(event) {

			r.animate( {
				scale : [ 1.01, 1.01, x, y ]
			}, 500, "elastic");
			txt.animate( {
				opacity : 1
			}, 500, "elastic");
		}).mouseout(function(event) {
			r.animate( {
				scale : [ 1, 1, x, y ]
			}, 500, "elastic");
			txt.animate( {
				opacity : 0
			}, 500);
		});
		
		
		var t = paper.text(x + (barW / 2), chartY + 20, labels[i]);
		t.rotate(55);
		start += 0.05;
		
	};

	for ( var i = 0; i < labels.length; i++) {

		process(i);
	}

	if (hasGuide) {
		var guideColor = "rgb(103, 101, 112)";
		guideLine = paper.path(pathSeg).attr({stroke: guideColor, "stroke-width": 4, "stroke-linejoin": "round"});
		
		var guideLbl = paper.text(chartW-50, chartH/4, "Budget line.  Anything over line is over budget.").attr( {
			fill : "rgb(27,95,138)",
			"font-family" : 'Fontin-Sans, Arial',
			"font-size" : "18px"
		});
		
		guideLbl.attr("opacity", 0).attr( {
			//fill : bcolor,
			stroke : "none",
			opacity : 0,
			"font-family" : 'Fontin-Sans, Arial',
			"font-size" : "12px"
		});
		
		guideLine.mouseover(function(event) {

			guideLine.animate( {
				scale : [ 1.01, 1.01, x, y ]
			}, 500, "elastic");
			guideLbl.animate( {
				opacity : 1
			}, 500, "elastic");
		}).mouseout(function(event) {
			guideLine.animate( {
				scale : [ 1, 1, x, y ]
			}, 500, "elastic");
			guideLbl.animate( {
				opacity : 0
			}, 500);
		});
		
	}
	
	
	//
	
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
