<div class="result_title_div">
    Search Results
</div>
<div class="export_div">
    Save as a TXT file<a
    href="${base}/search/exportCsvFileTissueExpression.jspx">
    <img src="${base}/images/export.png" class="search_ctip_image" id="export_pic"/></a>
</div>
<br>
<br/>
<br>
<br/>
<div class="search_table_div">
    <div class="nojava">
        <p>(Javascript Must Be Enabled to View These Results)</p>
    </div>
    <span style="color: gray; font-size: 10pt"><p>The image below uses a heat map to display the expression of interferon regulated genes (IRGs) in their resting, unstimulated state, across various tissues and cells.</p>
    <p>The human and mouse expression data were obtained from the tissues and cell lines data in the BioGPS portal.</p>
    <p>The IRG list resulting from the search is plotted against expression in these tissues and cells; red indicates high expression while blue indicates low expression.</p></span>

    <div id="tissueexp_container"></div>
    <div id="te_table">
        <table id="tesites">
            <@s.iterator status="tissStat" value="tissueExpressionList" id="tissueResult">
                <tr>
                    <@s.if test="#tissStat.first == true">
                        <th>GeneName</th>
                        <@s.iterator status="headerStat" value="#tissueResult.tissueexpression" id="headerVal">
                            <th class="tissues"><@s.property value='#headerVal.tissue.tissue' /></th>
                        </@s.iterator>
                        </tr><tr>
                    </@s.if>
                    <td class="geneName"><@s.property value='#tissueResult.gene.geneName' /></td>
                    <@s.iterator status="expStat" value="#tissueResult.tissueexpression" id="expVal">
                        <td class="expressionVal"><@s.property value='#expVal.expression' /></td>
                    </@s.iterator>
                </tr>

            </@s.iterator>
        </table>
    </div>
</div>
<br/>