// fix CSRF for AJAX requests
$(function () {
    var token = $("meta[name='_csrf']").attr('content');
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

// helper method for url encoding of strings
function fixedEncodeURIComponent (str) {
	return encodeURIComponent(str).replace(/[!'()*]/g, function(c) {
		return '%' + c.charCodeAt(0).toString(16);
	});
}

$(document).ready(function() {
	formHandler.init();
	modalHandler.init();
	listHandler.init();
	
	// enable link to tab
	var url = document.location.toString();
	if (url.match('#')) {
		$('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
	} 
		
	// Change hash for page-reload
	$('.nav-tabs a').on('shown.bs.tab', function (e) {
		window.location.hash = e.target.hash;
	});
	
	// init sorting indicators
	$('.sorted:not(.sorted-asc):not(.sorted-desc)').before('<i class="fa fa-sort fa-fw m-r-xs"></i>');
	$('.sorted.alpha.sorted-asc').before('<i class="fa fa-sort-alpha-asc m-r-xs"></i>');
	$('.sorted.sorted-asc:not(.alpha)').before('<i class="fa fa-sort-amount-asc m-r-xs"></i>');
	$('.sorted.alpha.sorted-desc').before('<i class="fa fa-sort-alpha-desc m-r-xs"></i>');
	$('.sorted.sorted-desc:not(.alpha)').before('<i class="fa fa-sort-amount-desc m-r-xs"></i>');
});

var formHandler = {
	init : function() {
		formHandler.initSummernote();
		formHandler.initSkillsListHandler();
	},
	
	initSummernote: function() {
		// init summernote inputs
		$('.summernote').each(function() {
			var elem = $(this);
			var mode = elem.attr('data-mode');
			
			switch (mode) {
				case 'no_toolbar' :
					elem.summernote({
						height: 300,
						toolbar: []
					});
					break;
				case 'simple' :
					elem.summernote({
						height: 300,
						toolbar : [
				           ['style', ['bold', 'italic', 'underline', 'clear']],
				           ['misc', ['undo', 'redo']]
			            ]
					});
					break;
				case 'no_media' :
					elem.summernote({
						height: 300,
						toolbar : [
				           ['style', ['bold', 'italic', 'underline', 'clear']],
				           ['misc', ['undo', 'redo']],
				           ['insert', ['link']]
			            ]
					});
					break;
				case 'extended' :
					elem.summernote({
						height: 800,
						toolbar : [
				           ['style', ['bold', 'italic', 'underline', 'clear']],
				           ['misc', ['undo', 'redo']],
				           ['insert', ['link', 'picture', 'video']]
			            ],
			            onImageUpload : formHandler.handleSummernoteFileUpload
					});
				default:
					elem.summernote({
						height: 300,
						toolbar : [
				           ['style', ['bold', 'italic', 'underline', 'clear']],
				           ['misc', ['undo', 'redo']],
				           ['insert', ['link', 'picture', 'video']],
				           ['paragraph', ['ol', 'ul', 'paragraph']]
			            ],
			            onImageUpload : formHandler.handleSummernoteFileUpload
					});
					break;
			}
			
			elem.code(elem.val());

			elem.parentsUntil('form').parent().submit(formHandler.handleSummernoteEditorFormSubmit);
		});
	},
	
	handleSummernoteEditorFormSubmit : function() {
		// fill all inputs that where editors before submitting the form 
		$('.summernote').each(function() {
			$(this).val($(this).code());
		});
	},
	
	handleSummernoteFileUpload : function(files, editor, welEditable) {
		var formData = new FormData();
        formData.append("file", files[0]);
        
        // create file upload progress bar within summernote file selection modal
        var progressbarMarkup = '<div id="fileUploadProgress" class="progress progress-xs progress-corners progress-striped active">'
        		+ '<div style="width: 0" role="progressbar" class="progress-bar progress-bar-info">'
        		+ '</div></div>';
        $('.note-toolbar').after(progressbarMarkup);
        
        $.ajax({
            data: formData,
            type: 'POST',
            xhr: function() {
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) {
                	myXhr.upload.addEventListener('progress', function(e) {
	                	if(e.lengthComputable){
	                		var percent = (e.loaded / e.total) * 100;
	                        $('#fileUploadProgress div').attr('style', 'width: ' + percent + '%;');
	                    }
	                }, false);
                }
                return myXhr;
            },
            url: '/files/upload.do',
            cache: false,
            contentType: false,
            processData: false,
            success: function(data) {
                editor.insertImage(welEditable, data.url);
                $('#fileUploadProgress').remove();
            }
        });
	},
	
	initSkillsListHandler : function() {
		$('[data-ref="skills-form"]').on('click','[data-ref="skillsList"] button[data-ref="switchSkillType"]', function(e) {
			e.preventDefault();
			
			$(this).find('i').toggleClass('fa-star-o').toggleClass('fa-star');
			var input = $(this).parent().next('input');
			if(input.attr('name') == 'skills') {
				input.attr('name', 'highlightedSkills');
			}
			else {
				input.attr('name', 'skills');
			}
		})
		.on('click', '[data-ref="removeSkill"]', function(e) {
			e.preventDefault();
			
			$(this).parentsUntil('.form-group').parent().hide(200, function() {
				$(this).remove();
			});
		})
		.on('click', '[data-ref="addSkill"]', function(e) {
			e.preventDefault();
			var newSkillUrl = $(this).parentsUntil('form').parent().attr('data-new-skill-url');
			
			$.get(newSkillUrl, function(html) {
				$(html).insertBefore('[data-ref="addSkill"]');
			});
		});
	}
}

var modalHandler = {
	init : function() {
		// reset all modals on close
		$('.inmodal').on('hidden.bs.modal', function() {
			$(this).removeAttr('style').removeData();
		    $('.inmodal .modal-content').html('');
		});
	}
}

var listHandler = {
	init : function() {
		if($('body').iCheck != undefined) {
			$('.i-checks').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green',
			});			
		}
	}
}
