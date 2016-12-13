
$(document).ready(function(){
	// ################
	// For formToogable() template
	// ################
	//.toggleOnForm : display the form next to it, hide itself and the .toggleShadowForm if exists
	//.toggleOfForm : hide the form where it is and show the .toggleOnForm and .toggleShadowForm
	
	$(".toggleOnForm").click(function(){
		$(this).hide();
		$(this).parent().find("form").show();
		$(this).parent().find(".toggleShadowForm").hide();
	});
	
	$(".toggleOffForm").click(function(){
		$(this).parent().hide();
		$(this).parent().parent().find(".toggleOnForm").show();
		$(this).parent().parent().find(".toggleShadowForm").show();
	});
	
	
	//click on the submit button (SHOULB BE A BUTTON)
	$(".submitToggledForm[type='button']").click(function(){
		//get the form action
		var action = $(this).parent().attr("action");
		var method = $(this).parent().attr("method");
		
		var out = $(this).parent().parent().find(".toogleDisplay");
		var off = $(this).siblings(".toggleOffForm");
		
		$.ajax({
			url: action,
			type: method,
			data: $(this).parent().serialize(),
			dataType: "html",
			beforeSend: function(){
				out.append("</br>updating ...");
				off.click();
			},
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
			},
			success: function(res){
				out.replaceWith($.parseHTML(res));
			}
		});
	});
	
	//########
	//For edit and delete in itemPanel()
	//########
	$(".deletePanelItem").click(function(){
		var item = $(this).closest("li");
		$.get( $(this).attr("ref"), function( data ) {
			item.remove();				
		});
	});
	
	
});