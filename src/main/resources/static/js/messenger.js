var irisUpdateRunning = false;
var irisUpdateInterval = '';

$(document).ready(function() {
    $('body')
        .on('submit', '[name="iris-form"]', function(e) {
            e.preventDefault();

            $('#iris-update-notification').removeClass('hidden');

            var url = $(this).attr('action');
            var data = $(this).serialize();

            $.post(url, data, function(content) {
                $('.iris-message-input').val('').focus();
            });
        })
        .on('click', '#iris-widget-paging-btn', function(e) {
           e.preventDefault();

           var url = $(this).attr('href');
           $.get(url, function(content) {
              $('#iris-widget-paging-btn').replaceWith(content);
           });
        })
        .on('keydown', $('.iris-message-input'), function(e) {
            if(e.ctrlKey && e.keyCode == 13) {
                $('[name="iris-form"]').submit();
            }
        });

    irisUpdateInterval = setInterval(updateIrisMessages, 2500);
});

function updateIrisMessages() {

    if(irisUpdateRunning) {
        return;
    }

    irisUpdateRunning = true;

    var url = $('#iris-update-notification').attr('data-iris-update-url');

    if(!url || url != '') {
        $.get(url, function(content) {
            $('#iris-update-notification').replaceWith(content);

            // check for added content
            var newElements = $('.chat-discussion .chat-message:not(.left,.right)');
            if(newElements.size() > 0) {

                var lastElementOrientation = $('.chat-discussion .chat-message.left,.chat-message.right').last().hasClass('left') ? 'left' : 'right';
                // update element orientations that have none
                newElements.each(function() {
                    var orientiation = lastElementOrientation === 'left' ? 'right' : 'left';

                    $(this).addClass(orientiation);

                    lastElementOrientation = orientiation;
                });

                // scroll down
                $('.chat-discussion').scrollTop(999999);
            }

            irisUpdateRunning = false;
        });
    }
}