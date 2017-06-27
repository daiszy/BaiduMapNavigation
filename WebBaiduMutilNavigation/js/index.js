
$(function($){
	showList();
	//全局变量
    myArray = new Array(); 
    map = new BMap.Map("map");   //实例化地图控件
    
	var pointCenter = new BMap.Point(108.998919,34.25967);
	map.centerAndZoom(pointCenter,14);
	map.setCurrentCity("西安");
	map.enableScrollWheelZoom(true);
	
	//进行浏览器定位	
	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r){
//		var mk = new BMap.Marker(r.point);
//		map.addOverlay(mk);
//		map.panTo(r.point);
		
	},{enableHighAccuracy: true})
	
	//获取骑手位置，并在地图上显示
	getLocation();
	
});

function getLocation()
{
	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/WebGetLocationServlet";
	$.ajax({
		type:"get",
		url:url,
		async:false,
		datatype : "JSON",
		error:function()
		{
			alert("Request error!");
		},
		success:function(data)
		{
			//alert("Request success!");
			//alert(data);
			$.each(JSON.parse(data), function(i,n) {
				var marker = new BMap.Marker(new BMap.Point(n.longitude,n.latitude));
				map.addOverlay(marker);
						   
			   var content = "骑手姓名："+n.name+"</br> "+"具体位置："+n.longitude+","+n.latitude;
				
				marker.addEventListener("click",function(e){
					openInfo(content,e);
				});
			});
		}
	});
}

function openInfo(content,e)
{
	var opts = {
				width : 300,     // 信息窗口宽度
				height: 100,     // 信息窗口高度
				title : "信息窗口" , // 信息窗口标题
				enableMessage:true//设置允许信息窗发送短息
			   };
	
	var p = e.target;
	var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
	var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象 
	map.openInfoWindow(infoWindow,point); //开启信息窗口
}


function showList()
{
        
}

///*/*function getUserInfo(telphone)
//{
//	var url = "http://10.102.4.160:8080/MapMutilNaviagtion/GetUserInfoServlet";
//	$.ajax({
//		type:"get",
//		url:url,
//		async:false,
//		datatype : "JSON",
//		data:{username:telphone},
//		error:function()
//		{
//			alert("获取个人信息Request error!");
//		},
//		success:function(data)
//		{
//			//alert("获取个人信息Request success!");
//			$.each(JSON.parse(data), function(i,n) {
//				//alert(n.name);
//				return data.name;
//			});
//			
//			
//		}
//	});
//}*/*/
