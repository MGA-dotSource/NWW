var imageDataRowCount = 0;

$(document).ready(function() {
	imageDataRowCount = $('#images tbody tr').size();

	console.log(imageDataRowCount);
	
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
	
	$('#images').on('click', 'button[name="btnRemoveImage"]', function() {
		var fileUUID = $(this).attr('data-ref');
		$('tr[data-file-uuid="' + fileUUID + '"]').remove();
		reindexImageRows();
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
				$.get($('#dropzone').attr('data-new-data-url') + '?fi=' + response.uuid + "&c=" + imageDataRowCount++, function(html) {
					var table = $('#images');
					
					if(table.find('tbody tr').size() > 0) {
						// append after existing elements
						table.find('tbody tr:last').after(html);
					}
					else {
						// insert as first row
						table.find('tbody').html(html);
					}
					
					// clear preview area
					$('.dz-preview').remove();
				});
			});
			this.on("error", function(file, response) {
			});
		}
	});
});

/**
 * Calculate new indexes for the project files input fields
 */
function reindexImageRows() {
	// first update the current total row count in case a new image wil be added while doing this here
	imageDataRowCount = $('#images tbody tr').size(); 
	
	var count = 0;
	$('#images tr [data-name="fileUUID"]').each(function() {
		var idValueFile = 'Images' + count + '.fileInformationUUID';
		var nameValueFile = 'Images[' + count + '].fileInformationUUID';
		var idValueDesc = 'Images' + count + '.Description';
		var nameValueDesc = 'Images[' + count + '].Description';
		$(this).attr('id', idValueFile).attr('name', nameValueFile)
				.next().attr('id', idValueDesc).attr('name', nameValueDesc);
		
		count++;
	});
}