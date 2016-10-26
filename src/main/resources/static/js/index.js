function logoutfunc()
{
	window.location.href="logout";
}
$(document).ready(function() {
	
	
	
	$("#addform").bind('submit',function(event){
		event.preventDefault();
		
		var cnicNo = $("#inpCnicNo").val().trim();
		var accountType = $("#inpAccountFor").val().trim();
		var rollNumber=$("#inpRollNumber").val().trim();
		var studentNameValue=$("#inpStudentName").val().trim();
		var studentIdValue=$("#inpStudentId").val().trim();
		var activeAccountValue=$("#inpActiveAccount").val().trim();
		var departmentValue=$("#inpDepartment").val().trim();
		var batchValue=$("#inpBatch1").val().trim() ;
		var rfID_user=$("#inpRfID_user").val().trim();
		var fileToUploadValue=$("#inpFileToUpload").prop("files")[0];
		var sex=$("#inpSex").val().trim();
		var vehicleNumber=$("#inpVehicleNumber").val().trim();
		var vehicleType=$("#inpVehicleType").val().trim();
		
		formdata= new FormData();

		
		if(accountType=='employee'){
			
			formdata.append("studentName",studentNameValue);
			formdata.append("studentId",studentIdValue);
			formdata.append("activeAccount",activeAccountValue);
			formdata.append("department","no");
			formdata.append("batch","no");
			formdata.append("rfid",rfID_user);
			formdata.append("file", fileToUploadValue);
			formdata.append("sex",sex);
			formdata.append("vehicleNumber",vehicleNumber);
			formdata.append("vehicleType", vehicleType);
			formdata.append("cnicNo",cnicNo);
			formdata.append("accountType", accountType);
			formdata.append("rollNumber", "no");
		
		} else if(accountType=='student'){
		
			formdata.append("studentName",studentNameValue);
			formdata.append("studentId",studentIdValue);
			formdata.append("activeAccount",activeAccountValue);
			formdata.append("department",departmentValue);
			formdata.append("batch",batchValue);
			formdata.append("rfid",rfID_user);
			formdata.append("file", fileToUploadValue);
			formdata.append("sex",sex);
			formdata.append("vehicleNumber",vehicleNumber);
			formdata.append("vehicleType", vehicleType);
			formdata.append("cnicNo",cnicNo);
			formdata.append("accountType", accountType);
			formdata.append("rollNumber", rollNumber);
		
		}
		
		

		
		$.ajax({
			  type: "POST",
			  url: "/account",
			  data: formdata,
			  success: function (jsonresponse){
				  console.log(jsonresponse);
				  var responseMap=$.parseJSON(jsonresponse);
				  $("#responselabel").empty();
				  
				  
				  var message=responseMap.message;
				  
				  
				  $("#responselabel").html(message);
				  $('html,body').animate({
				        scrollTop: $("#responselabel").offset().top},
				        'slow');

				  
				},
              processData: false,
              contentType: false
			});	
	
	});
	
	
	
	$("#btnEditAccount").click(function(){
		
		
		var inpEditCnic=$("#inpEditCnic").val().trim();
		var inpEditRollNumber=$("#inpEditRollNumber").val().trim();
		var inpEditThrough=$("#inpEditThrough").val().trim();
		var inpEditAccountId=$("#inpEditAccountId").val().trim();
		var inpEditStudentName=$("#inpEditStudentName").val().trim();
		var inpEditActiveAccount=$("#inpEditActiveAccount").val().trim();
		var inpEditBatch=$("#inpEditBatch1").val().trim() + "-" + $("#inpEditBatch2").val().trim();
		var inpEditSex=$("#inpEditSex").val().trim();
		var inpEditFileToUpload=$("#inpEditFileToUpload").prop("files")[0];
		var inpEditVehicleType=$("#inpEditVehicleType").val().trim();
		var inpEditDepartment=$("#inpEditDepartment").val().trim();
		var inpEditRfID_user=$("#inpEditRfID_user").val().trim();
		var inpEditStudentId=$("#inpEditStudentId").val().trim();
		var inpEditVehicleNumber=$("#inpEditVehicleNumber").val().trim();
		
		
		
		formdata= new FormData();
		formdata.append("idType",inpEditThrough);
		formdata.append("oldAccountId",inpEditAccountId);
		formdata.append("studentName",inpEditStudentName);
		formdata.append("activeAccount",inpEditActiveAccount);
		formdata.append("batch",inpEditBatch);
		formdata.append("sex",inpEditSex);
		formdata.append("file", inpEditFileToUpload);
		formdata.append("vehicleType",inpEditVehicleType);
		formdata.append("department",inpEditDepartment);
		formdata.append("rfid", inpEditRfID_user);
		formdata.append("studentId", inpEditStudentId);
		formdata.append("vehicleNumber", inpEditVehicleNumber);
		formdata.append("accountId", "checking");
		formdata.append("cnic", inpEditCnic);
		formdata.append("rollNumber", inpEditRollNumber);
		

		alert(inpEditCnic);
		
		
		alert("sending POST ajax");
		$.ajax({
			  type: "POST",
			  url: "/editaccount/",
			  data: formdata,
			  success: function (responseMap){
				  alert("success called");
				  $("#updatedAccount").empty();
				  var imageAppend="<img src='data:" + responseMap.account.frontImageContentType + ";base64," +  responseMap.account.bytes + "' height='200px' width='200px'style='float:right;' />";
				  
				  var message="Message : " + responseMap.message + "<br<br><br>";
				  var studentName="Student Name : " + responseMap.account.studentName + "<br>";
				  var studentId="Student ID : " + responseMap.account.studentId + "<br>";
				  var department="Department : " + responseMap.account.department + "<br>";
				  var batch="Batch : " + responseMap.account.batch + "<br><br><br>";
				  var rfid="Rfid : " + responseMap.account.rfid + "<br>";
				  var activeAccount="Active Account : " + responseMap.account.activeAccount + "<br>";
				  var sex="Sex : " + responseMap.account.sex + "<br>";
				  var vehicleType="Vehicle Type : " + responseMap.account.vehicleType + "<br>";
				  var vehicleNumber="Vehicle Number : " + responseMap.account.vehicleNumber + "<br>";
				  var frontImageContentType="file content type : " + responseMap.account.frontImageContentType;

				  $("#editAccountInfoDiv").append(imageAppend);
				  $("#editAccountInfoDiv").append(message);
				  $("#editAccountInfoDiv").append(studentName);
				  $("#editAccountInfoDiv").append(studentId);
				  $("#editAccountInfoDiv").append(department);
				  $("#editAccountInfoDiv").append(batch);
				  $("#editAccountInfoDiv").append(activeAccount);
				  $("#editAccountInfoDiv").append(rfid);
				  $("#editAccountInfoDiv").append(sex);
				  $("#editAccountInfoDiv").append(vehicleType);
				  $("#editAccountInfoDiv").append(vehicleNumber);
				  $("#editAccountInfoDiv").append(frontImageContentType);

				  alert('Updated with this information, after clicking OK the image will disappear');
				  $("#editAccountInfoDiv").empty();
				},
              processData: false,
              contentType: false
			});	

		
	});
	
	
});