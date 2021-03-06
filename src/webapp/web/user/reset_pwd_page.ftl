<#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"] />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><@s.text name="reset.password.action.title" /></title>
<#include "../template/header.ftl"/>
<script type="text/javascript">
function refresh()
{
	document.getElementById("imagevalue").src='${base}/security/securityCode.jspx?now=' + new Date();
}
</script>
</head>
<body>
	<#include "../template/navsection.ftl"/>
	<!-- nav title -->
	<div class="nav_namebar_div nav_title_gray">
		User <img border="0" src="${base}/images/grayarrow.png">
		<@s.text name="reset.password.action.title" />
	</div>
	<div style="clear:both"></div>
	<!-- main body -->
 	<div class="main_container">
 		<#include "../template/action_errors.ftl">	
 		<div class="container_inner">
 			<@s.form action="resetPassword.jspx" namespace="/user" method="post">
 			<div class="reguser_div">
				<@s.hidden name="user.id" />
				<@s.hidden name="user.resetPasswdHashCode" />
				<@s.hidden name="user.email" />
				<@s.hidden name="user.displayName" />
				<div class="display_field_div">
					User Name:
				</div>	
				<div class="display_field_content">
					<@s.property value="user.displayName"/>
				</div>
				<div class="blank_separator"></div>	
				<div class="display_field_div">
					E-mail:
				</div>	
				<div class="display_field_content">
					<@s.property value="user.email"/>
				</div>
				
	 			<div class="blank_separator"></div>	
	 			<br/>
 				
 				<div class="input_field_div"> New Password: </div>
 				<div class="input_field_div">
 					<@s.password name="user.password" cssClass="input_field_normal" />
 				</div>	
 				<div class="input_field_comment">
 				 	* (<@s.text name="reset.password.new.password.spec" />)
 				</div>
 				 
 				<div class="input_field_div"> New Password Again: </div>
 				<div class="input_field_div">
 				 	<@s.password name="rePassword" cssClass="input_field_normal" />
 				</div>	
 				<div class="input_field_comment">
 					* (<@s.text name="reset.password.new.password.again.spec" />)
 				</div>
 				 
 				<div class="input_field_div"> Word Verification: </div>
 				<div class="input_field_div">
 				 	<@s.textfield name="securityCode" cssClass="input_field_normal" />
 				</div>	
 				<div class="input_field_comment">
 					* (<@s.text name="security.code.spec" />)
 				</div>
 				<div class="security_code">
 					<img src="${base}/security/securityCode.jspx?now=new Date()" border="0" id="imagevalue" name="imagevalue" />
 				 	&nbsp; <a href="#" onclick="refresh()"><img src="${base}/images/refresh.png" class="security_code_img" /> can't read this?</a>
 				</div>
 				<br/>
 				<div class="button_div">
					<@s.submit value="Reset" cssClass="input_button" /> &nbsp; <@s.reset value="Clear" cssClass="input_button" />
				</div>
				<div style="clear:both"></div>
			</div>
			</@s.form>
 		</div>
        <br/>
		<div style="clear:both"></div>  
 	</div>
	<#include "../template/footer.ftl"/>
</body>
</html>