$(document).ready(function(){
	
	$("#searchaccountbyname").bind('submit',function(event){
		event.preventDefault();
		var studentName=$("#sname").val().trim();
		$.get("/getaccountbyname",{"studentName":studentName})
			.done(function(data){
				$("#accountdata").html("<hr size='30'>");
				for(var i=0;i<data.TotalAccounts;i++){
					$("#accountdata").append('<div class="row"><div class="col-md-5"><p><b>Name: '+data.Accounts[i].studentName+'</b></p><p>Enrollment #: '+data.Accounts[i].studentId+'</p></div><div class="col-md-5"><p> Roll #: '+data.Accounts[i].rollNumber+'</p> <p>Department: '+data.Accounts[i].department+'</p> <p>Batch: '+data.Accounts[i].batch+'</p></div></div><hr size="30">');
				}
			});
		
	});
	
});