var cbpAnimatedHeader;

//fix CSRF for AJAX requests
$(function () {
    var token = $("meta[name='_csrf']").attr('content');
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$(document).ready(function () {

    $('body').scrollspy({
        target: '.navbar-fixed-top',
        offset: 80
    });

    // Page scrolling feature
    $('a.page-scroll').bind('click', function(event) {
        var link = $(this);
        $('html, body').stop().animate({
            scrollTop: $(link.attr('href')).offset().top - 50
        }, 500);
        event.preventDefault();
        $("#navbar").collapse('hide');
    });
    
    cbpAnimatedHeader = (function() {
    	var docElem = document.documentElement,
    	header = document.querySelector( '.navbar-default' ),
    	didScroll = false,
    	changeHeaderOn = 200;
    	function init() {
    		window.addEventListener( 'scroll', function( event ) {
    			if( !didScroll ) {
    				didScroll = true;
    				setTimeout( scrollPage, 250 );
    			}
    		}, false );
    	}
    	function scrollPage() {
    		/*
    		 * HEADER ALWAYS STAYS THE SAME
    		var sy = scrollY();
    		if ( sy >= changeHeaderOn ) {
    			$(header).addClass('navbar-scroll')
    		}
    		else {
    			$(header).removeClass('navbar-scroll')
    		}
    		*/
    		didScroll = false;
    	}
    	function scrollY() {
    		return window.pageYOffset || docElem.scrollTop;
    	}
    	init();
    	
    })();
	    
	// Activate WOW.js plugin for animation on scroll
    new WOW().init();
    
    $('[data-async="true"]').submit(function(e) {
    	e.preventDefault();
    	
    	var form = $(this);
    	
    	$.ajax({
    		url : form.attr('action'),
    		data : form.serialize(),
    		method : form.attr('method'),
    		error : function() {
    			toastr.error('Ihre Anfrage konnte nicht verarbeitet werden!', 'Es ist ein Fehler aufgetreten!');
    		},
    		success : function() {
    			toastr.success('Wir haben ihre Anfrage erhalten und melden uns schnellstm&ouml;glich!', 'Kontaktanfrage versendet');
    			form.find('input').val('');
    			form.find('textarea').val('');
    		}
		});
    });
});

