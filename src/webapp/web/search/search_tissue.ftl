<div class="result_title_div">
    Search Results
</div>
<div id="saveimage"></div>
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
    <span style="color: gray; font-size: 10pt"><p>The images below use a heat map to display the expression of interferon regulated genes (IRGs) in their resting, unstimulated state, across various tissues and cells.</p>
          <p>A separate plot is produced for human and mouse data.</p>
          <p>The human and mouse expression data were obtained from the tissues and cell lines data in the BioGPS portal<sup>*</sup>.</p>
          <p>The first two columns show the name of the gene returned in the result set and the name of probe asociated with this gene within the BioGPS dataset</p>
          <p>The IRG list resulting from the search is plotted against expression in these tissues and cells; deep red indicates very high expression, pale red indicates moderately high expression, pale blue indicates moderately low expresison, and deep blue indicates very low expression.</p>
          <p>Some gene names are associated with more than one probe within the BioGPS  data set, in which case data from both probes are presented on different rows.</p>
    </span>
    <div id="tissueexp_container"></div>
    <div id="te_table">
        <table id="tesites">
            <@s.iterator status="tissStat" value="geneExpressionRecordList" id="tissueResult">
                <tr>
                    <@s.if test="#tissStat.first == true">
                        <th>Probe Id</th>
                        <@s.iterator status="headerStat" value="#tissueResult.tissueExpressionList" id="headerVal">
                            <th class="tissues"><@s.property value='#headerVal.tissue.tissueId' /></th>
                        </@s.iterator>
                        </tr><tr>
                    </@s.if>
                    <td class="probeId"><@s.property value='#tissueResult.probe.probeId' />  <@s.property value='#tissueResult.geneName' /></td>
                    <!--<td class="geneId"><@s.property value='#tissueResult.geneName' /></td>   -->
                    <@s.iterator status="expStat" value="#tissueResult.tissueExpressionList" id="expVal">
                        <td class="expressionVal"><@s.property value='#expVal.expression' /></td>
                    </@s.iterator>
                </tr>

            </@s.iterator>
        </table>
    </div>
    <p><sup>*</sup>Chunlei Wu, Ian MacLeod, and Andrew I. Su. (2012) <i>BioGPS and MyGene.info: organizing online, gene-centric information.</i> Nucleic Acids Research<b>41(D1):</b> D561-D565.</p>
</div>
<br/>