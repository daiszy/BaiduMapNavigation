
var telphone = GetUrlem("telphone");
//alert(telphone);

$(function(){
	
	getDetailInfo();
});

function getDetailInfo()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/GetUserInfoServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		data:{username:telphone},
		error:function()
		{
			alert("详细信息Request error!");
		},
		success:function(data)
		{
			//alert("详细信息Request success!");
			$.each(JSON.parse(data), function(i,n) {
				document.getElementById("name").value = n.name;
				document.getElementById("sex").value = n.sex;
				document.getElementById("telphone").innerHTML = n.telphone;
				document.getElementById("grade").value = n.grade;
				document.getElementById("ordersNum").innerHTML = n.ordersNum;
				
				document.getElementById("ofCity").value = n.ofCity;
				if(n.status == 0)
				{
					//alert("into 0");
					document.getElementById("status").innerHTML = "空闲" ;
				}
				if(n.status == 1)
				{
					//alert("1");
					document.getElementById("status").innerHTML = "正在配送中" ;
				}
			});
		}
	});
}

function editUser()
{
 	var Name = document.getElementById("name").value;
	var Sex = document.getElementById("sex").value;
	var Telphone = document.getElementById("telphone").innerHTML;
	var Grade = document.getElementById("grade").value;
	var OfCity = document.getElementById("ofCity").value;
	
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebEditUserServlet";
	$.ajax({
		type:"get",
		url:url,
		async:true,
		datatype : "JSON",
		data:{name:Name,sex:Sex,telphone:Telphone,grade:Grade,ofCity:OfCity},
		error:function()
		{
			alert("修改骑手信息Request error!");
		},
		success:function(data)
		{
			alert("修改骑手信息Request success!");
			alert(data);
		}
	});
}

function deleteUser()
{
	if(window.confirm("您确定要删除这个骑手吗？"))
	{
		var Telphone = document.getElementById("telphone").innerHTML;
		alert(Telphone);
		var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebDeleteUserServlet";
		$.ajax({
			type:"get",
			url:url,
			async:true,
			datatype : "JSON",
			data:{telphone:Telphone},
			error:function()
			{
				alert("删除骑手Request error!");
			},
			success:function(data)
			{
				alert("删除骑手Request success!");
				alert(data);
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