/**
 * Select2 Chinese translation
 */
(function ($) {
    "use strict";
    $.extend($.fn.select2.defaults, {
        formatNoMatches: function () { return "検索結果が見つかりませんでした"; },
        formatInputTooShort: function (input, min) { var n = min - input.length; return "更に" + n + "文字を入力してください";},
        formatInputTooLong: function (input, max) { var n = input.length - max; return   n + "文字を削除してください";},
        formatSelectionTooBig: function (limit) { return "你只能选择最多" + limit + "项"; },
        formatLoadMore: function (pageNumber) { return "読み込ん結果..."; },
        formatSearching: function () { return "検索..."; }
    });
})(jQuery);
