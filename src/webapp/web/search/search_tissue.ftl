<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
    <div class="nojava">
        <p>(Javascript Must Be Enabled to View These Results)</p>
    </div>
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


        </li>
    </div>

</div>
<br/>