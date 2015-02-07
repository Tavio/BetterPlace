/**
 * Created by philipe on 06/02/15.
 */
    console.log('routes');
var BP = Backbone.Router.extend({

    routes: {
        "/": "index",
        "/test": "test"
    },

    index: function() {
        console.log('hahaha');
    },
    test: function(){
        console.log('blah')
    }


});
new BP();