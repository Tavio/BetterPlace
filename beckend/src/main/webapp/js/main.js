/**
 * Created by philipe on 06/02/15.
 */

$(function(){
    //wrappers de constructors
    var Views = {};
    var Models = {};
    var Collections = {};
    //wrappers de instanciadas
    var views = {};
    var models = {};
    var collections = {};

    var typesMap = {
        "school" 					: "school",
        "university" 				: "school",
        "bus_station" 				: "bus_station",
        "subway_station"			: "bus_station",
        "airport" 					: "bus_station",
        "taxi_stand" 				: "bus_station",
        "train_station" 			: "bus_station",
        "gym" 						: "gym",
        "spa" 						: "gym",
        "health" 					: "health",
        "doctor" 					: "health",
        "hospital" 					: "health",
        "physiotherapist" 			: "health",
        "veterinary_care" 			: "health",
        "park" 						: "park",
        "store" 					: "store",
        "bakery"					: "store",
        "bar" 						: "store",
        "beauty_salon"				: "store",
        "bicycle_store"				: "store",
        "book_store"				: "store",
        "car_dealer"				: "store",
        "clothing_store"			: "store",
        "convenience_store"			: "store",
        "department_store"			: "store",
        "electronics_store"			: "store",
        "furniture_store"			: "store",
        "grocery_or_supermarket"	: "store",
        "hardware_store"			: "store",
        "home_goods_store"			: "store",
        "shoe_store"				: "store",
        "shopping_mall"				: "store"
    };
    var mapsHandler;

    // **** Collections
    Collections.Comments = Backbone.Collection.extend({});

    // **** Models
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
    Models.Comment = Backbone.Model.extend({});
    Models.CurrentLocation = Backbone.Model.extend({
        defaults: {
            latitude: 0,
            longitude: 0,
            neighborhoodName: '',
            endereco: ''
        },
        initialize: function(){
            this.listenTo(this, 'change:endereco', function(model,address){
                mapsHandler.geoLocate(address, function(data) {
                    var bairro;
                    data.results[0].address_components.forEach(function(component) {
                        if (component.types[0] === 'neighborhood') {
                            bairro = component.short_name;
                            models.currentLocation.set('neighborhoodName', bairro);
                        }
                    });
                    if (bairro) views.neighborhoodComments = new Views.NeighborhoodComments(bairro);
                    var result = data.results[0];
                    var resultLat = result.geometry.location.lat;
                    var resultLng = result.geometry.location.lng;
                    models.currentLocation.set('latitude', resultLat);
                    models.currentLocation.set('longitude', resultLng);
                    var resultlatLng = new google.maps.LatLng(resultLat, resultLng);

                    mapsHandler.addAddressMarker(resultlatLng);
                    mapsHandler.map.panTo(resultlatLng);
                    mapsHandler.clearPlaceMarkers();
                    mapsHandler.findPlaces(resultlatLng, models.placesFilters.extractData(), function(results) {
                        results.forEach(function(result) {
                            var placeLat = result.geometry.location.k || geometry.location.lat;
                            var placeLng = result.geometry.location.D || geometry.location.lng;
                            var placelatLng = new google.maps.LatLng(placeLat, placeLng);
                            var iconUrl = "img/" + typesMap[result.types[0]] + ".png";
                            mapsHandler.addPlaceMarker(placelatLng, iconUrl, result);
                        });
                    });

                });
            })
        }
    });

    // **** Views
    Views.AddressQuery = Backbone.View.extend({
        el: $('.address_query'),
        events: {
            'click button': 'submitAddress'
        },
        initialize: function() {
            mapsHandler = new MapsHandler('map-canvas', models.currentLocation);
        },
        render: function() {
        },
        submitAddress: function(){
            models.currentLocation.set('endereco', this.$el.find('.address').val());

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
    Views.NeighborhoodComments = Backbone.View.extend({
        el: $('.neighborhood_comments'),
        events: {
            'change [type=checkbox]': 'saveToModel',
            'click .addComment' : 'setModalContent'
        },
        template: _.template($('#neighborhood_comments_template').html()),
        initialize: function(neighborhood) {
            this.neighborhood = neighborhood;
            this.fetchComments(neighborhood);
            this.listenToOnce(collections.comments, 'reset', function(collection){
                this.render(neighborhood, collection);
            });
            this.listenTo(collections.comments, 'add', function(){
                this.render(neighborhood, collections.comments);
            });
            return this
        },
        render: function(neighborhood, comments){
            this.$el.html(this.template({neighborhood: neighborhood, commentsCollection: comments}));
            return this;
        },
        fetchComments: function(neighborhood, callback) {
            var view = this;
            $.getJSON("/mock.json", function(data) {
                if (data && data.length && !data.error) {
                    collections.comments.reset(data);
                }
            });
            return this;
        },
        setModalContent: function() {
            new Views.SubmmitComment();
        }

    });
    Views.SubmmitComment = Backbone.View.extend({
        el: $('.modal-content'),
        template: _.template($('#submit_comment_template').html()),
        events: {
            'submit form'   : 'submitData',
            'keyup input'   : 'saveToModel',
            'keyup textarea': 'saveToModel'
        },
        initialize: function(){
            this.comment = new Models.Comment(models.currentLocation.attributes);
            this.render();
            var view = this;
            $('#myModal').on('hide.bs.modal', function(){
                $('#myModal').off('hide.bs.modal');
                view.removeModal(view);
            });
            return this;
        },
        render: function(){
            this.$el.html(this.template());
            return this;
        },
        submitData: function(e){
            e.preventDefault();
            var url = '/private/review/?';
            _.each(this.comment.attributes, function(val, key) {
                url += key + '=' + escape(val) + '&'
            });
            //url = '/private/review/?lat=-23.5290087&lng=-46.6790629&latitude=-23.6573994&longitude=-46.706191&neighborhoodName=Santo%20Amaro&endereco=alameda%20santo%20amaro&comment=asdasd&reviewerName=Jos%E9a&reviewerEmail=jose@fino.comm&';
            $.ajax({
                type: 'PUT',
                url: url,
                success: function(data) {
                    console.log(data)
                },
                error: function(data) {
                    console.log(data)
                }
            });
        },
        saveToModel: function(e) {
            this.comment.set(e.target.id, e.target.value);
        },
        removeModal: function(view){
            view.undelegateEvents()
            view.unbind();

        }
    });

    // **** Inicializando
    collections.comments = new Collections.Comments();
    models.placesFilters = new Models.PlacesFilters();
    models.currentLocation = new Models.CurrentLocation({lat:-23.5290087,lng:-46.6790629});
    views.addressQuery = new Views.AddressQuery();
    views.placesFilters = new Views.PlacesFilters(models.placesFilters);

    function MapsHandler(container, initialLocation) {
        var startingPos = initialLocation;
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

        function addPlaceMarker(latLng, icon, place) {
            var marker = addMarker(latLng, icon);
            placeMarkers.push(marker);
            addPlaceInfo(place, marker)
        }

        function addPlaceInfo(place, marker) {
            var content = '<div id="title"><h3>' + place.name + '</h3>';
            var infowindow = new google.maps.InfoWindow({
                content: content
            });
            google.maps.event.addListener(marker, 'click', function() {
                infowindow.open(map,marker);
            });
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
});
