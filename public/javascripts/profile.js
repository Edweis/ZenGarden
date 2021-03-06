$(document).ready(function(){
	// ################ Profile only
	// For formToogable() template
	// ################
	//.toggleOnForm : display the form next to it, hide itself and the .toggleShadowForm if exists
	//.toggleOffForm : hide the form where it is and show the .toggleOnForm and .toggleShadowForm
	//.toogleDisplay : element that hides and show when the form does not
	
	$(".toggleOnForm").click(function(){
		$(this).hide();
		$(this).parent().find("form").show();
		$(this).parent().find("form").focus();
		$(this).parent().find(".toogleDisplay").hide();
	});
	
	$(".toggleOffForm").click(function(){
		$(this).parent().hide();
		$(this).parent().parent().find(".toggleOnForm").show();
		$(this).parent().parent().find(".toogleDisplay").show();
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
				out.html($.parseHTML(res));
			}
		});
	});
	
	//######## Profile only
	//For edit and delete in itemPanel()
	//########
	$(".deletePanelItem").click(function(){
		var item = $(this).closest("li");
		$.get( $(this).attr("ref"), function( data ) {
			item.remove();				
		});
	});
	
	
	//######## Profile only
	//For contact removing
	//########
	$(".removeContact").click(function(){
		var id = $(this).attr("data-contactid");
		var action = $(this).attr("ref");
		var out = $(this).closest("tr");
		
		$.ajax({
			url: action,
			type: "get",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+$.parseHTML(res)+"</p>");
			},
			success: function(res){
				out.remove();
			}
		});
	})
	
	//######## Profile only
	//For contact adding
	//########
	$(".addContact").click(function(){
		var form = $(this).closest("form");
		var action = form.attr("action");
		var out = $(".contactList");
		$.ajax({
			url: action,
			data: $(this).closest("form").serialize(),
			type: "post",
			dataType: "html",
			error: function(res){
				out.append("<p class=\"error\">Error in ajax request :</br>"+res+"</p>");
			},
			success: function(res){
				out.append($.parseHTML(res));
				form.trigger("reset");
				form.find("input:first").focus();
			}
		});
	})
	
	
});