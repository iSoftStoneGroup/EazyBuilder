;(function(angular){
  'use strict';
  var indexOf = [].indexOf || function(item) {
    for (var i = 0, l = this.length; i < l; i++) {
      if (i in this && this[i] === item) return i;
    }
    return -1;
  };

  function map(items, property) {
    var mappedArray = [];
    angular.forEach(items, function(item) {
      mappedArray.push(angular.isFunction(property) ? property(item) : item[property]);
    });
    return mappedArray;
  }

  angular.module('pickadate', [])

    .provider('pickadateI18n', function() {
      var defaults = {
        'prev': 'prev',
        'next': 'next'
      };

      this.translations = {};

      this.$get = function() {
        var translations = this.translations;

        return {
          t: function(key) {
            return translations[key] || defaults[key];
          }
        };
      };
    })

    .factory('pickadateModalBindings', ['$window', '$document', function($window, $document) {
      var supportPageOffset = $window.pageXOffset !== undefined,
          isCSS1Compat = (($document.compatMode || "") === "CSS1Compat");

      var computeStyles = function(element) {
        var scrollX = supportPageOffset ? $window.pageXOffset : isCSS1Compat ? $document.documentElement.scrollLeft : $document.body.scrollLeft,
            scrollY = supportPageOffset ? $window.pageYOffset : isCSS1Compat ? $document.documentElement.scrollTop : $document.body.scrollTop,
            innerWidth = $window.innerWidth || $document.documentElement.clientWidth || $document.body.clientWidth,
            styles = { top: scrollY + element.getBoundingClientRect().bottom + 'px' };

        if ((innerWidth - element.getBoundingClientRect().left ) >= 300) {
          styles.left = scrollX + element.getBoundingClientRect().left  + 'px';
        } else {
          styles.right = innerWidth - element.getBoundingClientRect().right - scrollX + 'px';
        }

        return styles;
      };

      var isDescendant = function(parent, child) {
        var node = child.parentNode;
        while (node !== null) {
          if (node === parent) return true;
          node = node.parentNode;
        }
        return false;
      };

      return function(scope, element, rootNode) {
        var togglePicker = function(toggle) {
          scope.displayPicker = toggle;
          scope.$apply();
        };

        element.on('focus', function() {
          scope.modalStyles = computeStyles(element[0]);
          togglePicker(true);
        });

        element.on('keydown', function(e) {
          if (indexOf.call([9, 13, 27], e.keyCode) >= 0) togglePicker(false);
        });

        $document.on('click', function(e) {
          if (isDescendant(rootNode, e.target) || e.target === element[0]) return;
          togglePicker(false);
        });
      };

    }])

    .factory('pickadateDateHelper', ['$locale', 'dateFilter', function($locale, dateFilter) {

      function getPartName(part) {
        switch (part) {
          case 'dd':   return 'day';
          case 'MM':   return 'month';
          case 'yyyy': return 'year';
        }
      }

      return function(format, options) {
        var minDate, maxDate, disabledDates, currentDate, weekStartsOn, noExtraRows;

        options      = options || {};
        format       = format  || 'yyyy-MM-dd';
        weekStartsOn = options.weekStartsOn;
        noExtraRows  = options.noExtraRows;

        if (!angular.isNumber(weekStartsOn) || weekStartsOn < 0 || weekStartsOn > 6) weekStartsOn = 0;

        return {

          parseDate: function(dateString) {
            if (!dateString) return;
            if (angular.isDate(dateString)) return new Date(dateString);

            var formatRegex = '(dd|MM|yyyy)',
                separator   = format.match(/[-|/]/)[0],
                dateParts   = dateString.split(separator),
                regexp      = new RegExp([formatRegex, formatRegex, formatRegex].join(separator)),
                formatParts = format.match(regexp),
                dateObj     = {};

            formatParts.shift();

            angular.forEach(formatParts, function(part, i) {
              dateObj[getPartName(part)] = parseInt(dateParts[i], 10);
            });

            if (isNaN(dateObj.year) || isNaN(dateObj.month) || isNaN(dateObj.day)) return;

            return new Date(dateObj.year, dateObj.month - 1, dateObj.day, 3);
          },

          setRestrictions: function(restrictions) {
            minDate       = this.parseDate(restrictions.minDate) || new Date(0);
            maxDate       = this.parseDate(restrictions.maxDate) || new Date(99999999999999);
            currentDate   = restrictions.currentDate;
            disabledDates = restrictions.disabledDates || [];
          },

          allowPrevMonth: function() {
            return currentDate > minDate;
          },

          allowNextMonth: function() {
            var nextMonth = angular.copy(currentDate);
            nextMonth.setMonth(nextMonth.getMonth() + 1);
            return nextMonth <= maxDate;
          },

          buildDateObject: function(date) {
            var localDate     = angular.copy(date),
                formattedDate = dateFilter(localDate, format),
                disabled      = indexOf.call(disabledDates, formattedDate) >= 0,
                monthOffset   = this.getMonthOffset(localDate, currentDate),
                outOfMinRange = localDate < minDate,
                outOfMaxRange = localDate > maxDate,
                outOfMonth    = (monthOffset === -1 && !options.previousMonthSelectable) ||
                                (monthOffset === 1 && !options.nextMonthSelectable);

            return {
              date: localDate,
              formattedDate: formattedDate,
              today: formattedDate === dateFilter(new Date(), format),
              disabled: disabled,
              outOfMinRange: outOfMinRange,
              outOfMaxRange: outOfMaxRange,
              monthOffset: monthOffset,
              enabled: !(disabled || outOfMinRange || outOfMaxRange || outOfMonth)
            };
          },

          buildDates: function(year, month, options) {
            var dates      = [],
                date       = new Date(year, month, 1, 3),
                lastDate   = new Date(year, month + 1, 0, 3);

            options        = options || {};
            currentDate    = angular.copy(date);

            while (date.getDay() !== weekStartsOn) date.setDate(date.getDate() - 1);

            for (var i = 0; i < 42; i++) {  // 42 == 6 rows of dates
              if (noExtraRows && date.getDay() === weekStartsOn && date > lastDate) break;

              dates.push(this.buildDateObject(date));
              date.setDate(date.getDate() + 1);
            }

            return dates;
          },

          buildDayNames: function() {
            var dayNames = $locale.DATETIME_FORMATS.SHORTDAY;

            if (weekStartsOn) {
              dayNames = dayNames.slice(0);
              for (var i = 0; i < weekStartsOn; i++) dayNames.push(dayNames.shift());
            }
            return dayNames;
          },

          getMonthOffset: function(date1, date2) {
            return date1.getMonth() - date2.getMonth() + (12 * (date1.getFullYear() - date2.getFullYear()));
          }
        };
      };

    }])

    .directive('pickadate', ['$locale', '$sce', '$compile', '$document', '$window', 'pickadateDateHelper',
      'pickadateI18n', 'pickadateModalBindings', 'filterFilter', function($locale, $sce, $compile, $document, $window,
                                                                          dateHelperFactory, i18n, modalBindings, filter) {

      var TEMPLATE =
        '<div class="pickadate" ng-show="displayPicker" ng-style="modalStyles">' +
          '<div class="pickadate-header">' +
            '<div class="pickadate-controls">' +
              '<a href="" class="pickadate-prev" ng-click="changeMonth(-1)" ng-show="allowPrevMonth">' +
                $sce.trustAsHtml(i18n.t('prev')) +
              '</a>' +
              '<a href="" class="pickadate-next" ng-click="changeMonth(1)" ng-show="allowNextMonth">' +
                $sce.trustAsHtml(i18n.t('next')) +
              '</a>' +
            '</div>'+
            '<h3 class="pickadate-centered-heading">' +
              '{{currentDate | date:"MMMM yyyy"}}' +
            '</h3>' +
          '</div>' +
          '<div class="pickadate-body">' +
            '<div class="pickadate-main">' +
              '<ul class="pickadate-cell">' +
                '<li class="pickadate-head" ng-repeat="dayName in dayNames">' +
                  '{{dayName}}' +
                '</li>' +
              '</ul>' +
              '<ul class="pickadate-cell">' +
                '<li ng-repeat="dateObj in dates" ng-click="setDate(dateObj)" ng-class="classesFor(dateObj)">' +
                  '{{dateObj.date | date:"d"}}' +
                '</li>' +
              '</ul>' +
            '</div>' +
          '</div>' +
        '</div>';

      return {
        require: 'ngModel',
        scope: {
          defaultDate: '=',
          minDate: '=',
          maxDate: '=',
          disabledDates: '=',
          weekStartsOn: '='
        },

        link: function(scope, element, attrs, ngModel)  {
          var allowMultiple           = attrs.hasOwnProperty('multiple'),
              selectedDates           = [],
              wantsModal              = element[0] instanceof HTMLInputElement,
              compiledHtml            = $compile(TEMPLATE)(scope),
              format                  = (attrs.format || 'yyyy-MM-dd').replace(/m/g, 'M'),
              dateHelper              = dateHelperFactory(format, {
                previousMonthSelectable: /^(previous|both)$/.test(attrs.selectOtherMonths),
                nextMonthSelectable:     /^(next|both)$/.test(attrs.selectOtherMonths),
                weekStartsOn: scope.weekStartsOn,
                noExtraRows: attrs.hasOwnProperty('noExtraRows')
              });

          scope.displayPicker = !wantsModal;

          scope.setDate = function(dateObj) {
            if (!dateObj.enabled) return;
            selectedDates = allowMultiple ? toggleDate(dateObj, selectedDates) : [dateObj];

            setViewValue(selectedDates);

            scope.changeMonth(dateObj.monthOffset);
            scope.displayPicker = !wantsModal;
          };

          var $render = ngModel.$render = function(options) {
            if (angular.isArray(ngModel.$viewValue)) {
              selectedDates = ngModel.$viewValue;
            } else if (ngModel.$viewValue) {
              selectedDates = [ngModel.$viewValue];
            }

            scope.currentDate = dateHelper.parseDate(scope.defaultDate || selectedDates[0]) || new Date();

            dateHelper.setRestrictions(scope);

            selectedDates = map(selectedDates, function(date) {
              return dateHelper.buildDateObject(dateHelper.parseDate(date));
            });

            selectedDates = filter(selectedDates, { enabled: true });

            setViewValue(selectedDates, options);
            render();
          };

          scope.classesFor = function(date) {
            var formattedDates = map(selectedDates, 'formattedDate'),
                classes        = indexOf.call(formattedDates, date.formattedDate) >= 0 ? 'pickadate-active' : null;
            return date.classNames.concat(classes);
          };

          scope.changeMonth = function(offset) {
            if (!offset) return;
            // If the current date is January 31th, setting the month to date.getMonth() + 1
            // sets the date to March the 3rd, since the date object adds 30 days to the current
            // date. Settings the date to the 2nd day of the month is a workaround to prevent this
            // behaviour
            scope.currentDate.setDate(1);
            scope.currentDate.setMonth(scope.currentDate.getMonth() + offset);
            render();
          };

          // Workaround to watch multiple properties. XXX use $scope.$watchGroup in angular 1.3
          scope.$watch(function() {
            return angular.toJson([scope.minDate, scope.maxDate, scope.disabledDates]);
          }, $render);

          // Insert datepicker into DOM
          if (wantsModal) {
            modalBindings(scope, element, compiledHtml[0]);

            // if the user types a date, update the picker and set validity
            scope.$watch(function() {
              return ngModel.$viewValue;
            }, function(val) {
              var isValidDate = dateHelper.parseDate(val);

              if (isValidDate) $render({ skipRenderInput: true });
              ngModel.$setValidity('date', !!isValidDate);
            });

            // if the input element has a value, set it as the ng-model
            scope.$$postDigest(function() {
              if (attrs.value) { ngModel.$viewValue = attrs.value; $render(); }
            });

            element.after(compiledHtml.addClass('pickadate-modal'));
          } else {
            element.append(compiledHtml);
          }

          function render() {
            var dates = dateHelper.buildDates(scope.currentDate.getFullYear(), scope.currentDate.getMonth());

            scope.allowPrevMonth = dateHelper.allowPrevMonth();
            scope.allowNextMonth = dateHelper.allowNextMonth();
            scope.dayNames       = dateHelper.buildDayNames();

            scope.dates = map(dates, function(date) {
              date.classNames = [date.enabled ? 'pickadate-enabled' : 'pickadate-disabled'];

              if (date.today)    date.classNames.push('pickadate-today');
              if (date.disabled) date.classNames.push('pickadate-unavailable');

              return date;
            });
          }

          function setViewValue(value, options) {
            options = options || {};

            if (allowMultiple) {
              ngModel.$setViewValue(map(value, 'formattedDate'));
            } else {
              ngModel.$setViewValue(value[0] && value[0].formattedDate);
            }

            if (!options.skipRenderInput) element.val(ngModel.$viewValue);
          }

          function toggleDate(dateObj, dateArray) {
            var index = indexOf.call(dateArray, dateObj);
            if (index === -1) {
              dateArray.push(dateObj);
            } else {
              dateArray.splice(index, 1);
            }
            return dateArray;
          }
        }
      };
    }]);
})(window.angular);
