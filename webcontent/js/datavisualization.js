window.onload = function() {
    if(document.getElementById('promoter_container')){
        var pi = new PromoterImage();
    }

    if(document.getElementById('hs_chromosome_container')){
        var ci = new ChromosomeImage();
    }

    if(document.getElementById('tissueexp_container')){
        new HeatMap();
    }


    if(document.getElementById('venn')){
        drawVenn();
    }

    try {
        if(document.getElementById('tags1') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas1', 'tags1');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer1').style.display = 'none';
    }
    try {
        if(document.getElementById('tags2') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas2', 'tags2');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer2').style.display = 'none';
    }
    try {
        if(document.getElementById('tags3') != null){
            TagCanvas.textColour = 'blue';
            TagCanvas.outlineColour = '#ff9999';
            TagCanvas.outlineColour	= "#ffff99";
            TagCanvas.outlineMethod = "outline"
            TagCanvas.weight = true;
            TagCanvas.freezeActive	= true;
            TagCanvas.minBrightness	= 0.3;
            TagCanvas.Start('myCanvas3', 'tags3');
        }
    } catch(e) {
        // something went wrong, hide the canvas
        document.getElementById('myCanvasContainer3').style.display = 'none';
    }

    //Everything worked remove the java required tag
    document.getElementsByClassName('nojava').style.display = 'none';


};

function drawVenn(){
    var paper = new Raphael(document.getElementById('venn'), 1000, 400);
    var t1 = paper.circle(150, 150, 100);
    t1.attr({
        "fill": "red",
        "fill-opacity": 0.25
    });    var t2 = paper.circle(300, 150, 100);
    t2.attr({
        "fill": "blue",
        "fill-opacity": 0.25
    });
    var t3 = paper.circle(225, 275, 100);
    t3.attr({
        "fill": "green",
        "fill-opacity": 0.25
    });
    paper.text(150,150, document.getElementById('t1').value);
    paper.text(300,150, document.getElementById('t2').value);
    paper.text(225,275, document.getElementById('t3').value);
    paper.text(225,150, document.getElementById('t1t2').value);
    paper.text(265,215, document.getElementById('t2t3').value);
    paper.text(185,215, document.getElementById('t1t3').value);
    //paper.text(225,150, document.getElementById('t2t3').value);
    paper.text(225,195, document.getElementById('t1t2t3').value);

    var t1Title = paper.text(110, 110, "Type 1");
    t1Title.attr({
        "font-size": "16pt"
    });
    var t2Title = paper.text(340, 110, "Type 2");
    t2Title.attr({
        "font-size": "16pt"
    });
    var t3Title = paper.text(225, 340, "Type 3");
    t3Title.attr({
        "font-size": "16pt"
    });
    paper.
    document.getElementById("hiddenlist").style.display = "none";
}
