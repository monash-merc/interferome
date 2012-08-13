<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
    <@s.iterator status="ontologyList" value="ontologyList" id="ontlResults">

            <div id="myCanvasContainer<@s.property value='%{#ontologyList.count}' />">
                <canvas width="500" height="300" id="myCanvas<@s.property value='%{#ontologyList.count}' />">
                    <p>In Internet Explorer versions up to 8, things inside the canvas are inaccessible!</p>
                </canvas>
            </div>
            <div id="tags<@s.property value='%{#ontologyList.count}' />">
                 <ul>
                  <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                        <li><a style='font-size:<@s.property value="#onResults[1]"/>0pt' href="${goLink}
                        <@s.property value="#onResults[0].goTermAccession"/>" target="_blank"><@s.property value="#onResults[0].goTermName"/></a></li>
                  </@s.iterator>
                </ul>
            </div>

        <table  class="search_result_tab">
            <tr class="search_result_header"><td>Accession</td><td>Term Name</td><td>Term Definition</td><td>Gene Count</td><td>Ontology Group Size</td></tr>
            <@s.iterator status="ontlResults" value="#ontlResults" id="onResults">
                <tr>
                    <td><@s.property value="#onResults[0].goTermAccession"/></td>
                    <td><@s.property value="#onResults[0].goTermName"/></td>
                    <td><@s.property value="#onResults[0].goTermDefinition"/></td>
                    <td><@s.property value="#onResults[1]"/></td>
                    <td><@s.property value="#onResults[0].goCount"/></td>
                </tr>
            </@s.iterator>
       </table>
    </@s.iterator>
    </table>
</div>
<br/>