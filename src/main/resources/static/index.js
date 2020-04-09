

$(document).ready(function () {

    initBinds();
    loadYears();
    loadCategories();
    refreshTable();

    function initBinds() {

        $('#filter').on('click', function () {

           var search = $('#filter_title').val();


            var queryString = window.location.search;
            var urlParams = new URLSearchParams(queryString);

            urlParams.set("search", search);

            redirect("?"+urlParams.toString());

        });

        $('.tickbox').on('click', function () {


            var queryString = window.location.search;
            var urlParams = new URLSearchParams(queryString);

            var params = [];
            $('.tickbox input:checked').each(function () {

                var values = $(this).val();
                var item = values.split('_');
                var param = item[0] + "=" + item[1] + "&";

                params.push(param);

        });

            var url = "/?";
            for (i = 0; i < params.length; i++) {
                url = url + params[i];
            }

            url = url.substring(0, url.length - 1);

            url += urlParams.has('search') ? (params.length>0) ? "&search="+urlParams.get('search'): "?search="+urlParams.get('search') : "";
            redirect(url);
        });



    }

    function redirect(url) {
        window.location = url;
    }

    $("#refreshFileButton").click(function () {
        refreshTable();
    });


    function loadCategories() {

        var queryString = window.location.search;
        var urlParams = new URLSearchParams(queryString);
        var year = urlParams.get('ano');
        var category = urlParams.get('category');
        var search = urlParams.get('search') == '' ? 'null' : urlParams.get('search');

        $.ajax({
            url: "/categories/"+year+"/"+category+"/"+search
        }).done(function (data) {
            console.dir(data);

            var queryString = window.location.search;
            var urlParams = new URLSearchParams(queryString);

            var html = '<ul class="nobull">';

            for (var i = 0; i < data.length; i++) {
                html +='<li>';
                html +='<div class="tickbox">';
                var checked = (urlParams.has('category') && parseInt(urlParams.get('category')) == data[i].category.id) ? 'checked="checked"' : '';
                html +='<input type="checkbox" name="CategoryCheckFilter" id="'+data[i].category.category+'" value="category_'+data[i].category.id+'" '+checked+'/>';
                html +='<span> '+data[i].category.category+' </span>';
                html +='<span class="pull-right">'+data.length+'</span>';
                html +='</div>';
                html +='</li>';
            }

            html += "</ul>";
            $("#doc_categories").html(html);
            initBinds();
        });
    }


    function loadYears() {

        var queryString = window.location.search;
        var urlParams = new URLSearchParams(queryString);
        var year = urlParams.get('ano');
        var category = urlParams.get('category');
        var search = urlParams.get('search') == '' ? 'null' : urlParams.get('search');
        $.ajax({
            url: "/years/"+year+"/"+category+"/"+search
        }).done(function (data) {
            console.dir(data);

            var queryString = window.location.search;
            var urlParams = new URLSearchParams(queryString);

            var html = '<ul class="nobull">';

            for (var i = 0; i < data.length; i++) {
                html +='<li>';
                html +='<div class="tickbox">';
                var checked = (urlParams.has('ano') && parseInt(urlParams.get('ano')) == data[i].year) ? 'checked="checked"' : '';
                html +='<input type="checkbox" name="YearCheckFilter" id="'+data[i].year+'" value="ano_'+data[i].year+'" '+checked+'/>';
                html +='<span> '+data[i].year+' </span>';
                html +='<span class="pull-right">'+data.length+'</span>';
                html +='</div>';
                html +='</li>';
            }

            html += "</ul>";
            $("#years_more").html(html);
            initBinds();
        });
    }

});

function refreshTable() {
    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var year = urlParams.get('ano');
    var category = urlParams.get('category');
    var search = urlParams.get('search') == '' ? 'null' : urlParams.get('search');

    $.ajax({
        url: "/listdbfiles/"+year+"/"+category+"/"+search
    }).done(function (data) {
        // console.dir(data);

        var tr = '';
        for (var i = 0; i < data.data.length; i++) {
            tr += '<tr>' +
                '<td>' + data.data[i].year + '</td>' +
                // '<td>' + data.data[i].title + '</td>';
                '<td><a href="' + data.data[i].url + '" target="_blank">'+data.data[i].title +'</a></td>';


            tr += '<td>'

            tr += data.data[i].attachments;
            // for (var j = 0; j < data.data[i].attachmentCollection.length; j++) {
            //     tr+='<div><a href="'+data.data[i].attachmentCollection[j].url+'" target="_blank">'+data.data[i].attachmentCollection[j].title+'</a></div>';
            //
            // }
            tr += '</td>'


            tr +='<td></td>' +
                '</tr>';
        }
        $("#tbo").html(tr);
        var filter = (search == 'null') ? '' : search;
        $('#filter_title').val(filter);
    });
}

