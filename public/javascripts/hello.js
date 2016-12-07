$(document).ready(function(){
	$(".toggleOnFormPanel").click(function(){
		$(this).hide();
		$(this).parent().find("form").show();
	});
	
	$(".toggleOffFormPanel").click(function(){
		$(this).parent().hide();
		$(this).parent().parent().find(".toggleOnFormPanel").show();
	});
});