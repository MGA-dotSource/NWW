var chatItemList;
var chatItemTemplateLeft = '<div class="chat-message left" data-msgid="{{msgid}}">'
	+ '<a href="/network/users/{{userid}}/" class="pull-left" title="{{username}}">'
	+ '<img class="message-avatar" src="img/a1.jpg" alt="{{username}}" />'
	+ '</a>'
	+ '<div class="message">'
	+ '<a class="message-author" href="/network/users/{{userid}}/">{{username}}</a>'
	+ '<span class="message-date"> Mon Jan 26 2015 - 18:39:23 </span>'
	+ '<span class="message-content">{{content}}</span>'
	+ '<small class="text-muted">Today 4:21 pm - 12.06.2014</small>'
	+ '</div></div>';
		
var chatItemTemplateRight = '<div class="chat-message right" data-msgid="{{msgid}}">'
	+ '<a href="/network/users/{{userid}}/" class="pull-left" title="{{username}}">'
	+ '<img class="message-avatar" src="img/a1.jpg" alt="{{username}}" />'
	+ '</a>'
	+ '<div class="message">'
	+ '<a class="message-author" href="/network/users/{{userid}}/">{{username}}</a>'
	+ '<span class="message-date"> Mon Jan 26 2015 - 18:39:23 </span>'
	+ '<span class="message-content">{{content}}</span>'
	+ '<small class="text-muted">Today 4:21 pm - 12.06.2014</small>'
	+ '</div></div>';

$(document).ready(function() {
	$('body').on('keydown', $('#newChatMessage'), function(e) {
		if(e.ctrlKey && e.keyCode == 13) {
			$('#chatForm').submit();
		}
	});
	
	$('body').on('submit', $('#chatForm'), function(e) {
		e.preventDefault();
		e.stopPropagation();
		
		var action = $(this).attr('action');
		console.log(action);
		$.ajax({
			url: action,
			method: 'POST',
			data: $(this).serialize(),
			success: function(data, status, jqXHR) {
				$('#chatForm textarea').val('').focus();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(textStatus);
			}
		});
	});
});