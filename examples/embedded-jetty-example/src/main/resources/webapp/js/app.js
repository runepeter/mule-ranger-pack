var app = angular.module('MyApp', ['ngAnimate', 'ui.bootstrap']);

app.factory('MyService', ['$q', '$rootScope', function ($q, $rootScope) {
    // We return this object to anything injecting our service
    var Service = {
        currentUser: currentUser
    };
    // Keep all pending requests here until they get responses
    var callbacks = {};
    // Create a unique callback ID to map requests to responses
    var currentCallbackId = 0;

    var ws = {
        close: function() {
        }
    };


    var eventCallback = function () {
    };

    function sendRequest(request) {
        var defer = $q.defer();
        var callbackId = getCallbackId();
        callbacks[callbackId] = {
            time:new Date(),
            cb:defer
        };
        request.callback_id = callbackId;
        console.log('Sending request', request);
        ws.send(JSON.stringify(request));
        return defer.promise;
    }

    function listener(data) {
        var messageObj = data;
        console.log("Received data from websocket: ", messageObj.data);
        // If an object exists with callback_id in our callbacks object, resolve it
        if (callbacks.hasOwnProperty(messageObj.callback_id)) {
            console.log(callbacks[messageObj.callback_id]);
            var resolve = callbacks[messageObj.callback_id].cb.resolve(messageObj.data);
            $rootScope.$apply(resolve);
            delete callbacks[messageObj.callbackID];
        } else {
            $rootScope.$apply(function () {
                eventCallback(messageObj.data);
            });
        }
    }

    // This creates a new callback ID for a request
    function getCallbackId() {
        currentCallbackId += 1;
        if (currentCallbackId > 10000) {
            currentCallbackId = 0;
        }
        return currentCallbackId;
    }

    Service.connectTo = function(feed) {

         ws.close();

         ws = new WebSocket("ws://127.0.0.1:8080/mule/" + feed);

         ws.onopen = function () {
                 console.log("Socket has been opened!");

                 var request = {
                     type: 'get_results'
                 };

                 sendRequest(request);
         };

         ws.onmessage = function (message) {
            listener(JSON.parse(message.data));
         };
    }

    Service.subscribeLeague = function(league) {
        var request = {
            type:"subscribe_league",
            arguments: {
                league: league
            }
        };
        return sendRequest(request);
    };

    Service.on = function (event, callback) {
        eventCallback = callback;
    };

    return Service;
}]);

app.directive('shake', function(
  $timeout,
  $window,
  $animate
) {
  return {
    restrict: 'A',
    scope: {
        when: '=when',
    },
    link: function(scope, element, attrs) {
        scope.$watch('when', function(value, oldValue) {
            if (value.homeChanged || value.awayChanged) {

                $('#sounds')[0].play();

                $animate.addClass(element, 'animated shake', function() {
                    $timeout(function() {
                        $animate.removeClass(element, 'animated shake');
                    }, 5000);
                });

                if (value.homeChanged) {
                    jQuery(element).find(".homeScore").removeClass("label-default").addClass("label-success animate wobble").delay(5000).queue(function() {
                        $(this).removeClass("label-success animate wobble").addClass("label-default");
                        $(this).dequeue();
                    });
                }

                if (value.awayChanged) {
                    jQuery(element).find(".awayScore").removeClass("label-default").addClass("label-success animate wobble").delay(5000).queue(function() {
                        $(this).removeClass("label-success animate wobble").addClass("label-default");
                        $(this).dequeue();
                    });
                }
            }
        });
    }
  };
});

function SoccerCtrl($scope, MyService) {

    $scope.currentUser = MyService.currentUser;

    $scope.resultMap = {}
    $scope.results = function() {
        return $.map($scope.resultMap, function(v, k) {return v;});
    }

    $scope.matchSorter = function(match) {

        if (!match) {
            return "N/A";
        }

        var time = (match.time + "");
        if (time.contains('\'')) {
            return '0' + time;
        }

        if (time.contains(':')) {
            return '1' + time;
        }

        if (time.contains(':')) {
            return '2' + time;
        }

        if (time == "HT") {
            return '3' + time;
        }

        if (time == "FT") {
            return '4' + time;
        }

        return '5' + time;
    }

    $scope.feed = 'ALL'; // default feed 'receive all results'

    // connect to feed when feedChange has been observed.
    $scope.$watch('feed', function() {

        console.log("Connecting to feed -> " + $scope.feed);

        $scope.resultMap = {};

        if ('SUB' == $scope.feed) {
            MyService.connectTo('personal');
        } else {
            MyService.connectTo('all');
        }

    }, true);

    MyService.on('event', function (data) {

        if (data.length == undefined) {
            data = new Array(data);
        }

        for (var i=0;i<data.length;i++) {

            var d = data[i];

            var old = $scope.resultMap[d.soccerMatch.key]
            if (old) {
                if (old.home != d.home) d.homeChanged = true;
                if (old.away != d.away) d.awayChanged = true;
                if (old.time != d.time) d.timeChanged = true;
            } else {
                d.homeChanged = true;
                d.awayChanged = true;
                d.timeChanged = true;
            }

            $scope.resultMap[d.soccerMatch.key] = d;
        }
    });

    $scope.subscribeLeague = function(league) {
        console.log("Subscribing to league '" + league + "'.");
        MyService.subscribeLeague(league);
    }
}