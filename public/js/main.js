/**
 * Created by philipe on 06/02/15.
 */

$(function(){
    var Views = {};

    var map;
    var placesService;

    Views.AddressQuery = Backbone.View.extend({
        el: $('.address_query'),

        events: {
            'click button': 'submitAddress'
        },

        initialize: function() {
            console.log('initialize', this.$el);
            mapsHandler = new MapsHandler('map-canvas');
        },

        render: function() {
        },
        submitAddress: function(){
            var address = this.$el.find('.address').val();
            console.log(mapsHandler)
            mapsHandler.geoLocate(address, function(data) {
                var result = data.results[0];
                var resultLat = result.geometry.location.lat;
                var resultLng = result.geometry.location.lng;
                var resultlatLng = new google.maps.LatLng(resultLat, resultLng);

                mapsHandler.addMarker(resultlatLng);

                map.panTo(resultlatLng);

                mapsHandler.findPlaces(resultlatLng, function(results) {
                    results.forEach(function(result) {
                        var placeLat = result.geometry.location.lat;
                        var placeLng = result.geometry.location.lng;
                        var placelatLng = new google.maps.LatLng(placeLat, placeLng);

                        mapsHandler.addMarker(placelatLng);
                    });
                });

            });
        }
    });

    function initialize() {
       var startingPos = new Location({lat:-23.5290087,lng:-46.6790629});

        var mapOptions = {
            center: new google.maps.LatLng(startingPos.get('lat'), startingPos.get('lng')),
            zoom: 15
        };
        map = new google.maps.Map(document.getElementById('map-canvas'),
            mapOptions);
        placesService = new google.maps.places.PlacesService(map);
    }
    google.maps.event.addDomListener(window, 'load', initialize);

    new Views.AddressQuery();

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

    function findPlaces(latLng, successCallback) {
        var request = {
            location: latLng,
            radius: '500',
            types: ['store']
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
        findPlaces: findPlaces
    }
}
var mapsHandler;
//google.maps.event.addDomListener(window, 'load', );
