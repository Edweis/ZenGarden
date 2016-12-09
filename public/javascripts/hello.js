//.toggleOnForm : display the form next to it, hide itself and the .toggleShadowForm if exists
//.toggleOfForm : hide the form where it is and show the .toggleOnForm and .toggleShadowForm

$(document).ready(function(){
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
});