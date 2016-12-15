$(document).ready(function(){
	var xhr;
	$('.autoSchool').autoComplete({
	    source: function(term, response){
	        try { xhr.abort(); } catch(e){}
	        xhr = $.getJSON('/autocomplete/school', { input: term }, function(data){
	        	response(data);
	        	});
	    }
	});
});