<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><@s.text name="tfsite.import.action.title" /></title>
<#include "../template/jquery_header.ftl"/>
</head>
<body>
<#include "../template/navsection.ftl"/>
<div class="nav_namebar_div nav_title_gray">
    Annotation
    <img border="0" src="${base}/images/grayarrow.png">
    <a href="${base}/data/showImportTFSite.jspx"><@s.text name="tfsite.import.action.title" /></a>
</div>
<div style="clear:both"></div>
<div class="main_container">
<#include "../template/action_errors.ftl">
    <div class="container_inner_left">
        <div class="blank_separator"></div>
        <div class="data_outer_noborder_div">
            <p>
                The transcription factor binding site import is a backend singleton process, which will take a while to import a whole file.
            </p>
            <p>
                The input file should be in the format:&nbsp;&nbsp;&nbsp;Ensembl Gene Id, Factor, Start, End, Core Match, Matrix Match
            </p>
        </div>

    <@s.form action="importTFSite.jspx" namespace="/data" method="post" enctype="multipart/form-data" >
        <div class="data_outer_div">
            <div class="input_field_div">Please choose an transcription factor file: </div>
            <table class="reporter_tab">
                <tr>
                    <td><@s.file name="upload" cssClass="file_upload"/></td>
                </tr>
                <tr>
                    <td>
                        <div class="input_field_comment">
                            * (Only the CSV format is supported)
                        </div>
                    </td>
                </tr>
            </table>
            <div class="blank_separator"></div>
            <div style="clear:both"></div>

        </div>
        <div class="data_outer_noborder_div">
            <@s.submit value="Import" cssClass="input_button" id="wait_modal" name='wait_modal' />
            <div id='mask'></div>
            <div id='modal_window' >
                Importing the transcription factor binding sites, please wait ... <img src="${base}/images/wait_loader.gif" class="loading_image">
            </div>
        </div>
    </@s.form>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
        <div class="empty_div"></div>
    </div>
    <div class="container_inner_right">
    <#include "../template/user_nav.ftl">
    </div>
    <div style="clear:both"></div>
</div>
<#include "../template/footer.ftl"/>
</body>
</html>