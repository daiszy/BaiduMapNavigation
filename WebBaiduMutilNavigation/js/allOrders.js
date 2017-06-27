$(function(){
    /*$('#myTab a:first').tab('show');
})
  $('#myTab a').click(function (e) {
  e.preventDefault();
  $(this).tab('show');*/
  
  //加载全部订单
  getAllOrders();
  getBeAsignedOrders();
  getIsAsignedOrders();
  getHaveAsignedOrders();
})

//获取全部订单
function getAllOrders()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetAllOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		error:function()
		{
			alert("获取全部订单Request error!");
		},
		success:function(data)
		{
			//alert("获取全部订单Request success!");
			$.each(JSON.parse(data), function(i,n) {
				var table = document.getElementById("rows0");
				//以表格形式动态加载订单
				addOrdersTable(table,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
//				var UlList = document.getElementById("allOrdersUl");
//				addOrders(UlList,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
			});
			
			
		}
	});	
}

//获取未分配订单
function getBeAsignedOrders()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		error:function()
		{
			alert("获取未分配订单Request error!");
		},
		success:function(data)
		{
			//alert("获取未分配订单Request success!");
			$.each(JSON.parse(data), function(i,n) {
				
				var table = document.getElementById("rows1");
				//以表格形式动态加载订单
				addOrdersTable(table,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
				/*var UlList = document.getElementById("beAsignedOrdersUl");
				
				addOrders(UlList,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);*/
			});		
		}
	});	
}



//获取已分配订单
function getIsAsignedOrders()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetIsAsignedOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		error:function()
		{
			alert("获取已分配订单Request error!");
		},
		success:function(data)
		{
			//alert("获取已分配订单Request success!");
			$.each(JSON.parse(data), function(i,n) {
				var table = document.getElementById("rows2");
				//以表格形式动态加载订单
				addOrdersTable(table,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
//				var UlList = document.getElementById("isAsignedOrdersUl");
//				
//				addOrders(UlList,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
			});		
		}
	});	
}

//获取已完成订单
function getHaveAsignedOrders()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetHaveAsignedOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		error:function()
		{
			alert("获取已完成订单Request error!");
		},
		success:function(data)
		{
			//alert("获取已完成订单Request success!");
			$.each(JSON.parse(data), function(i,n) {
				var table = document.getElementById("rows3");
				//以表格形式动态加载订单
				addOrdersTable(table,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
//				var UlList = document.getElementById("haveAsignedOrdersUl");
//				
//				addOrders(UlList,n.OrderId,n.StartTime,n.Latitude,n.Longitude,n.ContactPhone,n.Status);
			});		
		}
	});	
}

function addOrdersTable(table,OrderId,StartTime,Latitude,Longitude,ContactPhone,Status)
{
//	$("#rows").html("");
	var row1 = document.createElement("tr");
	
	var td1 = document.createElement("td");
	td1.appendChild(document.createTextNode(OrderId));
	
	var td2 = document.createElement("td");
	td2.appendChild(document.createTextNode(StartTime));
	
	var td3 = document.createElement("td");
	td3.appendChild(document.createTextNode(Longitude+" , "+Latitude));
	
	var td4 = document.createElement("td");
	td4.appendChild(document.createTextNode(ContactPhone));

    if(Status == 0)
	{
		Status = "未分配";
		var td5 = document.createElement("td");
		td5.style.color = "#FF0000";
		td5.appendChild(document.createTextNode(Status));
	}
	if(Status == 1)
	{
		Status = "已分配";
		var td5 = document.createElement("td");
		td5.style.color = "#912CEE";
		td5.appendChild(document.createTextNode(Status));
	}
	if(Status == 2)
	{
		Status = "已完成";
		var td5 = document.createElement("td");
		td5.style.color = "#4CAF50";
		td5.appendChild(document.createTextNode(Status));
	}
	
	
	var td6 = document.createElement("td");
//	var detailBtn = document.createElement("button");
//	detailBtn.textContent = "详情";
//	detailBtn.className = "buttonAction";
	var editBtn = document.createElement("button");
	editBtn.className = "buttonAction";
	editBtn.textContent = "编辑";
	//editBtn.style.backgroundColor="#EE5C42";
	editBtn.onclick = (function(OrderId){
		return function(obj){
			location.href = "orderDetail.html?orderId="+OrderId;
		}
		
	})(OrderId);
	//editBtn.addEventListener("click",function)
	//editBtn.onclick = window.location.href = "orderDetail.html?orderId="+OrderId;";
	var deleteBtn = document.createElement("button");
	deleteBtn.className = "buttonAction";
	deleteBtn.textContent = "删除";
	//deleteBtn.style.backgroundColor="#EE5C42";
	deleteBtn.onclick = (function(OrderId){
		return function(obj){
			deleteOrder(OrderId);
		}		
	})(OrderId);
	//td6.appendChild(detailBtn);
	td6.appendChild(editBtn);
	td6.appendChild(deleteBtn);
	
	row1.appendChild(td1);
	row1.appendChild(td2);
	row1.appendChild(td3);
	row1.appendChild(td4);
	row1.appendChild(td5);
	row1.appendChild(td6);
	
	table.appendChild(row1);
}

//删除订单
function deleteOrder(OrderId)
{
	//alert(OrderId);
	if(window.confirm("您确定要删除整个订单吗？"))
	{
		//var OrderId1 = document.getElementById("OrderId").innerHTML;
		var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebDeleteOrdersServlet";
		$.ajax({
			type:"get",
			url:url,
			async:true,
			datatype : "JSON",
			data:{orderId:OrderId},
			error:function()
			{
				alert("删除订单Request error!");
			},
			success:function(data)
			{
				alert("删除订单Request success!");
				alert(data);
			}
		});
		
	}else{
		return false;
	}

}

////动态加载订单
//function addOrders(UlList,OrderId,StartTime,Latitude,Longitude,ContactPhone,Status)
//{
//	
//	var AList = document.createElement("a");
//	AList.href = "orderDetail.html?orderId="+OrderId;
//	UlList.appendChild(AList);
//	
//	var LiList = document.createElement("li");
//	
//	AList.appendChild(LiList);
//	
//	var spanIdList = document.createElement("span");
//	spanIdList.innerHTML = "订单号："+OrderId+"</br>";
//	
//	var spanTimeList = document.createElement("span");
//	spanTimeList.innerHTML = "下单时间："+StartTime+"</br>";
//	
//	var spanLocationList = document.createElement("span");
//	spanLocationList.innerHTML = "具体位置："+Longitude+","+Latitude+"</br>";
//	
//	var spanPhoneList = document.createElement("span");
//	spanPhoneList.innerHTML = "联系人手机号："+ContactPhone+"</br>";
//	
//	var spanStatusList = document.createElement("span");
//	if(Status == 0)
//	{
//		spanStatusList.innerHTML = "订单状态："+"未分配"+"</br>";
//	}
//	if(Status == 1)
//	{
//		spanStatusList.innerHTML = "订单状态："+"已分配"+"</br>";
//	}
//	if(Status == 2)
//	{
//		spanStatusList.innerHTML = "订单状态："+"已完成"+"</br>";
//	}
//	LiList.appendChild(spanIdList)
//		  .appendChild(spanTimeList)
//		  .appendChild(spanLocationList)
//		  .appendChild(spanPhoneList)
//		  .appendChild(spanStatusList);
//}
