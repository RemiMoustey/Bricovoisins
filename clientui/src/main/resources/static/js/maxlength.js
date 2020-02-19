$(function() {
    $("textarea[maxlength]").bind('input propertychange', function() {
        let maxLength = $(this).attr('maxlength');
        if ($(this).val().length > maxLength) {
            $(this).val($(this).val().substring(0, maxLength));
        }
    })
});