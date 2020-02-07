class Register {
    constructor() {
        $("#img-gardener").on('click', function() {
            $("#examples-gardening").css("display", "block");
        }.bind(this));

        $(".close-link").on('click', function() {
            $("#examples-gardening").css("display", "none");
        }.bind(this));
    }
}

new Register();