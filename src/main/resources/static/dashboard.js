

$(document).ready(function () {




    initBinds();

    fill_filter();

    fill_datatable_files();

    function fill_filter() {

        var option = '';
        for (var i=2020; i>2005; i--){
            option+='<option value="'+i+'">'+i+'</option>';
        }
        $('[name="filter_year"]').html(option);

        $.ajax({
            url:"/category/all",
            method: 'GET'
        }).done(function(data){
            var option = '';
            for (var i = 0; i < data.length; i++){
                option+='<option value="'+data[i].id+'">'+data[i].category+'</option>';
            }
            $('[name="filter_category"]').html(option);

        });
    }

    function initBinds() {

        if ($('.view').length > 0) {
            var viewBtn = document.getElementsByClassName('view');
            for (var i = 0; i < viewBtn.length; i++) {
                viewBtn[i].addEventListener("click", function () {
                    // var id = $(this).attr('rel');
                    // edit_candidate(id);

                    $('#formViewModal').modal('show');

                }, false);
            }
        }


        if ($('.edit').length > 0) {
            var editBtn = document.getElementsByClassName('edit');
            for (var i = 0; i < editBtn.length; i++) {
                editBtn[i].addEventListener("click", function () {
                    var id = $(this).attr('rel');
                    edit(id);
                }, false);
            }
        }



        if ($('.delete').length > 0) {
            var deleteBtn = document.getElementsByClassName('delete');
            for (var i = 0; i < deleteBtn.length; i++) {
                deleteBtn[i].addEventListener("click", function () {
                    var id = $(this).attr('rel');
                    delete_file(id);
                }, false);
            }
        }

        if ($('#syncronize').length > 0) {
            ($('#syncronize').click(function () {
                fetch_datatable_files();
            }));
        }

        if ($('#btn-save-editar').length > 0) {
            ($('#btn-save-editar').click(function () {
                var id = $('#id').val();
                save(id);
            }));
        }
    }


    // $('#filter').on('click', function () {
    //     reload_table();
    // });

    if ($('#btnAddAll').length > 0) {
        $('#btnAddAll').click(function () {
            $.ajax({
                url: "/addallfiles",
                method: "POST",
                dataType: "json",
                success: function (data) {
                    if(data.status){
                        // fetch_datatable_files();
                }
                },
                complete:  function(data){

                },
                error: function (xhr, textStatus, error) {
                    // alert('An error has occurred ::from save answer ajax call --> ' + error);
                    console.log(xhr.statusText);
                    console.log(textStatus);
                    console.log(error);
                }
            });
        });
    }

    $("#simpleUpLoad").click(function () {

        $.ajax({
            url:"/create",
            sucess:function () {
                alert("file upload complete");
            }
        });
    });


    $("#createFolderButton").click(function () {
        var folderName = prompt('Please enter the folder name');
        $.ajax({
            url:"/createfolder/"+folderName,
        }).done(function(data){
            console.dir(data);
        });
    });

    $("#upLoadFileInFolder").click(function () {
        $.ajax({
            url:"/uploadinfolder",
        }).done(function(data){
            alert(data.message);
        });
    });


    function fill_datatable_files() {

        var year = $('#filter_year').val() !=null ? $('#filter_year').val() : 'null';
        var category = $('#filter_category').val() !=null ? $('#filter_category').val() : 'null';
        var search = $('#filter_name').val() == '' ? 'null' : $('#filter_name').val();


        // var year = 'null';
        // var category ='null';
        // var search = 'null';

        var table = $('#tblfiles').DataTable({
            // "processing": true, //Feature control the processing indicator.
            "serverSide": true, //Feature control DataTables' server-side processing mode.
            "order": [], //Initial no order.

            // Load data for the table's content from an Ajax source
            "ajax": {
                "url": "/listdbfiles/"+year+"/"+category+"/"+search,
                "complete": function (xhr, responseText) {
                    console.log("called");
                    initBinds();
                    console.log(xhr);
                    console.log(responseText); //*** responseJSON: Array[0]
                }



            },

            //Set column definition initialisation properties.
            "columnDefs": [
                {
                    "targets": [-1], //last column
                    "orderable": false, //set not orderable
                },
            ],
            "columns": [
                {"data": "year"},
                {"data": "title"},
                // {"data": "url"},
                {"data": "attachments"},
                {"data": "action"}
            ],

            "scrollCollapse": true,

            "searching": false,
            "paging": true,
            "info": false,
            "lengthMenu": false,
            "language": {
                "lengthMenu": "Mostrar _MENU_ itens por página",
                "zeroRecords": "Não foi encontrado nenhum registo",
                "info": "Mostrando página _PAGE_ de _PAGES_",
                "infoEmpty": "Nenhum registo encontrado",
                "infoFiltered": "(fitrados apartir _MAX_ dos registos)",
                "paginate": {
                    "first": "Primeiro",
                    "last": "Último",
                    "next": "Próximo",
                    "previous": "Anterior"
                },
                "search": "Pesquisar: "
            }
        });
    }

    function fetch_datatable_files() {

        table = $('#tblsync').DataTable({
            // "processing": true, //Feature control the processing indicator.
            "serverSide": true, //Feature control DataTables' server-side processing mode.
            "order": [], //Initial no order.

            "bDestroy": true,
            // Load data for the table's content from an Ajax source
            "ajax": {
                "url": "/listfiles",
                // success : function (data) {
                //     var lbl = $('#lblresult');
                //     var hml = '<p class="text-center">Nenhum ficheiro encontrado</p>';
                //     if(data.length>0){
                //         hml = '<p class="text-center">Foram encontrados '+data.length+' ficheiros</p>';
                //     }
                //     lbl.html(hml);
                //     initBinds();
                // },
                "complete": function (xhr, responseText) {
                    $("#syncfiles").removeClass("hidden");
                    initBinds();
                    console.log(xhr);
                    console.log(responseText); //*** responseJSON: Array[0]
                }
            },

            //Set column definition initialisation properties.
            "columnDefs": [
                {
                    "targets": [-1], //last column
                    "orderable": false, //set not orderable
                },
            ],
            "columns": [
                {"data": "title"},
                {"data": "action"}
            ],

            "scrollCollapse": true,

            "searching": false,
            "paging": true,
            "info": false,
            "lengthMenu": false,
            "language": {
                "lengthMenu": "Mostrar _MENU_ itens por página",
                "zeroRecords": "Não foi encontrado nenhum registo",
                "info": "Mostrando página _PAGE_ de _PAGES_",
                "infoEmpty": "Nenhum registo encontrado",
                "infoFiltered": "(fitrados apartir _MAX_ dos registos)",
                "paginate": {
                    "first": "Primeiro",
                    "last": "Último",
                    "next": "Próximo",
                    "previous": "Anterior"
                },
                "search": "Pesquisar: "
            }
        });
    }


    function reload_table() {
        // $('#tblfiles').empty();
        $('#tblfiles').DataTable().destroy();
        fill_datatable_files();
    }

    function save(id) {

        $('.form-group').removeClass('has-error has-feedback');
        $('.form-group').find('small.help-block').hide();
        $('.form-group').find('i.form-control-feedback').hide();
        $('.help-block').empty(); // clear error string

        $('#btn-save').text('saving...'); //change button text
        $('#btn-save').attr('disabled', true); //set button disable

        $.ajax({
            url: "/edit",
            type: "POST",
            data :  $('#formEditar').serialize(),
            dataType: "JSON",
            success: function (data) {
                if (data.status) {
                    $('#formEditar').modal('hide');
                    reload_table();
                } else {
                    // $("#alert").removeClass("hidden");
                }
                // init();

                $('#btn-save-editar').text('save'); //change button text
                $('#btn-save-editar').attr('disabled', false); //set button enable

            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert('Error deleting data');
                console.log(jqXHR.statusText);
                console.log(textStatus);
                console.log(errorThrown);
            }
        });
    }


    function delete_file(id) {
        if (confirm('Tem certeza que deseja remover este dado?')) {
            // ajax delete data to database
            $.ajax({
                url: "/delete/" + id,
                type: "DELETE",
                dataType: "JSON",
                success: function (data) {
                    //if success reload ajax table
                    // $('#formModal').modal('hide');
                    reload_table();
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    alert('Error deleting data');
                }
            });

        }
    }



});




function deleteFile(id) {
    $.ajax({
        url:"/deletefile/"+id,
        method: 'DELETE'
    }).done(function(data){
        alert('File has been deleted. Please refresh the page!');
    });
}

function makePublic(id) {
    $.ajax({
        url:"/makepublic/"+id,
        method: 'POST'
    }).done(function(data){
        alert('File can be view by anyone online');
    });
}


function fillComboYear() {
    var option = '';
    for (var i=2020; i>2005; i--){
        option+='<option value="'+i+'">'+i+'</option>';
    }
    $('[name="year"]').html(option);
}


function fillComboCategory() {
    $.ajax({
        url:"/category/all",
        method: 'GET'
    }).done(function(data){
        var option = '';
        for (var i = 0; i < data.length; i++){
            option+='<option value="'+data[i].id+'">'+data[i].category+'</option>';
        }
        $('[name="category"]').html(option);

    });
}


function fillComboType() {
    $.ajax({
        url:"/type/all",
        method: 'GET'
    }).done(function(data){
        var option = '';
        for (var i = 0; i < data.length; i++){
            option+='<option value="'+data[i].id+'">'+data[i].type+'</option>';
        }
        $('[name="type"]').html(option);

    });
}

function edit(id) {
    // save_method = 'update';

    $('.form-group').removeClass('has-error'); // clear error class
    $('.form-group').removeClass('has-error'); // clear error class
    $('.form-control-feedback').removeClass('glyphicon glyphicon-remove');
    $('.help-block').empty(); // clear error string

    fillComboYear();
    fillComboCategory();
    fillComboType();

    //Ajax Load data from ajax
    $.ajax({
        url: "/document/" + id,
        type: "GET",
        dataType: "JSON",
        success: function (data) {
            $('[name="id"]').val(data.id);
            $('[name="googleId"]').val(data.googleId);
            $('[name="year"]').val(data.year);
            $('[name="title"]').val(data.title);
            $('[name="category"]').val(data.category.id);
            $('[name="type"]').val(data.type.id);
            $('[name="url"]').val(data.url);
            $('[name="datecreate"]').val(data.datecreate);

            // if(data.document != null)
            // $('[name="document"]').val(data.document.id);

            $('#formEditarModal').modal('show'); // show bootstrap modal
            $('.modal-title').text('Editar Documento'); // Set Title to Bootstrap modal title
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(jqXHR.statusText);
            console.log(textStatus);
            console.log(errorThrown);
        }
    });
}


