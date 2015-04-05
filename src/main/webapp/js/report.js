$(document).ready(function(){
    handleTabs();
    handleDatepickers();
    fetchData();
    handleGoButton();
    setInterval(fetchData, 30000);
});

function fetchData() {
    getByCountry();
    getByCurrencyFrom();
    getByCurrencyTo();
    getErrorReport();
}

function handleGoButton() {
    $("#go_button").click(function(){
        fetchData();
    });
}

function handleTabs() {
    $('.tabs.standard .tab-links a').on('click',function(e) {
        var currentAttrValue = $(this).attr('href');
        $('.tabs '+ currentAttrValue).show().siblings().hide();
        $(this).parent('li').addClass('active').siblings().removeClass('active');
        e.preventDefault();
    });
}

function handleDatepickers() {
    $( "#datepicker_from" ).datepicker({
        changeMonth: true,
        changeYear: true
    });
    $( "#datepicker_to" ).datepicker({
        changeMonth: true,
        changeYear: true
    });
}

function getDateFilter() {
    var filter = {};
    if ($("#datepicker_from").val() != "") {
        filter["from"] = $("#datepicker_from").val();
    }
    if ($("#datepicker_to").val() != "") {
        filter["to"] = $("#datepicker_to").val();
    }
    return filter;
}

function buildReportGrid(data) {
    var html = '';
    if (data.length > 0) {
        var max = data[0].cnt;
        var maxWidth = 300;
        for (var x in data) {
            var width = Math.ceil(maxWidth * data[x].cnt / max);
            html += '<div class="row">' +
            '<div class="col-left">' + data[x].name + '</div>' +
            '<div class="col-right" style="width: ' + width + 'px;">' + data[x].cnt + '</div>' +
            '<div style="clear: both;" />' +
            '</div>';
        }
    }
    else {
        html += '<b>No data available for this report</b>';
    }
    return html;
}

function getByCountry() {
    $.getJSON("/cf/api/transaction/byCountry", getDateFilter(), function(data){
        $("#tab1").html(buildReportGrid(data));
    });
}

function getByCurrencyFrom() {
    $.getJSON("/cf/api/transaction/byCurrencyFrom", getDateFilter(), function(data){
        $("#tab2").html(buildReportGrid(data));
    });
}

function getByCurrencyTo() {
    $.getJSON("/cf/api/transaction/byCurrencyTo", getDateFilter(), function(data){
        $("#tab3").html(buildReportGrid(data));
    });
}

function getErrorReport() {
    $.getJSON("/cf/api/transaction/errors", getDateFilter(), function(data){
        var html = '';
        if (data.length > 0) {
            for (var x in data) {
                html += '<div class="row">' +
                '<div class="col-left">User ID: ' + data[x].userId + '</div>' +
                '<div style="float: left; padding-right: 10px;">' + data[x].message + '</div>' +
                '<div style="float: left; width: 20%;">' + data[x].date + '</div>' +
                '<div style="clear: both;" />' +
                '</div>';
            }
        }
        else {
            html += '<b>No data available for this report</b>';
        }
        $("#tab4").html(html);
    });
}
