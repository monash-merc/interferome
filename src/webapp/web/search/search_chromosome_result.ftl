<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
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