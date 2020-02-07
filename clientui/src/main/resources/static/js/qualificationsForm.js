class QualificationsForm {

    constructor() {
        this.qualifications =
            [["jardinier-n1", "Jardinier - Petits travaux"], ["jardinier-n2", "Jardinier - Intermédiaire"], ["jardinier-n3", "Jardinier - Expert"],
            ["electricien-n1", "Electricien - Petits travaux"], ["electricien-n2", "Electricien - Intermédiaire"], ["electricien-n3", "Electricien - Expert"],
            ["plombier-n1", "Plombier - Petits travaux"], ["plombier-n2", "Plombier - Intermédiaire"], ["plombier-n3", "Plombier - Expert"],
            ["menuisier-n1", "Menuisier - Petits travaux"], ["menuisier-n2", "Menuisier - Intermédiaire"], ["menuisier-n3", "Menuisier - Expert"],
            ["peintre-n1", "Peintre - Petits travaux"], ["peintre-n2", "Peintre - Intermédiaire"], ["peintre-n3", "Peintre - Expert"],
            ["macon-n1", "Maçon - Petits travaux"], ["macon-n2", "Maçon - Intermédiaire"], ["macon-n3", "Maçon - Expert"],
            ["divers-n1", "Divers - Petits travaux"], ["divers-n2", "Divers - Intermédiaire"], ["divers-n3", "Divers - Expert"]];
        this.addListQualifications();
        this.removeListQualifications();
    }

    addListQualifications() {
        $("#add-list-qualifications").on('click', function(e) {
            e.preventDefault();

            let lastChildSelector = $("#groups-qualifications #group-qualifications:last-child select");
            let i = Number(lastChildSelector.attr("name").charAt(lastChildSelector.attr("name").length - 1));
            $("#groups-qualifications").append('<div id="group-qualifications' + '"></div>');
            let lastGroupSelector = $("#group-qualifications:last-child");
            lastGroupSelector.append('<label for="qualification' + (i + 1) + '">Qualification n°' + (i + 1) + ' :\xa0</label>');
            lastGroupSelector.append('<select name="qualification' + (i + 1) + '" id="qualification' + (i + 1) + '"></select>');
            for (let qualification in this.qualifications) {
                this.addQualification(i + 1, this.qualifications[qualification]);
            }
            if (i + 1 === 6) {
                this.makeDisabledButton($("#add-list-qualifications button"));
            }
            if (i + 1 > 1) {
                this.makeActivatedButton($("#remove-list-qualifications button"));
            }
        }.bind(this));
    }

    addQualification(i, qualification) {
        $("#qualification" + i).append('<option value="' + qualification[0] + '">' + qualification[1] + '</option>');
    }

    removeListQualifications() {
        $("#remove-list-qualifications").on('click', function(e) {
            e.preventDefault();

            $("#group-qualifications:last-child").remove();
            let lastChildSelector = $("#groups-qualifications #group-qualifications:last-child select");
            let i = Number(lastChildSelector.attr("name").charAt(lastChildSelector.attr("name").length - 1));
            if (i === 1) {
                this.makeDisabledButton($("#remove-list-qualifications button"));
            }
            if (i > 1) {
                this.makeActivatedButton($("#add-list-qualifications button"));
            }
        }.bind(this));
    }

    makeDisabledButton(selector) {
        selector.attr("disabled", "");
        selector.removeClass("pointer");
    }

    makeActivatedButton(selector) {
        selector.removeAttr("disabled");
        selector.addClass("pointer");
    }
}

new QualificationsForm();