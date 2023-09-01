(function (raphael) {
    $(function () {
        var values = [],
            labels = [];
        $("#chart1 tr").each(function () {
            values.push(parseInt($("td", this).text().replace(/,/g, ''), 10));
            labels.push($("th", this).text());
        });
        $("#chart1").hide();
        //7995b7
        //fff
        raphael("holder1", 400, 300).pieChart(200, 100, 75, values, labels, "#7995b7");
    });
})(Raphael.ninja());
