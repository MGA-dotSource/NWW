$(document).ready(function() {
	$('[data-role="saveDraft"]').click(function(e) {
		e.preventDefault();

		var form = $(this).parentsUntil('form').parent();
		
		form.find('.summernote').each(function() {
			$(this).val($(this).code());
		});
		
		$.ajax({
			url: $(this).attr('href'),
			data: form.serialize(),
			method : form.attr('method'),
    		error : function() {
    			toastr.error('Entwurf konnte nicht gespeichert werden!', 'Es ist ein Fehler aufgetreten!');
    		},
    		success : function(uuid) {
    			$('input[name="UUID"]').val(uuid);
    			toastr.success('', 'Entwurf gespeichert');
    		}
		});
	});
});