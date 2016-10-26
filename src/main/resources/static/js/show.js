function displaypicture(inout,rfid,timestamp){
		$.get("/fetchinoutimages",{"inout":inout,"datetime":timestamp.replace("."," "),"rfid":rfid})
				.done(function(data){
					//console.log(data);
					if(data.TotalAccountsReaderModule!=0)
					{
						$("#picturebox1").html("Reader<br><img width='100%' src='data:image/jpeg;base64,"+data.AccountsReaderModule[0].frontImageBytes+"' />");
					}
					else{
						$("#picturebox1").html("Reader<br>");
					}
					if(data.TotalAccountsCameraModule!=0)
						$("#picturebox2").html("Reader<br><img width='100%' src='data:image/jpeg;base64,"+data.AccountsCameraModule[0].frontImageBytes+"' />");
					else
						$("#picturebox2").html("Camera<br>");
					$("#picturebox1").append("<button class='btn btn-primary' onclick='hidepictures()'>Close</button>");
				
					$("#picturebox1").css("display","block");
					$("#picturebox2").css("display","block");
					
					$('html,body').animate({
				        scrollTop: $("#picturebox1").offset().top},
				        'slow');
					
				});
		
	}
function hidepictures(){
	
					$("#picturebox1").css("display","none");
					$("#picturebox2").css("display","none");
}
$(document).ready(function(){
	
	
	$("#findbyid").bind('submit',function(event){
		event.preventDefault();
		var studentId=$("#sid").val().trim();
		
		$.get("/getaccountbyid",{"studentId":studentId})
			.done(function(data){
				console.log(data);
				$("#accountdata").html("<hr size='30'>");
				$("#inhead").empty();
				$("#outhead").empty();
				$("#incomingdata").empty();
				$("#outgoingdata").empty();
				$("#accountdata").append('<div class="row"><div class="col-md-8"><p><b>Name: '+data.Account.studentName+'</b></p><p>Enrollment #: '+data.Account.studentId+'</p><p> Roll #: '+data.Account.rollNumber+'</p> <p>Department: '+data.Account.department+'</p> <p>Batch: '+data.Account.batch+'</p> <p>Vehicle: '+data.Account.vehicleType+'</p> <p>Vehicle No. plate: '+data.Account.vehicleNumber+'</p></div><div class="col-md-3"><img width="100%" style="border-radius: 50%;" src="data:'+data.Account.frontImageContentType+';base64,'+data.Account.frontImageBytes+'"></div></div><hr size="30"><div style="visibility:hidden" id="rfidiv">'+data.Account.rfid+'</div>');
				//$("#rfidiv").html("77,168,195,36");
				
				$.get("/fetchallinoutrecordsexcludingimages",{"rfid":$("#rfidiv").html().toString()})
				.done(function(data1){
					$("#inhead").html("Incoming");
					
					$("#outhead").html("Outgoing");
					for(var i=0;i<data1.TotalIncomingAccounts;i++)
					{
						console.log(data1.IncomingAccounts[i].timeStamp);
						$("#incomingdata").append('<hr size="30"><div class="row" onclick=displaypicture("in","'+$("#rfidiv").html()+'","'+data1.IncomingAccounts[i].timeStamp.replace(" ",".")+'")><button class="btn btn-primary">Date Time: '+data1.IncomingAccounts[i].timeStamp+"</button></div>");
					}
					for(var i=0;i<data1.TotalOutgoingAccounts;i++)
					{
						$("#outgoingdata").append('<hr size="30"><div class="row" onclick=displaypicture("out","'+$("#rfidiv").html()+'","'+data1.OutgoingAccounts[i].timeStamp.replace(" ",".")+'")><button class="btn btn-primary">Date Time: '+data1.OutgoingAccounts[i].timeStamp+"</button></div>");
					}
					
					
				});
				
				
			});
		
	});
	
});