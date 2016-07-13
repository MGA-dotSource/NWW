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
		createImageThumbnails: true,
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
			this.on("success", function(file, response) {
				// load file data input area for uploaded file
				$.get($('#dropzone').attr('data-new-data-url') + '?' + response.fileInformationUUID, function(html) {
					var table = $('#images');
					if(table.find('tbody tr').size() > 0) {
						// append after existing elements
						table.find('tbody tr:last').after(html);
					}
					else {
						// insert as first row
						table.find('tbody').html(html);
					}
				});
			});
			this.on("error", function(file, response) {
			});
		}
	});
});