<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@s.text name="experiment.search.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#tooltip_link").easyTooltip({
                tooltipId:"search_tips",
                clickRemove:true,
                content:'<h4>Treatment Concentration International Unit</h4><p>Please use the following formula to convert an interferome unit to an international unit.</p>'
            });
        });


        $(document).ready(function () {
            $("#export_csv").easyTooltip({
                tooltipId:"expcsv_tips",
                content:'<h4>Save The Search Results To A CSV File</h4><p>(Maximum records are up to ${maxRecords})</p>'
            });
        });

        $("input.input_button_big").live('click', function (event) {
            event.preventDefault();
            alert("submit button clicked");
            targetForm = document.forms[0];
            targetForm.action = "searchData.jspx";
            targetForm.submit();
        });

    </script>

</head>
<body>
<#include "../template/navsection.ftl"/>
<div class="nav_namebar_div nav_title_gray">
    <a href="${base}/search/showSearch.jspx"><@s.text name="experiment.search.action.title" /></a>
</div>
<div style="clear:both"></div>
<div class="main_container">
<#include "../template/action_errors.ftl">
    <div class="container_inner_left">
        <div class="search_hints_outer_div">
            Please select the following search condition(s):
        </div>
    <@s.form action="searchGene.jspx" namespace="/search" method="post">
        <#include "../search/search_con.ftl">
        <div class="data_header_div">
        <@s.if test="%{resultSize == 0 }">
            <@s.submit value=" Search " cssClass="input_button" />
            &nbsp; <@s.reset value="Clear" cssClass="input_button" />

        </@s.if>
        <@s.else>
            <div class="blank_separator"></div>
            <@s.submit value=" Data " cssClass="input_button_big" /> &nbsp; <@s.submit value=" Ontology " cssClass="input_button_big" /> &nbsp;<@s.submit value=" Transcript " cssClass="input_button_big" /> &nbsp;<@s.submit value=" Chromosome " cssClass="input_button_big" /> &nbsp;<@s.submit value=" IFN Subtype " cssClass="input_button_big" /> &nbsp;
            <div class="blank_separator"></div>
        </@s.else>
        </div>
    </@s.form>

    <@s.if test="%{searched == true}">
        <@s.if test="%{searchType == 'gene'}">
           <@s.if test="%{genePagination != null}">
                <#include "../search/search_gene_result.ftl" />
            </@s.if>
        </@s.if>
        <@s.if test="%{searchType == 'data'}">
            <@s.if test="%{dataPagination != null}">
                <#include "../search/search_data_result.ftl" />
            </@s.if>
        </@s.if>
    </@s.if>

    </div>
<@s.if test="%{#session.authentication_flag =='authenticated'}">
    <div class="container_inner_right">
        <#include "../template/user_nav.ftl">
    </div>
</@s.if>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>