$(function(){
	
	//获取列表数
	getListNum();
	
	//获取骑手信息
	getUserInfoList();
	
	
});

function getListNum()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetListNumServlet";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		datatype : "JSON",
		error:function()
		{
			alert("获取列表Request error!");
		},
		success:function(data)
		{
			//alert("获取列表Request success!");
			document.getElementById("usersNum").innerHTML = data;
		}
	});
}

function getUserInfoList()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/GetUserInfoListServlet";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		datatype : "JSON",
		error:function()
		{
			alert("获取个人信息列表Request error!");
		},
		success:function(data)
		{
			
			//alert("获取个人信息列表Request success!");
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
	aList.href = "userDetail.html?telphone="+telphone;
		
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
