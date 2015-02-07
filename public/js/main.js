/**
 * Created by philipe on 06/02/15.
 */

$(function(){
    //constructors
    var Views = {};
    var Models = {};
    //instanciadas
    var views = {};
    var models = {};

    var map;

    Views.AddressQuery = Backbone.View.extend({
        el: $('.address_query'),
        events: {
            'click button': 'submitAddress'
        },
        initialize: function() {
            mapsHandler = new MapsHandler('map-canvas');
        },
        render: function() {
        },
        submitAddress: function(){
            var address = this.$el.find('.address').val();

            mapsHandler.geoLocate(address, function(data) {
                var result = data.results[0];
                var resultLat = result.geometry.location.lat;
                var resultLng = result.geometry.location.lng;
                var resultlatLng = new google.maps.LatLng(resultLat, resultLng);

                mapsHandler.addMarker(resultlatLng);

                mapsHandler.map.panTo(resultlatLng);

                mapsHandler.findPlaces(resultlatLng, models.placesFilters.extractData(), function(results) {
                    results.forEach(function(result) {
                        var placeLat = result.geometry.location.k || geometry.location.lat;
                        var placeLng = result.geometry.location.D || geometry.location.lng;
                        var placelatLng = new google.maps.LatLng(placeLat, placeLng);

                        mapsHandler.addMarker(placelatLng);
                    });
                });

            });
        }
    });
    Views.PlacesFilters = Backbone.View.extend({
        el: $('.places_filter'),
        events: {
            'change [type=checkbox]': 'saveToModel'
        },
        template: _.template($('#places_filter_template').html()),
        initialize: function(model) {
            this.model = model;
            this.render();
        },
        render: function(){
            this.$el.html(this.template({model:this.model}));
            return this;
        },
        saveToModel: function(e) {
            this.model.set(e.target.value, {name: e.target.dataset.name, state: e.target.checked});
        }

    });

    Models.PlacesFilters = Backbone.Model.extend({
        defaults: {
            schools: {
                state:true,
                name: 'Escolas'
            },
            transportation: {
                state:true,
                name: 'Transporte Público'
            },
            entertainment: {
                state: true,
                name: 'Entertenimento'
            },
            hospital: {
                state: true,
                name: 'Hospital'
            },
            sports: {
                state: true,
                name: 'Atividades físicas'
            },
            store: {
                state: true,
                name: 'Comércio'
            }
        },

        extractData: function() {
            var retorno = [];
            _.each(this.attributes, function(filtro, key) {
               if (filtro.state) retorno.push(key);
            });
            return retorno;
        }


    });

    models.placesFilters = new Models.PlacesFilters();

    views.addressQuery = new Views.AddressQuery();
    views.placesFilters = new Views.PlacesFilters(models.placesFilters);
});

function MapsHandler(container) {
    var Location = Backbone.Model.extend({
        defaults: {
            lat: 0,
            lng: 0
        },
        initialize: function(){
        }
    });
    var startingPos = new Location({lat:-23.5290087,lng:-46.6790629});
    var mapOptions = {
        center: new google.maps.LatLng(startingPos.get('lat'), startingPos.get('lng')),
        zoom: 15
    };
    var map = new google.maps.Map(document.getElementById(container),
        mapOptions);
    var placesService = new google.maps.places.PlacesService(map);


    function addMarker(latLng) {
        var marker = new google.maps.Marker({
            position: latLng,
            map: map
        });
    }

    function findPlaces(latLng, types, successCallback) {
        var request = {
            location: latLng,
            radius: '500',
            types: types
        };

        placesService.search(request, function(results) {
            successCallback(results);
        });
    }

    function geoLocate(address, successCallback) {
        var basePath = 'https://maps.googleapis.com/maps/api/geocode/json';
        var url = basePath + '?address=' + address;
        url += '&key=AIzaSyD5HXz_hbIdyItJYTtWtg86-aUmJOyv-oc';

        $.ajax({
            method: 'GET',
            url:url,
            success: function(data) {
                successCallback(data);
            },
            error: function(e) {
                console.log(e)
            }
        })
    }

    return {
        geoLocate: geoLocate,
        addMarker: addMarker,
        findPlaces: findPlaces,
        map: map
    }
}
var mapsHandler;
