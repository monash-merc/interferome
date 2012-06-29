<div class="result_title_div">
    Search Results
</div>
<div class="data_header_div">
    <span class="name_title">Found a total of <font color="green"> ${dataPagination.totalRecords} </font> Data</span>

<@s.if test="%{dataPagination.totalRecords >0 }">
    <div class="export_div">
        Save as a CSV file <a
            href="${base}/search/exportCsvFile.jspx?maxRecords=<@s.property value='dataPagination.totalRecords' />&orderBy=${orderBy}&orderByType=${orderByType}">
        <img src="${base}/images/export.png" class="search_ctip_image" id="export_csv"/></a>
    </div>
</@s.if>
    <div style="clear:both"></div>
    <!-- page sorting block -->
    <div class="msg_content">
        <a href="${base}/${pageLink}${pageSuffix}<@s.property value='dataPagination.pageNo' />" class="page_url"></a>
    </div>
    <br/>
<#include "../pagination/pagination_header.ftl"/>
</div>

<div class="search_table_div">
    <table class="search_result_tab">
        <thead>
        <tr class="search_result_header">
            <td align="center" width="50">Dataset</td>
            <td align="center">FoldChange</td>
            <td align="center">Interferon Type</td>
            <td align="center">Treatment Time</td>
            <td align="center">Gene Symbol</td>
            <td align="center" width="180">Gene Description</td>
            <td align="center">GenBank ID</td>
            <td align="center">Ensembl ID</td>
            <td align="center">Probe ID</td>
        </tr>
        </thead>
        <tbody>
        <@s.iterator status="dataStat" value="dataPagination.pageResults" id="dataResult" >
        <tr>
            <td align="center">
                <div class="s_ds_link"><a
                        href="${base}/${viewDsAct}?experiment.id=<@s.property value='#dataResult.dataset.experiment.id' />&dataset.id=<@s.property value='#dataResult.dataset.id' />"><@s.property value="#dataResult.dataset.id" /></a>
                </div>
            </td>
            <td><@s.property value="#dataResult.value" /></td>
            <td align="center"><@s.property value="#dataResult.dataset.ifnType.typeName" /></td>
            <td align="center"><@s.property value="#dataResult.dataset.treatmentTime" /></td>
            <td><@s.property value="#dataResult.reporter.geneSymbol" /></td>
            <td width="180"><@s.property value="#dataResult.reporter.geneTitle" /></td>
            <td>
                <@s.if test="%{#dataResult.reporter.genBankAccession != '---'}">
                    <div class="s_ds_link">
                        <a href="${genBankLink}${dataResult.reporter.genBankAccession}"
                           target="_blank"><@s.property value="#dataResult.reporter.genBankAccession" /></a>
                    </div>
                </@s.if>
                <@s.else>
                    <@s.property value="#dataResult.reporter.genBankAccession" />
                </@s.else>
            </td>
            <td>
                <@s.if test="%{#dataResult.reporter.ensembl != '---'}">
                    <div class="s_ds_link">
                        <a href="${ensemblLink}${dataResult.reporter.ensembl}"
                           target="_blank"><@s.property value="#dataResult.reporter.ensembl" /></a>
                    </div>
                </@s.if>
                <@s.else>
                    <@s.property value="#dataResult.reporter.ensembl" />
                </@s.else>
            </td>
            <td><@s.property value="#dataResult.reporter.probeId" /></td>
        </tr>
        </@s.iterator>
        </tbody>
    </table>
</div>
<br/>
<#include "../pagination/search_page_style.ftl" />