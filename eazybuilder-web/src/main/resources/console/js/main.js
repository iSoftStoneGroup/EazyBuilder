// Toggle Sidebar

$(function() {
    $(".toggle-sidebar").bind('click', function() {
        $("body").toggleClass("sidebar-collapse");
    });


    $(function() {
        $(".dial").knob();
    });
});

$(function() {
    $(".dropdown").hover(
        function() {
            $('.dropdown-menu', this).stop(true, true).fadeIn("fast");
            $(this).toggleClass('open');
            $('b', this).toggleClass("caret caret-up");
        },
        function() {
            $('.dropdown-menu', this).stop(true, true).fadeOut("fast");
            $(this).toggleClass('open');
            $('b', this).toggleClass("caret caret-up");
        });
});

// Load More
$(function() {
    $("#message-load-more").bind('click', function() {
        $("#message-load-more .fa").toggleClass("fa-spin");
        return false;
    });
});

// Select2 & Bootstrap-select
$(function() {
    $('.selectpicker').selectpicker();
});
