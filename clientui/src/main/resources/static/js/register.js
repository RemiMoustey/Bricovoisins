class Register {
    constructor() {
        this.printAndCloseExamples("img-gardener", "examples-gardening");
        this.printAndCloseExamples("img-electrician", "examples-electricity");
        this.printAndCloseExamples("img-plumber", "examples-plumbing");
        this.printAndCloseExamples("img-carpenter", "examples-carpentry");
        this.printAndCloseExamples("img-painter", "examples-painting");
        this.printAndCloseExamples("img-builder", "examples-masonry");
        this.printAndCloseExamples("img-handyman", "examples-diy");
    }

    printAndCloseExamples(idImage, idExamples) {
        $(idImage).on('click', function() {
            $("#" + idExamples).css("display", "block");
        }.bind(this));
        $(".close-link--" + idExamples).on('click', function() {
            $("#" + idExamples).css("display", "none");
        }.bind(this));
    }
}

new Register();