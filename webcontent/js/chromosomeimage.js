/**
 * Created with IntelliJ IDEA.
 * User: helen
 * Date: 3/07/12
 * Time: 11:17 PM
 * To change this template use File | Settings | File Templates.
 */

function ChromosomeImage(){
    var paper = new Raphael(document.getElementById('chromosome_container'), 500, 350);


// to draw chromosomes

    var species ="human";
    //document.getElementById(
    var chrome_table = document.getElementById(species+"_chromosome");
    var gene_pos_table = document.getElementById("gene_chrome_pos");
    var separator = 0;
    var spacer = 0;
    var chrome_name;
    var centromere_pos;
    var height;
    var bottom_length;
    var top_length;
    var centromere_height;


    //Human Chromosome
    var hsChrom = new Object(); // or just {}
    hsChrom['1'] = new Array(121, 246);
    hsChrom['2'] = new Array(92, 244);
    hsChrom['3'] = new Array(90, 200);
    hsChrom['4'] = new Array(50, 192);
    hsChrom['5'] = new Array(46, 181);
    hsChrom['6'] = new Array(59, 171);
    hsChrom['7'] = new Array(58, 159);
    hsChrom['8'] = new Array(44, 146);
    hsChrom['9'] = new Array(47, 135);
    hsChrom['10'] = new Array(40, 136);
    hsChrom['11'] = new Array(53, 135);
    hsChrom['12'] = new Array(36, 134);
    hsChrom['13'] = new Array(17, 115);
    hsChrom['14'] = new Array(17, 106);
    hsChrom['15'] = new Array(18, 101);
    hsChrom['16'] = new Array(37, 90);
    hsChrom['17'] = new Array(24, 82);
    hsChrom['18'] = new Array(17, 78);
    hsChrom['19'] = new Array(26, 64);
    hsChrom['20'] = new Array(28, 64);
    hsChrom['21'] = new Array(13, 47);
    hsChrom['22'] = new Array(15, 50);
    hsChrom['X'] = new Array(60, 153);
    hsChrom['Y'] = new Array(12, 51);


    //Mouse Chromosome
    var mmChrom = new Object(); // or just {}
    mmChrom['1'] = new Array(198, 198);
    mmChrom['2'] = new Array(182, 182);
    mmChrom['3'] = new Array(160, 160);
    mmChrom['4'] = new Array(153, 153);
    mmChrom['5'] = new Array(153, 153);
    mmChrom['6'] = new Array(150, 150);
    mmChrom['7'] = new Array(153, 153);
    mmChrom['8'] = new Array(132, 132);
    mmChrom['9'] = new Array(125, 125);
    mmChrom['10'] = new Array(130, 130);
    mmChrom['11'] = new Array(122, 122);
    mmChrom['12'] = new Array(122, 122);
    mmChrom['13'] = new Array(121, 121);
    mmChrom['14'] = new Array(126, 126);
    mmChrom['15'] = new Array(104, 104);
    mmChrom['16'] = new Array(99, 99);
    mmChrom['17'] = new Array(96, 96);
    mmChrom['18'] = new Array(91, 91);
    mmChrom['19'] = new Array(62, 62);
    mmChrom['X'] = new Array(167, 167);
    mmChrom['Y'] = new Array(16, 16);



    //Add logic to check species
    visHashChrom = hsChrom;

// show the values stored
    for (var chrome_name in visHashChrom) {
        // use hasOwnProperty to filter out keys from the Object.prototype
        if (visHashChrom.hasOwnProperty(chrome_name)) {
            //
            var centromere_pos = visHashChrom[chrome_name][0];
            var height = visHashChrom[chrome_name][1];

            //writing the name of each chromosome
            paper.text((5 + spacer), 330, chrome_name);
            // drawing each chromosome

            bottom_length = height - centromere_pos - 10;
            //	alert(bottom_length);
            top_length = height - bottom_length - 10;
            var height_add = 288 - bottom_length;
            centromere_height = 294 - bottom_length;
            var centromere_space = spacer + 5;

            var chromo_bottom = paper.path("M "+spacer+" 300 l 0 -"+bottom_length+" c 0 -5 10 -5 10 0 l 0 "+bottom_length+" c 0 5 -10 5 -10 0");
            chromo_bottom.attr({gradient:'0-#333-#fff-#333'});

            var chromo_top = paper.path("M "+spacer+" "+height_add+" l 0 -"+top_length+" c 0 -5 10 -5 10 0 l 0 "+top_length+" c 0 5 -10 5 -10 0");
            chromo_top.attr({gradient:'0-#333-#fff-#333'});

            if (centromere_pos != height){

    //				alert("drawing centromere");

                var centromere = paper.ellipse(centromere_space, centromere_height, 5, 2);
                centromere.attr({fill: 'black'});
            }

            else {

                var top = 294 - centromere_pos;
                var centromere = paper.ellipse(centromere_space, top , 5, 2);
                centromere.attr({fill: 'black'});
    //				alert("accrocentric");
            }


    // Taking each position of a gene from a table, then determining if a gene needs to be drawn on the current chromosome being drawn and if so
    // drawing the position of the genes on the chromosome


            for (var r = 1, row; row = gene_pos_table.rows[r]; r++) {

                var gene_name = "";
                var chromosome = "";
                var start = "";
                var end = "";

                for (var c = 0, col; col = row.cells[c]; c++) {

                    if(col.firstChild != null){
                        var cell_val = col.firstChild.nodeValue;
                        //			  			alert(cell_val);

                        if (c == 0){
                                gene_name = cell_val;
                        }

                        if (c==1){
                            chromosome = cell_val;
                        }

                        if (c==2){
                            start = cell_val;
                            start = start/1000000;
                        }

                        if (c==3){
                            end = cell_val;
                            end=end/1000000;
                        }


                        if (c==4){
                            var link = cell_val;
                            //Only Draws Human Chromosomes

                            if (chrome_name==chromosome && start != null && link.indexOf("ENSG") != -1 ){

                                var line_height = 300 - start;
                                var gd = paper.path("M "+spacer+" "+line_height+" l 10 0");
                                gd.attr({'stroke-width': 2,
                                    stroke: 'red',
                                    gradient: '90-#700-#FF0-#700'});

                                // Allows information on the gene to be displayed when the mouse is held over the gene position

                                var display = gene_name + "  "+ chrome_name + ":" + (start*1000000) + " - " + (end*1000000);
                                gd.attr({title: display});

                                // This function in a function is to force js to evaluate the function and link the corrent gene name to a mouse click and
                                // then link to an ensembl gene summary page for that gene.
                                //							alert("before mouseover gene name ='"+gene_name + "'");
                                gd.node.onclick = linkOnClick(link);

                            }
                        }
                    }
                }
            }

            //This is the space between the chromosomes, it fits a maximum of 24 chromosomes in a 500 x 500 canvas. It could be changed to a dynamic variable that changed depending on the number of chromosomes.
            spacer = spacer + 20;
        }



    }

    //Successful Drawing of Chromosomes hide data table
    document.getElementById("gene_table").style.display="none";
}

function linkOnClick(gene_name){
        return function(){
//  	alert("from linkOnClick: " + gene_name);
            openGenePage(gene_name);
        };

}



function openGenePage(link) {
    //alert("in mouseover name =" + geneName);
    window.open(link);

}	