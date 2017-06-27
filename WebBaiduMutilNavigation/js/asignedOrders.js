$(function(){
	
	//获取要分配的订单
	getAsigned();
	
	//获取空闲骑手信息
	getUserInfoList();
	
});

function getAsigned()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetAsignedOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		datatype : "JSON",
		error:function()
		{
			alert("获取要分配订单Request error!");
		},
		success:function(data)
		{
			//alert("获取要分配订单Request success!");
			$.each(JSON.parse(data), function(i,n) {
				
				var table = document.getElementById("rows");
				var row1 = document.createElement("tr");
	
				var td1 = document.createElement("td");
				td1.appendChild(document.createTextNode(n.OrderId));
				
				var td2 = document.createElement("td");
				td2.appendChild(document.createTextNode(n.StartTime));
	
				var td3 = document.createElement("td");
				td3.appendChild(document.createTextNode(n.Longitude+" , "+n.Latitude));
				
				var td4 = document.createElement("td");
				td4.appendChild(document.createTextNode(n.ContactPhone));
				
				row1.appendChild(td1);
				row1.appendChild(td2);
				row1.appendChild(td3);
				row1.appendChild(td4);
				
				
				table.appendChild(row1);
				
//					var UlList = document.getElementById("orderList");
//				
//					var LiList = document.createElement("li");
//					
//					UlList.appendChild(LiList);
//					
//					var spanOrderId = document.createElement("span");
//					spanOrderId.innerHTML = "订单号："+n.OrderId+"</br>";
//					
//					var spanOrderStatus = document.createElement("span");
//					spanOrderStatus.innerHTML = "订单状态：未分配";
//					
//					LiList.appendChild(spanOrderId)
//						  .appendChild(spanOrderStatus);						  
																	
			});		
		}
	});	
}

function getUserInfoList()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetFreeUserInfoServlet";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		datatype : "JSON",
		error:function()
		{
			alert("获取空闲骑手Request error!");
		},
		success:function(data)
		{
			
			//alert("获取空闲骑手Request success!");
			$.each(JSON.parse(data), function(i,n) {
				//动态加载个人列表信息
				addInfoList(n.name,n.telphone,n.ordersNum);
			});
			
			$("#ulId").listview('refresh');
		}
	});
}

function addInfoList(name,telphone,ordersNum)
{
	
	var ulList = document.getElementById("ulId");
	
	var liList = document.createElement("li");
	
	ulList.appendChild(liList);
	
	var aList = document.createElement("a");
	aList.className = "asignedUser";
	aList.onclick = function(){
		asigned(telphone);
	}
		
	liList.appendChild(aList);
	
	var div1List = document.createElement("div");
	div1List.className = "infoDiv1";
	
	var imgList = document.createElement("img");
	imgList.src = "img/user_image.png";
	imgList.className = "infoImg";
	
	div1List.appendChild(imgList);
	
	var div2List = document.createElement("div");
	div2List.className = "infoDiv2";
	
	var spanNameList = document.createElement("span");
	spanNameList.innerHTML ="<br/>"+"姓名："+name+"<br/>";
	
	var spanTelList = document.createElement("span");
	spanTelList.innerHTML ="手机号："+telphone+"<br/>";
	
	var spanOrderNumList = document.createElement("span");
	spanOrderNumList.innerHTML ="已接订单数："+ordersNum+"<br/>";
	
	
	
	/*var div3List = document.createElement("div");
	div3List.className = "infoDetail";
	div3List.innerHTML = "查看详情"+"<br/>";*/
	
	aList.appendChild(div1List)
		 .appendChild(div2List)
		 /*.appendChild(div3List);*/
		 
	
	
	div2List.appendChild(spanNameList)
			.appendChild(spanTelList)			
			.appendChild(spanOrderNumList);			
}

function asigned(telphone)
{
	alert(telphone);
	//为该骑手分配订单
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebAsignedOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		data:{username:telphone},
		datatype : "JSON",
		error:function()
		{
			alert("分配骑手请求Request error!");
		},
		success:function(data)
		{
			
			alert("分配骑手请求Request success!");
			
			alert(data);
			if(data.equals("true"))
			{
				alert("分配骑手成功");
			}
		}
	});
}
