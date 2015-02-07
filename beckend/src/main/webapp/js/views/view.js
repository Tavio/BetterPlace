AppView = Backbone.View.extend({

    el: $(".content"),

    events: {
        "keypress #new-todo": "createOnEnter",
    },

    createOnEnter: function(e) {
        if (e.keyCode != 13) return;
        $("#todo-list").append("<li>"+$("#new-todo").val()+"</li>")
        $("#new-todo").val('');
    }

});

App = new AppView;