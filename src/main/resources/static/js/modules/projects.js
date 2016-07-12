$(document).ready(function() {
	$('.input-group.date').datepicker({
        todayBtn: "linked",
        keyboardNavigation: false,
        forceParse: false,
        calendarWeeks: true,
        autoclose: true,
        format: 'dd.mm.yyyy'
    });
	
	$('button[data-form-submit-action]').click(function() {
		var newFormAction = $(this).attr('data-form-submit-action');
		$(this).parentsUntil('form').parent().attr('action', newFormAction);
	});
	
	Dropzone.autoDiscover = false;
	new Dropzone("div#dropzone", {
		url: $('#dropzone').attr('data-upload-url'),
		autoProcessQueue: true,
		uploadMultiple: false,
		createImageThumbnails: false,
		parallelUploads: 5,
		maxFiles: 100,
		headers: {
	        'X-CSRF-Token': $('meta[name="_csrf"]').attr('content')
	    },
		// Dropzone settings
		init: function() {
			var myDropzone = this;
			
			this.on("sending", function() {
			});
			this.on("success", function(files, response) {
				console.log(response);
			});
			this.on("error", function(files, response) {
			});
		}
	});
});