<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>${pageTitle!"No Page Title Found"}</title>
<#include "../template/header.ftl"/>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<#include "../template/action_nav_bar.ftl"/>
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">
 		<div>
	 		<div class="container_inner_left">
	 			<div class="empty_div"></div>
				<div class="empty_div"></div>
				<div class="empty_div"></div>
				<div class="empty_div"></div>
				<div class="empty_div"></div>  	
				<div class="empty_div"></div>
				<div class="empty_div"></div>  	
				<div class="blank_separator"></div>	
			</div>
			<div class="container_inner_right">
				<#include "../template/user_nav.ftl">
			</div>
		</div>
		<div style="clear:both"></div>  
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>


