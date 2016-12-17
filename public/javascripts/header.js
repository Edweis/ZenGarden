$(document).ready(function(){
	//Check update of the profile notifications
	function refreshNotifications(){
		var out = $(".notificationPlace");
		
		if(out != null){
			//if the user is connected
			var action = "/notifications";
			$.ajax({
				url: action,
				type: "get",
				dataType: "html",
				error: function(res){
					out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
				},
				success: function(res){
					out.html($.parseHTML(res));
				}
			});
		}
		
	};
	
	//wait 2secondes before fecthing notifications, so if the guy read messages, they will be marked as read before reloading notification
	setTimeout(refreshNotifications, 2000);
	
	//repeat every minutes
	window.setInterval(refreshNotifications, 60000);
	
});