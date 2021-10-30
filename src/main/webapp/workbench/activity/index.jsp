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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script type="text/javascript">

	$(function(){

		//日期拾取器
		$(".time").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function(){
			$.ajax({
				url:"workbench/activity/save.do",
				type:"post",
				dataType: "json",
				data:{
					"owner": $.trim($("#create-owner").val()),
					"name":$.trim($("#create-name").val()),
					"startDate":$.trim($("#create-startDate").val()),
					"endDate":$.trim($("#create-endDate").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-description").val())
				},
				success:function(data){
					/*
					* data {"success":true/false}
					* */
					if(data.success){


						/*
						清空添加操作模态窗口中的数据
						注意：
							我们拿到了form表单的jquery对象
							对于表单的jquery对象，提供了submit()方法让我提交表单
							但是表单的jquery对象，没有为我们提供reset（）方法让我们重置表单（坑：idea 为我们提示了有reset()方法）

							虽然jquery对象没有为我们提供reset方法，但是原生js为我们提供了reset方法
							所以我们需要将jquery对象转换为原生dom对象

							jquery对象转换为dom对象：
								jquery对象[下标]
							dom对象转换为jquery对象
							$(dom对象)
						* */

						$("#activityAddForm")[0].reset();


						//添加成功后
						//刷新市场活动信息列表 （局部刷新）


						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						alert("创建成功！")
					}
					else{
						alert("保存失败");
					}
				}
			})
		})

		//为创建按钮绑定事件，打开添加操作的模态窗口
		$("#addBtn").click(function(){
			/*
			* 操作模态矿口的方式
			*  需要操作的模态窗口的jquery对象，调用modal方法，为该方法传递参数 show:打开模态窗口， hide：关闭模态窗口
			*
			*
			* */

			//从后台先取数据
			$.ajax({
				url:"workbench/activity/getUserList.do",
				type:"get",
				dataType:"json",
				success:function(data){
					/*

					data {
					 [{用户1},{用户2},{用户3}]
					* */
					var html = "<option></option>";
					//遍历出来的就是每个用户的信息，取得想要的信息就可以了
					$.each(data,function(index,element){
						html+= "<option value = '"+element.id+"'>"+element.name+"</option>";
					})
					//把所得的数据填充到下拉框
					$("#create-owner").html(html);


					//取得当前用户的id
					//在ji中使用 el表达式把必须要套用在字符串中

					var id = "${user.id}";
					//将当前登入的用户，设置为下拉框默认选项
					$("#create-owner").val(id);


					//当所有者下拉框的数据加载完毕后，再展示模态窗口
					$("#createActivityModal").modal("show");
				}
			})


		})
		//页面加载完毕后触发一个方法
		//默认展开列表的第一页，每页展现两条记录


		pageList(1,2);
		//为查询按钮绑定事件，触发pageList方法
		$("#searchBtn").click(function(){
			/*
			点击查询的时候，我们应该将搜索框中的信息保存起来，保存到隐藏域中
			* */

			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
		})

		//为全选绑定事件，触发全选操作
		$("#qx").click(function(){  		//this 是 ("#qx")
			$("input[name=xz]").prop("checked",this.checked);
		})

		//动态生成的元素，是不能够以普通绑定事件的形式来进行操作的
		/*
		动态生成的元素，我们要以on方式的形式来触发事件
		语法：
			$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jquery对象，回调函数)

		* */
		$("#activityBody").on("click",$("input[name=xz]"),function(){
											//复选框的选项个数如果登录  被选中的个数
			$("#qx").prop("checked",$("input[name=xz]").length == $("input[name=xz]:checked").length);
		})

		//为删除按钮绑定事件
		$("#delete").click(function(){
			var $xz = $("input[name=xz]:checked");
			if($xz.length == 0){
				alert("选择需要删除的活动");
			}else{

				//  询问用户是否真的删除
				if(confirm("你确定删除吗？")){
					//url:workbench/activity/delete.do?id=xxx&id=???
					//拼接参数
					var param = "";
					for(var i=0; i<$xz.length; i++){
						param += "id="+$($xz[i]).val();
						if(i<$xz.length-1){
							param+="&";
						}
					}
					$.ajax({
						url: "workbench/activity/delete.do",
						type:"post",
						dataType:"json",
						data:param,
						success:function(data){
							//需要接受 {"success":true/false}
							if(data.success){
								//删除成功后
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							}else{
								alert("删除失败")
							}
						}
					})
				}

			}
		})
		//为修改按钮绑定事件，打开修改模态窗口
		$("#editBtn").click(function(){
			var $xz = $("input[name=xz]:checked");

			if($xz.length == 0){
				alert("请选择要修改的市场活动记录");
			}else if($xz.length >1){
				alert("请选择其中一条记录");
			}else{
				var id = $xz.val();   //拿到选中要修改记录的id

				$.ajax({
					url: "workbench/activity/getUserListAndActivity.do",
					type:"get",
					dataType:"json",
					data:{
						"id": id
					},
					success:function(data){

						/*
                        data  {"uList":usList,"a":{市场活动}}
                        * */
						var html = "<option></option>"
						//"<option></option>"
						//处理所有者下拉框
						$.each(data.uList,function(i,n){
							html += "<option value='"+n.id+"'>"+n.name+"</option>"
						})
						$("#edit-owner").html(html);

						//处理activity对象
						$("#edit-id").val(data.a.id);
						$("#edit-name").val(data.a.name);
						$("#edit-owner").val(data.a.owner);
						$("#edit-cost").val(data.a.cost);
						$("#edit-startDate").val(data.a.startDate);
						$("#edit-endDate").val(data.a.endDate);
						$("#edit-description").val(data.a.description);

						//所有值都填好后，打开修改操作的模态窗口
						$("#editActivityModal").modal("show");
					}
				})
			}
		})

		//为修改市场活动记录的更新按钮操作绑定事件
		$("#updateBtn").click(function(){
				/*
				在实际开发项目中，一定是按照先做添加，在做修改的这种顺序
				所以，为了节省开发时间，修改操作一般都是copy添加操作
				* */
			$.ajax({
				url:"workbench/activity/update.do",
				type:"post",
				dataType: "json",
				data:{
					"id": $.trim($("#edit-id").val()),
					"owner": $.trim($("#edit-owner").val()),
					"name":$.trim($("#edit-name").val()),
					"startDate":$.trim($("#edit-startDate").val()),
					"endDate":$.trim($("#edit-endDate").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-description").val())
				},
				success:function(data){
					/*
					* data {"success":true/false}
					* */
					if(data.success){



						//修改成功后
						//刷新市场活动信息列表 （局部刷新）
						//pageList(1,2);     展示第一页， 两条记录

						/*
						第一个参数  表示操作后停留在当前页
						$("#activityPage").bs_pagination('getOption', 'currentPage')
						第二个参数  表示操作后维持用户已经选择好的显示页数
							$("#activityPage").bs_pagination('getOption', 'rowsPerPage')
						* */
						//pageList(("#activityPage").bs_pagination('getOption', 'currentPage'),
								//$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						//关闭修改操 作的模态窗口
						$("#editActivityModal").modal("hide");
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						alert("修改成功！")
					}
					else{
						alert("修改失败");
					}
				}
			})
		})

	});

	//分页
	function pageList(pageNo,pageSize){

		//如果用户点击删除操作，把全选的复选框给false
		$("#qx").prop("checked",false);
		//查询时，将隐藏域中保存的信息取出来，重新赋予到搜索框中
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));

		$.ajax({
			url: "workbench/activity/pageList.do",
			type:"post",
			dataType:"json",
			data:{
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startDate").val()),
				"endDate":$.trim($("#search-endDate").val())
			},
			success:function(data){
				/*

				我们需要的：市场活动信息列表
				[{市场活动1}，{市场活动2}，{市场活动3}]  list<Activity> aList
				一会分页插件需要的：查询出来的总记录数
				{"total":100} int total

				//总共
				{ "total":100,"dataList":[{市场活动1}，{市场活动2}，{市场活动3}]}
				* */

				var html = "";
				$.each(data.dataList,function(i,n){
					html +='<tr class="active">';
					html +='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>';									//先跳转到后台  并将id传给后台
					html +='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>';
					html +='<td>'+n.owner+'</td>';
					html +='<td>'+n.startDate+'</td>';
					html +='<td>'+n.endDate+'</td>';
					html +='</tr>';
				})
				//将市场活动记录展现到activityBody
				$("#activityBody").html(html);

				//计算 总页数
				var totalPages = data.total%pageSize == 0 ? data.total/pageSize : parseInt(data.total/pageSize)+1 ;
				//数据查询完毕后，结合分页插件，对前台展现分页相关信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//该回调函数实在点击分页组件触发的
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})


	}
	/*
	对于所有的关心型数据库，做前端的分页相关操作的基础组件
	就是pageNo和pageSize
	pageNo就是第几页，
	pageSize就是每页展现的记录条数

	pageList方法：就是发出ajax请求到后台，从后台取得最新的市场活动信息列表数据
				通过响应回来的数据，局部刷新市场活动信息列表

				我们都在哪些情况下，需要调用pageList方法（刷新）
				(1):点击市场活动超链接
				(2):添加，修改，删除
				(3):点击查询按钮的时候
				(4):点击分页组件


	*
	* */
	
</script>
</head>
<body>

	<input type = "hidden" id = "hidden-name">
	<input type = "hidden" id = "hidden-owner">
	<input type = "hidden" id = "hidden-startDate">
	<input type = "hidden" id = "hidden-endDate">
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">

					<form class="form-horizontal" role="form" >
						<input type = "hidden" id="edit-id">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner">
									<option>zhangsan</option>
									<option>lisi</option>
									<option>wangwu</option>
								</select>
							</div>
							<label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-name" >
							</div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time"  id="edit-startDate">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time"  id="edit-endDate">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>

						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--文本域textarea
									(1) 一定要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
									(2) textarea虽然是以标签对的形式来呈现，但是他也是属于表单元素范畴
									我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法，而不是html方法-->
								<textarea class="form-control" rows="3" id="edit-description">123</textarea>
							</div>
						</div>

					</form>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id ="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="activityAddForm">
					
						<div class="form-group">
							<label for="create-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-owner">



								 <!-- <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>-->

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-name">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startDate">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endDate">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<!--文本域textarea
									(1) 一定要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
									(2) textarea虽然是以标签对的形式来呈现，但是他也是属于表单元素范畴
									我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法，而不是html方法-->
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	

	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="searchBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
							点击创建按钮，观看两个属性值
						data-toggle = "modal";
						表示触发该按钮，将要打开一个模态窗口

						data-target="#createActivityModal";
						表示要打开哪个模态窗口，通过#id的形式找到该窗口

						现在我们是以属性和属性值得方式写在了button元素中，用来打开模态窗口
						但是这样做是有问题的：
								问题在于没有办法对按钮的功能进行填充
						所以在未来的实际项目开发，对于触发模态窗口的曹祖哦，一定不要写死在元素中，
						应该由我们自己写js代码来操作

					-->
					<button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
					<button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
					<button type="button" class="btn btn-danger" id="delete"><span class="glyphicon glyphicon-minus"></span> 删除</button>


				  <!--<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span> 删除</button>-->
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<!---<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>-->
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage">

				</div>

			</div>
			
		</div>
		
	</div>
</body>
</html>