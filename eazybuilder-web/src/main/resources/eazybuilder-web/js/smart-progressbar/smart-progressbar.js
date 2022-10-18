'use strict';

angular.module('smart-progressbar', [])
    .constant('spOptionsDefaults', {
        'background-color': 'black',
        'z-index': '2147483647',
        transition: 'all 0.5s ease',
        opacity: '0.5',
        sensitivity: 500,
        delayTrashHold: 50,
        minDuration: 700,
        spinner: 'spinner3'
    })
    .config(['$provide', '$injector', 'spOptionsDefaults', function ($provide, $injector, spOptionsDefaults) {

        var spOptions = spOptionsDefaults;
        if ($injector.has('spOptions')) {
            spOptions = angular.extend(spOptions, $injector.get('spOptions'));
        }

        $provide.decorator('ngClickDirective', [
            '$delegate', '$parse',
            function ($delegate, $parse) {
                $delegate[0].compile = function ($element, attr) {
                    var originalFn = $parse(attr['ngClick']);
                    var fn = debounce(originalFn, spOptions.sensitivity, {
                        leading: true,
                        trailing: false
                    });
                    return function ngEventHandler(scope, element) {
                        element.on('click', function (event) {
                            var callback = function () {
                                if (!attr['allowDoubleClick']) {
                                    fn(scope, {$event: event});
                                } else {
                                    originalFn(scope, {$event: event});
                                }
                            };
                            scope.$apply(callback);
                        });
                    };
                };
                return $delegate;
            }
        ]);

        $provide.decorator('$http', ['$delegate', '_AspPromiseTracker',
            function ($delegate, _AspPromiseTracker) {
                angular.forEach('get post put delete jsonp head patch'.split(' '), function (method) {
                    var original = $delegate[method];
                    $delegate[method] = function decorated() {
                        var promise = original.apply($delegate, arguments);
                        if (!(arguments[1] && arguments[1].skipProgressBar)) {
                            _AspPromiseTracker.reset({
                                promises: [promise],
                                delay: spOptions.sensitivity - spOptions.delayTrashHold,
                                minDuration: spOptions.minDuration
                            });
                        }
                        return promise;
                    };
                });
                return $delegate;
            }]);

        function debounce(func, wait, options) {
            var lastArgs,
                lastThis,
                maxWait,
                result,
                timerId,
                lastCallTime;

            var lastInvokeTime = 0;
            var leading = false;
            var maxing = false;
            var trailing = true;

            if (typeof func != 'function') {
                throw new TypeError('Expected a function')
            }
            wait = wait || 0;
            if (options) {
                leading = !!options.leading;
                maxing = 'maxWait' in options;
                maxWait = maxing ? nativeMax(toNumber(options.maxWait) || 0, wait) : maxWait;
                trailing = 'trailing' in options ? !!options.trailing : trailing
            }

            function invokeFunc(time) {
                const args = lastArgs;
                const thisArg = lastThis;

                lastArgs = lastThis = undefined;
                lastInvokeTime = time;
                result = func.apply(thisArg, args);
                return result
            }

            function leadingEdge(time) {
                // Reset any `maxWait` timer.
                lastInvokeTime = time;
                // Start the timer for the trailing edge.
                timerId = setTimeout(timerExpired, wait);
                // Invoke the leading edge.
                return leading ? invokeFunc(time) : result
            }

            function remainingWait(time) {
                const timeSinceLastCall = time - lastCallTime;
                const timeSinceLastInvoke = time - lastInvokeTime;
                const result = wait - timeSinceLastCall;

                return maxing ? nativeMin(result, maxWait - timeSinceLastInvoke) : result
            }

            function shouldInvoke(time) {
                const timeSinceLastCall = time - lastCallTime;
                const timeSinceLastInvoke = time - lastInvokeTime;

                // Either this is the first call, activity has stopped and we're at the
                // trailing edge, the system time has gone backwards and we're treating
                // it as the trailing edge, or we've hit the `maxWait` limit.
                return (lastCallTime === undefined || (timeSinceLastCall >= wait) ||
                (timeSinceLastCall < 0) || (maxing && timeSinceLastInvoke >= maxWait))
            }

            function timerExpired() {
                const time = Date.now();
                if (shouldInvoke(time)) {
                    return trailingEdge(time)
                }
                // Restart the timer.
                timerId = setTimeout(timerExpired, remainingWait(time))
            }

            function trailingEdge(time) {
                timerId = undefined;

                // Only invoke if we have `lastArgs` which means `func` has been
                // debounced at least once.
                if (trailing && lastArgs) {
                    return invokeFunc(time)
                }
                lastArgs = lastThis = undefined;
                return result
            }

            function cancel() {
                if (timerId !== undefined) {
                    clearTimeout(timerId)
                }
                lastInvokeTime = 0;
                lastArgs = lastCallTime = lastThis = timerId = undefined
            }

            function flush() {
                return timerId === undefined ? result : trailingEdge(Date.now())
            }

            function debounced() {
                const time = Date.now();
                const isInvoking = shouldInvoke(time);

                lastArgs = arguments;
                lastThis = this;
                lastCallTime = time;

                if (isInvoking) {
                    if (timerId === undefined) {
                        return leadingEdge(lastCallTime)
                    }
                    if (maxing) {
                        // Handle invocations in a tight loop.
                        timerId = setTimeout(timerExpired, wait);
                        return invokeFunc(lastCallTime)
                    }
                }
                if (timerId === undefined) {
                    timerId = setTimeout(timerExpired, wait)
                }
                return result
            }

            debounced.cancel = cancel;
            debounced.flush = flush;
            return debounced
        }
    }])
    .service('_AspPromiseTracker', ['$timeout', '$q', 'spOptionsDefaults', '$injector',
        function ($timeout, $q, spOptionsDefaults, $injector) {
            var spOptions = spOptionsDefaults;
            if ($injector.has('spOptions')) {
                spOptions = angular.extend(spOptions, $injector.get('spOptions'));
            }
            var tracker = {};
            tracker.promises = [];
            tracker.delayPromise = null;
            tracker.durationPromise = null;
            tracker.delayJustFinished = false;

            tracker.reset = function (options) {
                tracker.minDuration = options.minDuration;

                tracker.promises = [];
                angular.forEach(options.promises, function (p) {
                    if (!p || p.$aspFulfilled) {
                        return;
                    }
                    addPromiseLikeThing(p);
                });

                if (tracker.promises.length === 0) {
                    //if we have no promises then dont do the delay or duration stuff
                    return;
                }

                tracker.delayJustFinished = false;
                if (options.delay) {
                    tracker.delayPromise = $timeout(function () {
                        tracker.delayPromise = null;
                        tracker.delayJustFinished = true;
                    }, parseInt(options.delay, 10));
                }
                if (options.minDuration) {
                    tracker.durationPromise = $timeout(function () {
                        tracker.durationPromise = null;
                    }, parseInt(options.minDuration, 10) + (options.delay ? parseInt(options.delay, 10) : 0));
                }
            };

            tracker.isPromise = function (promiseThing) {
                var then = promiseThing && (promiseThing.then || promiseThing.$then ||
                    (promiseThing.$promise && promiseThing.$promise.then));

                return typeof then !== 'undefined';
            };

            tracker.callThen = function (promiseThing, success, error) {
                var promise;
                if (promiseThing.then || promiseThing.$then) {
                    promise = promiseThing;
                } else if (promiseThing.$promise) {
                    promise = promiseThing.$promise;
                } else if (promiseThing.denodeify) {
                    promise = $q.when(promiseThing);
                }

                var then = (promise.then || promise.$then);

                then.call(promise, success, error);
            };

            var addPromiseLikeThing = function (promise) {

                if (!tracker.isPromise(promise)) {
                    throw new Error('asp expects a promise (or something that has a .promise or .$promise');
                }

                if (tracker.promises.indexOf(promise) !== -1) {
                    return;
                }
                tracker.promises.push(promise);

                tracker.callThen(promise, function () {
                    promise.$aspFulfilled = true;
                    if (tracker.promises.indexOf(promise) === -1) {
                        return;
                    }
                    tracker.promises.splice(tracker.promises.indexOf(promise), 1);
                }, function () {
                    promise.$aspFulfilled = true;
                    if (tracker.promises.indexOf(promise) === -1) {
                        return;
                    }
                    tracker.promises.splice(tracker.promises.indexOf(promise), 1);
                });
            };

            tracker.active = function () {
                if (tracker.delayPromise) {
                    return false;
                }

                if (!tracker.delayJustFinished) {
                    if (tracker.durationPromise) {
                        return true;
                    }
                    return tracker.promises.length > 0;
                } else {
                    //if both delay and min duration are set,
                    //we don't want to initiate the min duration if the
                    //promise finished before the delay was compvare
                    tracker.delayJustFinished = false;
                    if (tracker.promises.length === 0) {
                        tracker.durationPromise = null;
                    }
                    return tracker.promises.length > 0;
                }
            };

            return tracker;
        }])
    .run(function ($rootScope, _AspPromiseTracker, spOptionsDefaults, $injector) {
        var spOptions = spOptionsDefaults;
        if ($injector.has('spOptions')) {
            spOptions = angular.extend(spOptions, $injector.get('spOptions'));
        }

        var progress = angular
            .element('<div class="loader-wrapper"><div class="loader">Loading...</div></div>');
        angular.element(document.body).append(progress);

        angular.element(document.head).append(angular.element('<style type=\"text/css\">.loader-wrapper{' +
            'z-index: ' + spOptions['z-index'] + ';' +
            'transition: ' + spOptions.transition + ';' +
            'background-color: ' + spOptions['background-color'] + ';' +
            'position: fixed;top: 0;left: 0;right: 0;bottom: 0;visibility: hidden;}' + '.loader-wrapper .loader{margin:auto;position: absolute;top: 0;left: 0;right: 0;bottom: 0;}</style>'));

        if (spOptions.spinner == 'spinner1') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader,.loader:after,.loader:before{background:#fff;-webkit-animation:load1 1s infinite ease-in-out;animation:load1 1s infinite ease-in-out;width:1em;height:4em}.loader{color:#fff;text-indent:-9999em;margin:88px auto;position:relative;font-size:8px;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0);-webkit-animation-delay:-.16s;animation-delay:-.16s}.loader:after,.loader:before{position:absolute;top:0;content:''}.loader:before{left:-1.5em;-webkit-animation-delay:-.32s;animation-delay:-.32s}.loader:after{left:1.5em}@-webkit-keyframes load1{0%,100%,80%{box-shadow:0 0;height:4em}40%{box-shadow:0 -2em;height:5em}}@keyframes load1{0%,100%,80%{box-shadow:0 0;height:4em}40%{box-shadow:0 -2em;height:5em}}" +
                "</style>"));
        } else if (spOptions.spinner == 'spinner2') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader{color:#fff;font-size:8px;margin:100px auto;width:1em;height:1em;border-radius:50%;position:relative;text-indent:-9999em;-webkit-animation:load4 1.3s infinite linear;animation:load4 1.3s infinite linear;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0)}@-webkit-keyframes load4{0%,100%{box-shadow:0 -3em 0 .2em,2em -2em 0 0,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 0}12.5%{box-shadow:0 -3em 0 0,2em -2em 0 .2em,3em 0 0 0,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}25%{box-shadow:0 -3em 0 -.5em,2em -2em 0 0,3em 0 0 .2em,2em 2em 0 0,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}37.5%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 0,2em 2em 0 .2em,0 3em 0 0,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}50%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 0,0 3em 0 .2em,-2em 2em 0 0,-3em 0 0 -1em,-2em -2em 0 -1em}62.5%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 0,-2em 2em 0 .2em,-3em 0 0 0,-2em -2em 0 -1em}75%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 0,-3em 0 0 .2em,-2em -2em 0 0}87.5%{box-shadow:0 -3em 0 0,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 0,-3em 0 0 0,-2em -2em 0 .2em}}@keyframes load4{0%,100%{box-shadow:0 -3em 0 .2em,2em -2em 0 0,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 0}12.5%{box-shadow:0 -3em 0 0,2em -2em 0 .2em,3em 0 0 0,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}25%{box-shadow:0 -3em 0 -.5em,2em -2em 0 0,3em 0 0 .2em,2em 2em 0 0,0 3em 0 -1em,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}37.5%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 0,2em 2em 0 .2em,0 3em 0 0,-2em 2em 0 -1em,-3em 0 0 -1em,-2em -2em 0 -1em}50%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 0,0 3em 0 .2em,-2em 2em 0 0,-3em 0 0 -1em,-2em -2em 0 -1em}62.5%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 0,-2em 2em 0 .2em,-3em 0 0 0,-2em -2em 0 -1em}75%{box-shadow:0 -3em 0 -1em,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 0,-3em 0 0 .2em,-2em -2em 0 0}87.5%{box-shadow:0 -3em 0 0,2em -2em 0 -1em,3em 0 0 -1em,2em 2em 0 -1em,0 3em 0 -1em,-2em 2em 0 0,-3em 0 0 0,-2em -2em 0 .2em}}" +
                "</style>"));

        } else if (spOptions.spinner == 'spinner3') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader{margin:100px auto;font-size:8px;width:1em;height:1em;border-radius:50%;position:relative;text-indent:-9999em;-webkit-animation:load5 1.1s infinite ease;animation:load5 1.1s infinite ease;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0)}@-webkit-keyframes load5{0%,100%{box-shadow:0 -2.6em 0 0 #fff,1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.5),-1.8em -1.8em 0 0 rgba(255,255,255,.7)}12.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.7),1.8em -1.8em 0 0 #fff,2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.5)}25%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.5),1.8em -1.8em 0 0 rgba(255,255,255,.7),2.5em 0 0 0 #fff,1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}37.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.5),2.5em 0 0 0 rgba(255,255,255,.7),1.75em 1.75em 0 0 #fff,0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}50%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.5),1.75em 1.75em 0 0 rgba(255,255,255,.7),0 2.5em 0 0 #fff,-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}62.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.5),0 2.5em 0 0 rgba(255,255,255,.7),-1.8em 1.8em 0 0 #fff,-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}75%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.5),-1.8em 1.8em 0 0 rgba(255,255,255,.7),-2.6em 0 0 0 #fff,-1.8em -1.8em 0 0 rgba(255,255,255,.2)}87.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.5),-2.6em 0 0 0 rgba(255,255,255,.7),-1.8em -1.8em 0 0 #fff}}@keyframes load5{0%,100%{box-shadow:0 -2.6em 0 0 #fff,1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.5),-1.8em -1.8em 0 0 rgba(255,255,255,.7)}12.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.7),1.8em -1.8em 0 0 #fff,2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.5)}25%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.5),1.8em -1.8em 0 0 rgba(255,255,255,.7),2.5em 0 0 0 #fff,1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}37.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.5),2.5em 0 0 0 rgba(255,255,255,.7),1.75em 1.75em 0 0 #fff,0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}50%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.5),1.75em 1.75em 0 0 rgba(255,255,255,.7),0 2.5em 0 0 #fff,-1.8em 1.8em 0 0 rgba(255,255,255,.2),-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}62.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.5),0 2.5em 0 0 rgba(255,255,255,.7),-1.8em 1.8em 0 0 #fff,-2.6em 0 0 0 rgba(255,255,255,.2),-1.8em -1.8em 0 0 rgba(255,255,255,.2)}75%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.5),-1.8em 1.8em 0 0 rgba(255,255,255,.7),-2.6em 0 0 0 #fff,-1.8em -1.8em 0 0 rgba(255,255,255,.2)}87.5%{box-shadow:0 -2.6em 0 0 rgba(255,255,255,.2),1.8em -1.8em 0 0 rgba(255,255,255,.2),2.5em 0 0 0 rgba(255,255,255,.2),1.75em 1.75em 0 0 rgba(255,255,255,.2),0 2.5em 0 0 rgba(255,255,255,.2),-1.8em 1.8em 0 0 rgba(255,255,255,.5),-2.6em 0 0 0 rgba(255,255,255,.7),-1.8em -1.8em 0 0 #fff}}" +
                "</style>"))
        } else if (spOptions.spinner == 'spinner4') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader{color:#fff;font-size:30px;text-indent:-9999em;overflow:hidden;width:1em;height:1em;border-radius:50%;margin:72px auto;position:relative;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0);-webkit-animation:load6 1.7s infinite ease,round 1.7s infinite ease;animation:load6 1.7s infinite ease,round 1.7s infinite ease}@-webkit-keyframes load6{0%,100%,5%,95%{box-shadow:0 -.83em 0 -.4em,0 -.83em 0 -.42em,0 -.83em 0 -.44em,0 -.83em 0 -.46em,0 -.83em 0 -.477em}10%,59%{box-shadow:0 -.83em 0 -.4em,-.087em -.825em 0 -.42em,-.173em -.812em 0 -.44em,-.256em -.789em 0 -.46em,-.297em -.775em 0 -.477em}20%{box-shadow:0 -.83em 0 -.4em,-.338em -.758em 0 -.42em,-.555em -.617em 0 -.44em,-.671em -.488em 0 -.46em,-.749em -.34em 0 -.477em}38%{box-shadow:0 -.83em 0 -.4em,-.377em -.74em 0 -.42em,-.645em -.522em 0 -.44em,-.775em -.297em 0 -.46em,-.82em -.09em 0 -.477em}}@keyframes load6{0%,100%,5%,95%{box-shadow:0 -.83em 0 -.4em,0 -.83em 0 -.42em,0 -.83em 0 -.44em,0 -.83em 0 -.46em,0 -.83em 0 -.477em}10%,59%{box-shadow:0 -.83em 0 -.4em,-.087em -.825em 0 -.42em,-.173em -.812em 0 -.44em,-.256em -.789em 0 -.46em,-.297em -.775em 0 -.477em}20%{box-shadow:0 -.83em 0 -.4em,-.338em -.758em 0 -.42em,-.555em -.617em 0 -.44em,-.671em -.488em 0 -.46em,-.749em -.34em 0 -.477em}38%{box-shadow:0 -.83em 0 -.4em,-.377em -.74em 0 -.42em,-.645em -.522em 0 -.44em,-.775em -.297em 0 -.46em,-.82em -.09em 0 -.477em}}@-webkit-keyframes round{0%{-webkit-transform:rotate(0);transform:rotate(0)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes round{0%{-webkit-transform:rotate(0);transform:rotate(0)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}" +
                "</style>"));
        } else if (spOptions.spinner == 'spinner5') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader,.loader:after,.loader:before{border-radius:50%;width:2.5em;height:2.5em;-webkit-animation:load7 1.8s infinite ease-in-out;animation:load7 1.8s infinite ease-in-out}.loader{color:#fff;font-size:5px;margin:80px auto;position:relative;text-indent:-9999em;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0);-webkit-animation-delay:-.16s;animation-delay:-.16s}.loader:after,.loader:before{content:'';position:absolute;top:0}.loader:before{left:-3.5em;-webkit-animation-delay:-.32s;animation-delay:-.32s}.loader:after{left:3.5em}@-webkit-keyframes load7{0%,100%,80%{box-shadow:0 2.5em 0 -1.3em}40%{box-shadow:0 2.5em 0 0}}@keyframes load7{0%,100%,80%{box-shadow:0 2.5em 0 -1.3em}40%{box-shadow:0 2.5em 0 0}}" +
                "</style>"));
        } else if (spOptions.spinner == 'spinner6') {
            angular.element(document.head).append(angular.element("<style type=\"text/css\">" +
                ".loader,.loader:after{border-radius:50%;width:10em;height:10em}.loader{margin:60px auto;font-size:5px;position:relative;text-indent:-9999em;border-top:1.1em solid rgba(255,255,255,.2);border-right:1.1em solid rgba(255,255,255,.2);border-bottom:1.1em solid rgba(255,255,255,.2);border-left:1.1em solid #fff;-webkit-transform:translateZ(0);-ms-transform:translateZ(0);transform:translateZ(0);-webkit-animation:load8 1.1s infinite linear;animation:load8 1.1s infinite linear}@-webkit-keyframes load8{0%{-webkit-transform:rotate(0);transform:rotate(0)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes load8{0%{-webkit-transform:rotate(0);transform:rotate(0)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}" +
                "</style>"));
        }
        $rootScope.$watch(_AspPromiseTracker.active, function (isActive) {
            if (isActive) {
                progress.css({visibility: 'visible', opacity: spOptions.opacity});
            } else {
                progress.css({visibility: 'hidden', opacity: '0'})
            }
        })
    })
;