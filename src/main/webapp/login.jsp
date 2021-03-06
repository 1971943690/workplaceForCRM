
  Created by IntelliJ IDEA.
  User: WH
  Date: 2021/10/2
  Time: 22:51
  To change this template use File | Settings | File Templates.
--%>
  <%@ page contentType="text/html;charset=UTF-8" language="java" %>
  <%
	  String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
  %>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function(){
			//将当前页面设置为提成窗口
			if(window.top!=window){
					window.top.location = window.location;
			}
			//每次加载页面，将文本框的值清空
			$("#loginAct").val("");
			$("#loginPwd").html("");
			$("#msg").html("");
			//页面加载完毕后 获得文本框焦点
			$("#loginAct").focus();
			//为登入按钮绑定事件，执行登入操作
			$("#submitBtn").click(function(){
				login();
			})
			//为当前登入页面绑定敲键盘登入事件
			//event ：这个参数可以知道我们按键
			$(window).keydown(function(event){
				if(event.keyCode == 13){
					login();
				}
			})
		})
		//普通的定义的function一定要写到加载页面的外面
		function login(){
			//alert("login");
			//验证账号密码不能为空，  要去掉前后空格
			//使用  $.trim(文本)
			var loginAct =$.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if(loginAct =="" || loginPwd==""){
				$("#msg").html("账号密码不能空");
				//如果密码和账号为空，则直接返回
				return false;
			}
			//去后台验证
			$.ajax({
				url:"settings/user/login.do", //注意前面没有/
				type:"post",
				dataType:"json",
				data:{
					"loginAct": loginAct,
					"loginPwd": loginPwd
				},
				success:function(data){
					/*
					data {
						"success":ture/false,
						"msg": "错误原因"

					 */
					//alert(data);
					if(data.success){
						//跳转到工作台页面， 登入成功
						window.location.href = "workbench/index.jsp";
					}else{
						//显示错误原因
						$("#msg").html(data.msg);
					}
				}
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<form action="workbench/index.jsp" class="form-horizontal" role="form">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id = "loginAct">
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码"  id = "loginPwd">
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red; size: 5px">出错了</span>
						
					</div>
					<!--注意：按钮写在form表中，默认的行为就是提交表单
							一定要将按钮的类型设置为button
							按钮所触发的行为应该是有我们手动写js代码来决定-->
					<button type="button" id = "submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>