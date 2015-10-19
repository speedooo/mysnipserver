var myPresentation = {

    init: function (config) {
        console.log("init");
        myPresentation.config = {
            categories: $('#categories'),
            titles: $('#titles'),
            body: $('#body')
        };
        $.extend(myPresentation.config, config);
        myPresentation.bind();
    },

    bind: function () {
        console.log("bind");
        myPresentation.hideAll();
        $('#browseButton').click(myPresentation.showBrowse);
        $('#newSnippetButton').click(myPresentation.showNewSnippet);
        $('#newCategoryButton').click(myPresentation.showNewCategory);
        $(myPresentation.config.categories).change(myPresentation.loadTitles);
        $(myPresentation.config.titles).change(myPresentation.loadBody);
        $("#category").change(myPresentation.saveCategory);
        $('#bodyInput').change(myPresentation.saveSnippet);
    },

    hideAll: function () {
        $('div').hide();
    },

    showBrowse: function () {
        console.log('showBrowse');
        myPresentation.hideAll();
        myPresentation.loadCategories();
        $("#categoryDiv").show();
        $("#browseDiv").show();
    },

    showNewSnippet: function () {
      console.log("showNewSnippet");
        myPresentation.hideAll();
        myPresentation.loadCategories();
        $("#categoryDiv").show();
        $("#newSnippetDiv").show();
    },

    showNewCategory: function () {
        console.log("showNewCategory");
        myPresentation.hideAll();
        $("#newCategoryDiv").show();
    },

    loadCategories: function () {
        console.log("loadCategories");
        $.get("v1/categories", function (data, status) {
            var list = JSON.parse(data);
            $(myPresentation.config.categories).empty();
            $(list)
                .sort(function (a, b) {
                    return a.name > b.name;
                })
                .each(function () {
                    myPresentation.config.categories.append(new Option(this.name, this.name));
                });
        });
    },

    loadTitles: function () {
        console.log("loadTitles");
        var category = $(myPresentation.config.categories).val();
        console.log("Selected Category: " + category);
        $('option', myPresentation.config.titles).remove();
        $(myPresentation.config.body).val('');
        $.get("v1/snippets/category/" + category, function (data, status) {
            var list = JSON.parse(data);
            $(list).each(function () {
                myPresentation.config.titles.append(new Option(this.title, this.title));
            })
        })
    },

    loadBody: function () {
        console.log("loadBody");
        var category = $(myPresentation.config.categories).val();
        var title = $(myPresentation.config.titles).val();
        console.log("Selected Category: " + category + " Title: " + title);
        $.get("v1/snippets/category/" + category + "/title/" + title, function (data, status) {
            console.log("Status: " + status + " Data: " + data);
            var found = JSON.parse(data);
            $(myPresentation.config.body).val(found.body);
        })
    },

    saveCategory: function () {
        console.log("saveCategory");
        $.post("v1/categories", JSON.stringify({name: $("#category").val()}));
        $('#category').val('');
    },

    saveSnippet: function () {
        console.log("Save Snippet");
        $.post('v1/snippets', JSON.stringify({category: 'Java', title: 'default method', body: 'This is the body'}));
    }

};

$(document).ready(function () {
    myPresentation.init();
});
