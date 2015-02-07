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

                mapsHandler.addAddressMarker(resultlatLng);

                mapsHandler.map.panTo(resultlatLng);
                mapsHandler.clearPlaceMarkers();

                mapsHandler.findPlaces(resultlatLng, models.placesFilters.extractData(), function(results) {
                    results.forEach(function(result) {
                        var placeLat = result.geometry.location.k || geometry.location.lat;
                        var placeLng = result.geometry.location.D || geometry.location.lng;
                        var placelatLng = new google.maps.LatLng(placeLat, placeLng);
                        var iconUrl = "img/" + result.types[0] + ".png";
                        mapsHandler.addPlaceMarker(placelatLng, iconUrl);
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
            school: {
                state:true,
                name: 'Escolas'
            },
            bus_station: {
                state:true,
                name: 'Transporte Público'
            },
            gym: {
                state: true,
                name: 'Academias'
            },
            health: {
                state: true,
                name: 'Hospital'
            },
            park: {
                state: true,
                name: 'Parques'
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

    var placeMarkers = [];
    var addressMarker;

    function addAddressMarker(latLng) {
        if(addressMarker) {
            addressMarker.setMap(null);
        }

        addressMarker= addMarker(latLng);
    }

    function addPlaceMarker(latLng, icon) {
        placeMarkers.push(addMarker(latLng, icon));
    }

    function addMarker(latLng, icon) {

        var markerOptions = {
            position: latLng,
            map: map
        };

        if(icon) {
            markerOptions.icon = icon;
        }

        return new google.maps.Marker(markerOptions);
    }

    function clearPlaceMarkers() {
        placeMarkers.forEach(function(marker) {
           marker.setMap(null);
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
        addAddressMarker: addAddressMarker,
        addPlaceMarker: addPlaceMarker,
        clearPlaceMarkers: clearPlaceMarkers,
        findPlaces: findPlaces,
        map: map
    }
}
var mapsHandler;
