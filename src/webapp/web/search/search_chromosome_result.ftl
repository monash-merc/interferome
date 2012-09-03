<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
    <div id="chromosome_container"></div>
    <div id="gene_table">
        <table id="gene_chrome_pos">
            <tr class="search_result_header">
                <td>Gene Name</td><td>Chromosome</td><td>Start</td><td>End</td><td>Link</td>
            </tr>
            <@s.iterator status="chromosomeGeneList" value="chromosomeGeneList" id="chrGeneResults">
                <tr class="search_result_tab">
                    <td align="center"><@s.property value="#chrGeneResults.geneName"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.chromosome"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.startPosition"/></td>
                    <td align="center"><@s.property value="#chrGeneResults.endPosition"/></td>
                    <td align="center">${ensemblLink}<@s.property value="#chrGeneResults.ensgAccession"/></td>

                </tr>
            </@s.iterator>
        </table>
    </div>


    <table  class="chromosome_result_tab">
    <tr class="search_result_header"><td>Chromosome</td><td>Gene Count</td></tr>
    <@s.iterator status="chromosomeList" value="chromosomeList" id="chrResults">
       <tr class="search_result_tab">
            <td align="center"><@s.property value="#chrResults[0]"/></td>
            <td align="center"><@s.property value="#chrResults[1]"/></td>
       </tr>
    </@s.iterator>
    </table>



</div>
<br/>