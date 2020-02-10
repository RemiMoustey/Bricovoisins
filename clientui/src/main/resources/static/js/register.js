class Register {
    constructor() {
        this.printAndCloseExamples("img-gardener", "gardening");
        this.printAndCloseExamples("img-electrician", "electricity");
        this.printAndCloseExamples("img-plumber", "plumbing");
        this.printAndCloseExamples("img-carpenter", "carpentry");
        this.printAndCloseExamples("img-painter", "painting");
        this.printAndCloseExamples("img-builder", "masonry");
        this.printAndCloseExamples("img-handyman", "diy");
        this.removeCompetence("gardening");
        this.removeCompetence("electricity");
        this.removeCompetence("plumbing");
        this.removeCompetence("carpentry");
        this.removeCompetence("painting");
        this.removeCompetence("masonry");
        this.removeCompetence("diy");
    }

    printAndCloseExamples(idImage, comptence) {
        $("#" + idImage).on('click', function() {
            $("#examples-" + comptence).css("display", "block");
            $("#remove-competence--" + comptence).css("margin-bottom", "10px");
        }.bind(this));
        $("#close-link--examples-" + comptence).on('click', function() {
            $("#examples-" + comptence).css("display", "none");
            $("#remove-competence--" + comptence).css("margin-bottom", "40px");
        }.bind(this));
    }

    removeCompetence(competence) {
        $("#remove-competence--" + competence).on('click', function () {
            $("#little-works-" + competence).prop("checked", false);
            $("#connoisseur-" + competence).prop("checked", false);
            $("#expert-" + competence).prop("checked", false);
        }.bind(this));
    }
}

new Register();