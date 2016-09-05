// remove sidebar inside admin area


$(document).ready(function() {
	dynamicContentHandler.init();
	genericFormHandler.init();
	searchForm.init();
	profileForm.init();
	newsletterForm.init();
});

// provides methods for dynamically loading content using some attributes
var dynamicContentHandler = {
	init : function() {
		$('[data-ref="ajax-load"]').each(function() {
			var url = $(this).attr('data-url');
			$.get(url, dynamicContentHandler.ajaxCallback);
		});
	},
	
	ajaxCallback : function(html) {
		var ref = $(html).attr('data-ref');
		$('[data-ref-id="' + ref + '"]').replaceWith(html);
	}
}

// provides methods to handle form actions like
// - changing the submission target
// - auto submit on actions like click, change, etc.
// - init WYSIWYG editor
// - show / hide areas on click (without ajax reload)
var genericFormHandler = {
	init : function() {
		$('[data-ref="changetarget"]').click(genericFormHandler.changeTarget);
		$('select[data-ref="auto-submit"]').change(genericFormHandler.autoSubmit);
		$('button[data-ref="auto-submit"]').click(genericFormHandler.autoSubmit);
		$('[data-ref="showArea"]').click(genericFormHandler.showArea);
		$('[data-ref="ajaxSubmit"]').click(genericFormHandler.ajaxSubmit)
		
		genericFormHandler.initSummernoteEditor();
		$('form').submit(genericFormHandler.handleSummernoteEditorFormSubmit);
	},
	
	getForm : function(elem) {
		// check for immediate parent to be a form
		var form = elem.parent();
		if(!form.is('form')) {
			// check for any parent element to be a form
			form = elem.parentsUntil('form').parent();
		}
		
		return form;
	},
	
	changeTarget : function() {
		var form = genericFormHandler.getForm($(this));
		var target = $(this).attr('data-value');
		
		form.attr('target', target);
	},
	
	autoSubmit : function() {
		var form = genericFormHandler.getForm($(this));
		
		form.submit();
	},
	
	ajaxSubmit : function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		var form = genericFormHandler.getForm($(this));
		
		$.ajax({
			  type: form.attr('method'),
			  url: form.attr('action'),
			  data: form.serialize(),
			  contentType : form.attr('enctype') != '' ? form.attr('enctype') : 'application/x-www-form-urlencoded; charset=UTF-8',
			  success: function() {
				  console.log('runs');
			  }
		});
	},
	
	initSummernoteEditor : function() {
		// init editor without a toolbar
		$('input.editor-init.editor-no-tb').summernote({
			toolbar: []
        });
		
		// init default editor
		$('input.editor-init.editor-default').summernote({
			toolbar: [
			    ['style', ['style']],
	          	['style', ['bold', 'italic', 'underline', 'clear']],
          		['font', ['strikethrough']],
          		['fontsize', ['fontsize']],
          		['para', ['ul', 'ol', 'paragraph']],
          		['insert', ['link', 'picture', 'table', 'hr']]
	          ],
	        styleTags: ['p', 'h2']
        });
        
		$('input.editor-init').each(function() {
        	$(this).code($(this).val());
        });
		// prevent reinit of editor & buttons
		$('.editor-init').removeClass('editor-init').addClass('val-on-submit');
		
	},
	
	handleSummernoteEditorFormSubmit : function(e) {
		// fill all inputs that where editors before submitting the form 
		$('.val-on-submit').each(function() {
			$(this).val($(this).code())
		});
	},
	
	showArea : function(e) {
		var target = $(this).attr('data-ref-id');
		
		$('#' + target).show();
	}
}

var searchForm = {
	init : function() {
		$('[role="search-paging"]').click(searchForm.handlePaging);
		
		if($('[data-disable-paging="true"]').size() > 0) {
			$('[role="search-paging"]').addClass('hidden');
		}
		else if($('[data-paging-url]').size() > 0) {
			$('[role="search-paging"]').removeClass('hidden');
		}
	},
	
	handlePaging : function(e) {
		if(e !== undefined) {
			e.preventDefault();
			e.stopPropagation();			
		}
		
		var pagingInfo = $('.search-result [data-paging-url]'); 
		var url = pagingInfo.size() > 0 ? pagingInfo.attr('data-paging-url') : '';
		
		if(url != '') {
			$.get(url, searchForm.appendResults);
		}
		
		pagingInfo.remove();
	},
	
	clearAndAppendResults : function(data) {
		$('.search-result').empty();
		$('[role="search-paging"]').addClass('hidden');
		searchForm.appendResults(data);
	},
	
	appendResults : function(data) {
		$('.search-result').append(data);
		if($('[data-disable-paging="true"]').size() > 0) {
			$('[role="search-paging"]').addClass('hidden');
		}
		else {
			$('[role="search-paging"]').removeClass('hidden');
		}
	}
}

var profileForm = {
	init : function() {
		profileForm.currentForm = $('form[data-ref="profile-form"]'); 
		// check for profile form
		if(profileForm.currentForm.length > 0) {
			this.initAbilityHandling();
			this.initSpecialsHandling();
			this.initHobbiesHandling();
		}
	},
	
	initAbilityHandling : function() {
		var newUrl = $('[data-ref="profile-abilities"]').attr('data-new-url');
		$('[name="addNewAbility"]').click(function() {
			$.get(newUrl, function(html) {
				$('[data-ref="profile-abilities-list"]').append(html);
				$('[data-ref="profile-abilities-list"] input:last').focus();
				profileForm.initRemoveAbility();
			});
		})
		this.initRemoveAbility();
	},
	
	initRemoveAbility : function() {
		$('body').off('click', '[data-ref="removeAbility"]');
		$('[data-ref="removeAbility"]').click(function() {
			$(this).closest('.input-group').remove();
		});
	},
	
	initSpecialsHandling : function() {
		var newUrl = $('[data-ref="profile-specials"]').attr('data-new-url');
		$('[name="addNewSpecial"]').click(function() {
			$.get(newUrl, function(html) {
				$('[data-ref="profile-specials-list"]').append(html);
				$('[data-ref="profile-specials-list"] input:last').focus();
				profileForm.initRemoveSpecial();
			});
		})
		this.initRemoveSpecial();
	},
	
	initRemoveSpecial : function() {
		$('body').off('click', '[data-ref="removeSpecial"]');
		$('[data-ref="removeSpecial"]').click(function() {
			$(this).closest('.input-group').remove();
		});
	},
	
	initHobbiesHandling : function() {
		var newUrl = $('[data-ref="profile-hobbies"]').attr('data-new-url');
		$('[name="addNewHobby"]').click(function() {
			$.get(newUrl, function(html) {
				$('[data-ref="profile-hobbies-list"]').append(html);
				$('[data-ref="profile-hobbies-list"] input:last').focus();
				profileForm.initRemoveHobby();
			});
		})
		this.initRemoveHobby();
	},
	
	initRemoveHobby : function() {
		$('body').off('click', '[data-ref="removeHobby"]');
		$('[data-ref="removeHobby"]').click(function() {
			$(this).closest('.input-group').remove();
		});
	},
}

var newsletterForm = {
	init : function() {
		$('[data-ref="GetAllRecipients"]').click(newsletterForm.getAllRecipients);
	},
	
	getAllRecipients : function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		$.get($(this).attr('data-url'), function(recipients) {
			$('textarea[name="recipients"]').val(recipients);
		});
	}
} 