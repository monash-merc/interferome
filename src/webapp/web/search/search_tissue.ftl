<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
    <div id="tissueexp_container"></div>
    <div id="te_table">
        <table id="tesites">
            Hello
            <@s.iterator status="tissStat" value="tissueExpressionList" id="tissueResult">
                <tr><@s.property value='#tissueResult.expression' /></tr>

            </@s.iterator>
        </table>


        </li>
    </div>

</div>
<br/>