'use strict';

/**
 * @ngdoc module
 * @name ng-bootstrap-select
 * @description
 * ng-bootstrap-select.
 */

angular.module('ng-bootstrap-select', [])
    .directive('selectpicker', ['$parse', '$timeout', selectpickerDirective]);

/**
 * @ngdoc directive
 * @name selectpicker
 * @restrict A
 *
 * @param {object} selectpicker Directive attribute to configure bootstrap-select, full configurable params can be found in [bootstrap-select docs](http://silviomoreto.github.io/bootstrap-select/).
 * @param {string} ngModel Assignable angular expression to data-bind to.
 *
 * @description
 * The selectpicker directive is used to wrap bootstrap-select.
 *
 * @usage
 * ```html
 * <select selectpicker ng-model="model">
 *   <option value="">Select one</option>
 *   <option>Mustard</option>
 *   <option>Ketchup</option>
 *   <option>Relish</option>
 * </select>
 *
 * <select selectpicker="{dropupAuto:false}" ng-model="model">
 *   <option value="">Select one</option>
 *   <option>Mustard</option>
 *   <option>Ketchup</option>
 *   <option>Relish</option>
 * </select>
 * ```
 */

function isWindow(obj) {
    return obj && obj.window === obj;
}

var isArray = Array.isArray;

function isArrayLike(obj) {

    // `null`, `undefined` and `window` are not array-like
    if (obj == null || isWindow(obj)) return false;

    // arrays, strings and jQuery/jqLite objects are array like
    // * jqLite is either the jQuery or jqLite constructor function
    // * we have to check the existance of jqLite first as this method is called
    //   via the forEach method when constructing the jqLite object in the first place
    if (isArray(obj) || isString(obj) || (jqLite && obj instanceof jqLite)) return true;

    // Support: iOS 8.2 (not reproducible in simulator)
    // "length" in obj used to prevent JIT error (gh-11508)
    var length = "length" in Object(obj) && obj.length;

    // NodeList objects (with `item` method) and
    // other objects with suitable length characteristics are array-like
    return isNumber(length) &&
        (length >= 0 && ((length - 1) in obj || obj instanceof Array) || typeof obj.item == 'function');

}

var NG_OPTIONS_REGEXP = /^\s*([\s\S]+?)(?:\s+as\s+([\s\S]+?))?(?:\s+group\s+by\s+([\s\S]+?))?(?:\s+disable\s+when\s+([\s\S]+?))?\s+for\s+(?:([\$\w][\$\w]*)|(?:\(\s*([\$\w][\$\w]*)\s*,\s*([\$\w][\$\w]*)\s*\)))\s+in\s+([\s\S]+?)(?:\s+track\s+by\s+([\s\S]+?))?$/;
// 1: value expression (valueFn)
// 2: label expression (displayFn)
// 3: group by expression (groupByFn)
// 4: disable when expression (disableWhenFn)
// 5: array item variable name
// 6: object item key variable name
// 7: object item value variable name
// 8: collection expression
// 9: track by expression

function selectpickerDirective($parse, $timeout) {
    return {
        restrict: 'A',
        priority: 1000,
        link: function (scope, element, attrs) {
            var $async = scope.$applyAsync ? '$applyAsync' : '$evalAsync', // fall back to $evalAsync if using AngularJS v1.2.x
                selectCollection;

            if (attrs.ngOptions) {
                var multiple = attrs.multiple,
                    optionsExp = attrs.ngOptions,
                    optionAttrs = $parse(attrs.bsOptionAttrs)(scope),
                    emptyOption,
                    unknownOption,
                    match = optionsExp.match(NG_OPTIONS_REGEXP);

                // The collection to watch
                selectCollection = match[8];
                // The variable name for the value of the item in the collection
                var valueName = match[5] || match[7];
                // The variable name for the key of the item in the collection
                var keyName = match[6];

                // An expression that generates the viewValue for an option if there is a label expression
                var selectAs = / as /.test(match[0]) && match[1];
                // An expression that is used to track the id of each object in the options collection
                var trackBy = match[9];
                // An expression that generates the viewValue for an option if there is no label expression
                var valueFn = $parse(match[2] ? match[1] : valueName);
                var selectAsFn = selectAs && $parse(selectAs);
                var viewValueFn = selectAsFn || valueFn;
                var trackByFn = trackBy && $parse(trackBy);

                // Get the value by which we are going to track the option
                // if we have a trackFn then use that (passing scope and locals)
                // otherwise just hash the given viewValue
                var getTrackByValueFn = trackBy ?
                    function(value, locals) { return trackByFn(scope, locals); } :
                    function getHashOfValue(value) { return hashKey(value); };
                var getTrackByValue = function(value, key) {
                    return getTrackByValueFn(value, getLocals(value, key));
                };

                var displayFn = $parse(match[2] || match[1]);
                var groupByFn = $parse(match[3] || '');
                var disableWhenFn = $parse(match[4] || '');
                var valuesFn = $parse(selectCollection);

                var locals = {};
                var getLocals = keyName ? function(value, key) {
                    locals[keyName] = key;
                    locals[valueName] = value;
                    return locals;
                } : function(value) {
                    locals[valueName] = value;
                    return locals;
                };

                scope[$async](function () {
                    element.selectpicker($parse(attrs.selectpicker)());
                });
            } else {
                selectCollection = attrs.selectCollection;

                element.selectpicker($parse(attrs.selectpicker)());

                $timeout(function () { // fall back to $timeout for selects that don't use ng-options
                    refresh();
                });
            }

            if (selectCollection) scope.$watch(selectCollection, refresh, true);

            function bindData(text) {
                var startIndex,
                    endIndex,
                    index = 0,
                    expressions = [],
                    parseFns = [],
                    textLength = text ? text.length : 0,
                    exp,
                    concat = [],
                    startSymbol = '{{',
                    endSymbol = '}}',
                    startSymbolLength = startSymbol.length,
                    endSymbolLength = endSymbol.length,
                    escapedStartRegexp = new RegExp(startSymbol.replace(/./g, escape), 'g'),
                    escapedEndRegexp = new RegExp(endSymbol.replace(/./g, escape), 'g'),
                    expressionPositions = [];

                function escape(ch) {
                    return '\\\\\\' + ch;
                }

                function unescapeText(text) {
                    return text.replace(escapedStartRegexp, startSymbol).
                    replace(escapedEndRegexp, endSymbol);
                }

                while (index < textLength) {
                    if (((startIndex = text.indexOf(startSymbol, index)) != -1) &&
                        ((endIndex = text.indexOf(endSymbol, startIndex + startSymbolLength)) != -1)) {
                        if (index !== startIndex) {
                            concat.push(unescapeText(text.substring(index, startIndex)));
                        }
                        exp = text.substring(startIndex + startSymbolLength, endIndex);
                        expressions.push(exp);
                        index = endIndex + endSymbolLength;
                        expressionPositions.push(concat.length);
                        concat.push('');
                    } else {
                        // we did not find an interpolation, so we have to add the remainder to the separators array
                        if (index !== textLength) {
                            concat.push(unescapeText(text.substring(index)));
                        }
                        break;
                    }
                }

                return function(context, locals) {
                    for (var i = 0, ii = expressions.length; i < ii; i++) {
                        concat[expressionPositions[i]] = $parse(expressions[i])(context, locals);
                    }

                    return concat.join('');
                }
            }

            function setAttributes() {
                if (typeof optionAttrs === 'object' && $.isEmptyObject(optionAttrs)) return;

                function getOptionValuesKeys(optionValues) {
                    var optionValuesKeys;

                    if (!keyName && isArrayLike(optionValues)) {
                        optionValuesKeys = optionValues;
                    } else {
                        // if object, extract keys, in enumeration order, unsorted
                        optionValuesKeys = [];
                        for (var itemKey in optionValues) {
                            if (optionValues.hasOwnProperty(itemKey) && itemKey.charAt(0) !== '$') {
                                optionValuesKeys.push(itemKey);
                            }
                        }
                    }
                    return optionValuesKeys;
                }

                var optionValues = valuesFn(scope) || [],
                    optionValuesKeys = getOptionValuesKeys(optionValues);

                emptyOption = [];
                unknownOption = [];

                // find empty or unknown options
                for (var i = 0, children = element.find('option'), ii = children.length; i < ii; i++) {
                    var value = children[i].value,
                        option = children.eq(i);

                    if (value === '') {
                        emptyOption.push(option);
                    } else if (value === '?') {
                        unknownOption.push(option);
                    } else {
                        break;
                    }
                }

                if (emptyOption.length > 1) emptyOption[1].remove();

                element.find('option').each(function(i) {
                    if (emptyOption.length) i--;
                    if (unknownOption.length) i--;

                    var locals = {},
                        newAttrs = {},
                        newData = {},
                        key = (optionValues === optionValuesKeys) ? i : optionValuesKeys[i],
                        value = optionValues[key];

                    locals = getLocals(value, key);

                    if (attrs.bsOptionAttrs && (keyName && key || !keyName && value)) {
                        var customAttrs = typeof optionAttrs === 'function' ? optionAttrs(value, key) : optionAttrs;

                        if ($.isEmptyObject(customAttrs)) return;

                        for (var optionAttr in customAttrs) {
                            var attr = customAttrs[optionAttr],
                                dataAttr = optionAttr.split('data-')[1] ? optionAttr.split('data-')[1].replace(/-([a-z])/g, function (g) { return g[1].toUpperCase(); }) : null, // convert to camelCase
                                parseAttr = bindData(attr)(scope, locals);

                            if (dataAttr) {
                                newData[dataAttr] = parseAttr || attr;
                            } else {
                                newAttrs[optionAttr] = parseAttr || attr;
                            }
                        }

                        $(this).data(newData).attr(newAttrs);
                    }
                });
            }

            function refresh(newVal) {
                scope[$async](function () {
                    if (attrs.ngOptions && attrs.bsOptionAttrs) setAttributes();
                    element.selectpicker('refresh');
                });
            }

            function render(newVal) {
                // update model if select is within child scope (e.g. inside ng-if)
                if (scope.$parent[attrs.ngModel] !== undefined && scope.$parent[attrs.ngModel] !== newVal) {
                    scope.$parent[attrs.ngModel] = newVal;
                }

                if (scope.$$childHead && scope.$$childHead[attrs.ngModel]) {
                    scope.$$childHead[attrs.ngModel] = newVal;
                }

                scope[$async](function () {
                    element.selectpicker('render');
                });
            }

            attrs.$observe('spTheme', function (val) {
                $timeout(function () {
                    element.data('selectpicker').$button.removeClass(function (i, c) {
                        return (c.match(/(^|\s)?btn-\S+/g) || []).join(' ');
                    });
                    element.selectpicker('setStyle', val);
                });
            });

            if (attrs.ngModel) {
                scope.$watch(attrs.ngModel, function(newVal, oldVal) {
                    if (newVal !== oldVal) {
                        if (!oldVal) {
                            return refresh(newVal);
                        } else {
                            return render(newVal);
                        }
                    }
                }, true);
            }

            if (attrs.ngDisabled) {
                scope.$watch(attrs.ngDisabled, refresh, true);
            }

            scope.$on('$destroy', function () {
                $timeout(function () {
                    element.selectpicker('destroy');
                });
            });
        }
    };
}
