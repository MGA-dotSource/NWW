(function( $ ) {
    $.fn.loadR = function(options) {
    	var settings = $.extend({
    		showLoadingAnimation : false,
    		loadingHTML : '',
    		contentURL : ''
    	}, $.fn.loadR.defaults, options);
    	
    	return this.each(function() {
    		var elem = $(this);
    		var url = settings.contentURL == '' ? elem.attr('data-loadr') : settings.contentURL; 
    		if(settings.showLoadingAnimation) {
    			elem = elem.after(settings.loadingHTML);
    		}
    		
    		$.get(url, function(html) {
    			cleanupLoadingAnimation(elem);
    			elem.replaceWith(html);
    		}).fail(function() {
    			cleanupLoadingAnimation(elem);
    		});
    	});
    	
    	function cleanupLoadingAnimation(elem) {
    		if(settings.showLoadingAnimation) {
				elem.next().remove();
			}
    	}
    };
}( jQuery ));