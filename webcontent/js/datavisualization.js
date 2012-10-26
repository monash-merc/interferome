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
    var noJavaDivs = document.getElementsByClassName('nojava')


    for (var i = 0; i < noJavaDivs.length; i++) {
        noJavaDivs[i].style.display = 'none';
    }

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

    var svg = paper.toSVG();
    var b64 = Base64.encode(svg);
    document.getElementById("saveimage").innerHTML = "<a href-lang='image/svg+xml'  target='_blank'  href='data:image/svg+xml;base64,\n"+b64+"' title='heatmap.svg'>Download Image</a>";
    document.getElementById("hiddenlist").style.display = "none";

}

var Base64 = {
// private property
    _keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

// public method for encoding
    encode : function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;

        input = Base64._utf8_encode(input);

        while (i < input.length) {

            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }

            output = output +
                Base64._keyStr.charAt(enc1) + Base64._keyStr.charAt(enc2) +
                Base64._keyStr.charAt(enc3) + Base64._keyStr.charAt(enc4);

        }

        return output;
    },

// public method for decoding
    decode : function (input) {
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;

        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

        while (i < input.length) {

            enc1 = Base64._keyStr.indexOf(input.charAt(i++));
            enc2 = Base64._keyStr.indexOf(input.charAt(i++));
            enc3 = Base64._keyStr.indexOf(input.charAt(i++));
            enc4 = Base64._keyStr.indexOf(input.charAt(i++));

            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;

            output = output + String.fromCharCode(chr1);

            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }

        }

        output = Base64._utf8_decode(output);

        return output;

    },

// private method for UTF-8 encoding
    _utf8_encode : function (string) {
        string = string.replace(/\r\n/g,"\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {

            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            }
            else if((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            }
            else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }

        return utftext;
    },

// private method for UTF-8 decoding
    _utf8_decode : function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;

        while ( i < utftext.length ) {

            c = utftext.charCodeAt(i);

            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            }
            else if((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i+1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            }
            else {
                c2 = utftext.charCodeAt(i+1);
                c3 = utftext.charCodeAt(i+2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }

        }
        return string;
    }
}
