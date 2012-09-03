<div class="result_title_div">
    Search Results
</div>
<div class="search_table_div">
    <div id="promoter_container"></div>
    <div id="tf_table">
        <table id="tfsites">
                        <tr>
                            <td>Gene</td><td>Sites</td>
                        </tr>
                    <@s.iterator status="status" value="tfSiteList">

                        <tr>
                            <td class="tfgene"><@s.property value="key" /></td>
                            <td class="tfsites">
                                <@s.iterator status="tfStatus" value="%{value}" id="tfSite">
                                    <@s.property value="#tfSite.factor" />,
                                    <@s.property value="#tfSite.coreMatch" />,
                                    <@s.property value="#tfSite.matrixMatch" />,
                                    <@s.property value="#tfSite.start" />,
                                    <@s.property value="#tfSite.end" /><br />
                                </@s.iterator>

                            </td>

                    </@s.iterator>



        </table>


        </li>
    </div>

</div>
<br/>