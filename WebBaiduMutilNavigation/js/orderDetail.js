
var OrderId = GetUrlem("orderId");
//alert(OrderId);
$(function(){
	
	var a = document.getElementById("showIsAsigned");
    var b = document.getElementById("showHaveAsigned");
	a.style.display = "none";
	b.style.display = "none";
	
	getOrderDetail();
});

function getOrderDetail()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetOrdersDetailByIdServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		data:{orderId:OrderId},
		error:function()
		{
			alert("订单详细信息Request error!");
		},
		success:function(data)
		{
			//alert("订单详细信息Request success!");
		
			$.each(JSON.parse(data), function(i,n) {
				document.getElementById("OrderId").innerHTML = n.OrderInfoId;
				document.getElementById("StartTime").innerHTML = n.StartTime;
				document.getElementById("Location").innerHTML = n.Longitude+","+n.Latitude;
				document.getElementById("ContactPhone").value = n.ContactPhone;
				if(n.SendTime == null)
				{
					document.getElementById("SendTime").value = "无要求";
				}else{
					document.getElementById("SendTime").value = n.SendTime;
				}
				
				document.getElementById("Money").innerHTML = n.Money;
				var detail = n.Deatail;
				
				document.getElementById("Detail").value = detail;
				if(n.Status == 0)
				{
					document.getElementById("Status").innerHTML = "未分配";
				}
				if(n.Status == 1)
				{
					document.getElementById("Status").innerHTML = "已分配";
					var a = document.getElementById("showIsAsigned");
					a.style.display = "table-row";
					document.getElementById("UserTel").value = n.UserTel;
				}
				if(n.Status == 2)
				{
					document.getElementById("Status").innerHTML = "已完成";
					document.getElementById("UserTel").value = n.UserTel;
					document.getElementById("EndTime").innerHTML = n.EndTime;
					var a = document.getElementById("showIsAsigned");
				    var b = document.getElementById("showHaveAsigned");
					a.style.display = "table-row";
					b.style.display = "table-row";
					
				}
				
			});
		}
	});
}

//修改订单
function editOrder()
{
	var OrderId = document.getElementById("OrderId").innerHTML;
	var ContactPhone = document.getElementById("ContactPhone").value;
	var  SendTime = document.getElementById("SendTime").value;
	var Detail = document.getElementById("Detail").value;
	alert(Detail);
	
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebEditOrdersServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		data:{orderId:OrderId,contactPhone:ContactPhone,sendTime:SendTime,detail:Detail},
		error:function()
		{
			alert("修改订单Request error!");
		},
		success:function(data)
		{
			alert("修改订单Request success!");
		}
	});
}
//删除订单
function deleteOrder()
{
	if(window.confirm("您确定要删除整个订单吗？"))
	{
		var OrderId1 = document.getElementById("OrderId").innerHTML;
		var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebDeleteOrdersServlet";
		$.ajax({
			type:"get",
			url:url,
			async:true,
			datatype : "JSON",
			data:{orderId:OrderId1},
			error:function()
			{
				alert("删除订单Request error!");
			},
			success:function(data)
			{
				alert("删除订单Request success!");
//				alert(data);
			}
		});
		
	}else{
		return false;
	}

}

function GetUrlem(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  decodeURI(r[2]); return null;
}