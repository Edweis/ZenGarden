$(document).ready(function(){
	//####### Chat only
	//load latestChats every 5 seconds
	//#######
	function reload(){
		var out = $(".messagesBoard");
		var when = out.attr("data-last");
		var roomId = out.attr("data-roomid");
		var action = "/chat/messages/"+roomId+"/"+when;
		
		$.ajax({
			url: action,
			type: "get",
			dataType: "html",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
			},
			success: function(res){
				out.attr("data-last", $.now());
				out.children("ul").append($.parseHTML(res));
				
			}
		});
	}
	
	//call when start
	reload.call();
	
	//repeat every 5sec
	window.setInterval(reload, 5000);
	
	//####### Chat only
	//send a message
	//#######
	$(".sendMessage").click(function(){
		var refresher = $(".refresher");
		var out = $(".messagesBoard");
		var roomId = out.attr("data-roomid");
		var content = $(this).siblings(".contentMsg");
		var action = "/chat/messages/new/"+roomId;
		
		refresher.show();
		$.ajax({
			url: action,
			type: "post",
			data: content.serialize(),
			dataType: "html",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
				refresher.hide();
			},
			success: function(res){
				reload.call();
				refresher.hide();
				content.value("");
				content.focus();
			}
		});
	});
	
	//####### Chat only
	//get Contact
	//#######
	$(".getContact").click(function(){
		var out = $(".contactDisplay");
		var roomId = $(this).attr("data-roomid");
		var action = "/chat/getContact/"+roomId
		
		$.ajax({
			url: action,
			type: "get",
			dataType: "html",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
			},
			success: function(res){
				out.text(res);
			}
		});
	})
	
	//####### Chat only
	//give Contact
	//#######
	$(".giveContact").click(function(){
		var out = $(".contactDisplay");
		var roomId = $(this).attr("data-roomid");
		var action = "/chat/giveContact/"+roomId
		
		$.ajax({
			url: action,
			type: "get",
			dataType: "html",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
			},
			success: function(res){
				out.prepend("You have shared your contact !");
			}
		});
	})
	
});