<div class="result_title_div">
    Search Results
</div>
<div class="data_header_div">
    <div class="export_div">
        Save as a CSV file <a
        href="${base}/search/exportCsvFileOntology.jspx">
        <img src="${base}/images/export.png" class="search_ctip_image" id="export_csv"/></a>
    </div>
</div>
<div class="search_table_div">
    <@s.iterator status="ontologyList" value="ontologyList" id="ontlResults">
            <div class="nojava">
                <p>(Javascript Must Be Enabled to View These Results)</p>
            </div>
            <div id="myCanvasContainer<@s.property value='%{#ontologyList.count}' />">
                <canvas width="500" height="300" id="myCanvas<@s.property value='%{#ontologyList.count}' />">
                    <p>In Internet Explorer versions up to 8, things inside the canvas are inaccessible!</p>
                </canvas>
            </div>
            <div id="tags<@s.property value='%{#ontologyList.count}' />">
                 <ul>
                  <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                      <@s.if test="%{#ontlResults.count < 16}">
                        <@s.if test="%{#onResults[1] < 100}">
                            <li><a style='font-size:<@s.property value="%{#onResults[1]}/10"/>pt' href="${goLink}<@s.property value="#onResults[0].goTermAccession"/>" target="_blank"><@s.property value="#onResults[0].goTermName"/></a></li>
                        </@s.if>
                        <@s.else>
                            <li><a style='font-size:10pt' href="${goLink}<@s.property value="#onResults[0].goTermAccession"/>" target="_blank"><@s.property value="#onResults[0].goTermName"/></a></li>
                        </@s.else>
                      </@s.if>
                  </@s.iterator>
                </ul>
            </div>

        <table  class="search_result_tab">
            <tr class="search_result_header"><td>Accession</td><td>Term Name</td><td>Term Definition</td><td>Gene Count</td><td>p Value</td></tr>
            <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                <tr>
                    <td><@s.property value="#onResults[0].goTermAccession"/></td>
                    <td><@s.property value="#onResults[0].goTermName"/></td>
                    <td><@s.property value="#onResults[0].goTermDefinition"/></td>
                    <td><@s.property value="#onResults[1]"/></td>
                    <!-- td><@s.property value="#onResults[2]"/></td -->
                    <td>N/A</td>
                </tr>
            </@s.iterator>
       </table>
    </@s.iterator>
    </table>
</div>
<br/>