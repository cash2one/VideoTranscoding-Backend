$(function callAjax() {
	var bar = $(".bar");
	var percent = $(".percent");
	var status = $("#status");

	$.ajax({
		type : "GET",
		url : "/ajaxCall",
		data : percent,
		cache : false,
		success : function(data) {
			if (data != "Vacio") {
				var percentVal = data + "%";
				bar.width(percentVal);
				percent.html(percentVal);
				var millisecondsToWait = 2000;
				setTimeout(function() {
					callAjax();
				}, millisecondsToWait);
			}
		}

	});
	// callAjax();

});