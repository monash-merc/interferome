/**
 * Created with IntelliJ IDEA.
 * User: samf
 * Date: 3/07/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */

function HeatMap(){
    //This is the background

    //
    var oTable = document.getElementById('tesites');
    //Skip first (header) row
    var rowLength = oTable.rows.length;
    var columns = oTable.rows.item(0).cells.length - 1;
    var boxWidth = 10;
    var boxHeight = 10;

    paper = new Raphael(document.getElementById('tissueexp_container'), 150+(boxWidth*columns) , 250+(boxHeight*rowLength));

    var colorStats = calculateColorStatistics(oTable);
    var yPos = 170;

    //Draw Header
    var headX = 10;
    for(var k = 1; k < columns+1;k++){
        var tissue = oTable.rows.item(0).cells.item(k).innerHTML;
        var text = paper.text(headX, 170, tissue);
        text.transform("t100,100r90t-100,0");
        text.attr({'text-anchor': 'end'})
        headX = 10 + ((k)*boxWidth);
    }

    //Add Data

    //Skip the tissues row;
    for (i = 1; i < oTable.rows.length; i++){
        var oCells = oTable.rows.item(i).cells;
        var xPos = 100;
        //Add Gene Name
        var geneName = oCells.item(0).innerHTML;
        var text = paper.text(60, yPos+(boxHeight/2), geneName);
        text.attr({'text-anchor': 'start'})

        //Skip the header column (gene name)
        for(var j = 1; j < columns+1; j++){
            var cellVal = oCells.item(j).innerHTML;
            if(cellVal != null && cellVal != ""){
                var rect = paper.rect(xPos, yPos, boxWidth, boxHeight);
                var color = getColor(colorStats[0], colorStats[1], colorStats[2], cellVal);
                rect.attr({fill: color, stroke:"white"});
                xPos=xPos+boxWidth;
            }

        }
        yPos=yPos+boxHeight;
    }

    //document.getElementById("tf_table").style.visibility="hidden";
    document.getElementById("tesites").style.visibility = "hidden";
    document.getElementById("tissueexp_container").style.overflow = "scroll";
}

function calculateColorStatistics(oTable){
    var averageArray = new Array();
    var max = oTable.rows.item(1).cells.item(1).innerHTML;
    var min = oTable.rows.item(1).cells.item(1).innerHTML;
    for (i = 1; i < oTable.rows.length; i++){
        //Skip the header column (gene name)
        for(var j = 1; j < oTable.rows.item(0).cells.length; j++){
            var cellVal = parseFloat(oTable.rows.item(i).cells.item(j).innerHTML);
               averageArray.push(cellVal);
               if(cellVal < min){
                   min = cellVal;

               }
               if(cellVal > max){
                   max = cellVal;
               }

        }
    }
    var returnVal = new Array();
    returnVal[0] = median(averageArray)
    returnVal[1] = min;
    returnVal[2] = max;
    return returnVal;
}

function getColor(median, min, max, value){
    //Choose Color on blue/green scale

    if(parseFloat(value) < median){
        var blueVal = Math.round(255 - (255/(min-median))*(parseFloat(value)-median));
        var color = "rgb(" + blueVal + ", " + blueVal + ", 255)";
        return color;
    }
    else{
        //Choose Color on red scale
        if(parseFloat(value) > median){
            var redVal = Math.round(255 - (255/(max-median))*(parseFloat(value)-median));
            var color = "rgb(255, " + redVal + ", " + redVal + ")";
            return color;
        }
        //Choose white
        else{
            return "White";
        }
    }


}

function median(values) {

    values.sort( function(a,b) {return a - b;} );

    var half = Math.floor(values.length/2);

    if(values.length % 2)
        return values[half];
    else
        return (values[half-1] + values[half]) / 2.0;
}


//Code From : http://haacked.com/archive/2009/12/29/convert-rgb-to-hex.aspx
function colorToHex(color) {
    if (color.substr(0, 1) === '#') {
        return color;
    }
    var digits = /(.*?)rgb\((\d+), (\d+), (\d+)\)/.exec(color);

    var red = parseInt(digits[2]);
    var green = parseInt(digits[3]);
    var blue = parseInt(digits[4]);

    var rgb = blue | (green << 8) | (red << 16);
    return digits[1] + '#' + rgb.toString(16);
};


