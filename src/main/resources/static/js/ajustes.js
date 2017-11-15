$("#gerar").click(()=>{
    let clientes=$('#destinos').val();
    let lojas=$('#origens').val();
    if(clientes!=""&& lojas!=""){
        clientes=parseInt(clientes);
        lojas=parseInt(lojas);
        first=false;
        for (i=0;i<=lojas;i++){
            if(i<lojas)
                $('#matrix').append("<tr id="+i+"><td>loja "+(i+1)+"</td></tr>");
            else
                $('#matrix').append("<tr id="+i+"><td>Demanda</td></tr>");

            for (j=0;j<=clientes;j++){
                if(first==false){

                    if(j<clientes)
                        $('#first').append("<th>Cliente"+(j+1)+"</th>");
                    else
                        $('#first').append("<th>Entrega</th>");
                }
                $("#"+i).append("<td><input name='mat[][]' type=\"number\" class=\"validate\"/></td>");
            }
            if(i==0){
                first=true;
            }
        }
        if(i>lojas) {
            $('#gerar').addClass("disabled");
            $('#limpar').removeClass("disabled");
            $('#chamar').removeClass("disabled");
        }
    }else{
        $('#modal1').modal('open');
    }

});

    $("#limpar").click(()=>{
    $('#limpar').addClass("disabled");
    $('#gerar').removeClass("disabled");
    $('#chamar').addClass("disabled");
    $('#first').remove();
    $('#matrix').remove();
    $('#destinos').val("");
    $('#origens').val("");
    $('#oferta').val("");

    $('#thread').append("<tr id=\"first\"><th>Origem</th></tr>");
    $('table').append("<tbody id=\"matrix\"></tbody>");
});
$('#chamar').click((e)=>{
    $('form').submit();
});
$('.modal').modal({
        dismissible: true, // Modal can be dismissed by clicking outside of the modal
        opacity: .5, // Opacity of modal background
        inDuration: 300, // Transition in duration
        outDuration: 200, // Transition out duration
        startingTop: '4%', // Starting top style attribute
        endingTop: '10%', // Ending top style attribute
        ready: function(modal, trigger) { // Callback for Modal open. Modal and trigger parameters available.
        },
        complete: function() {  } // Callback for Modal close
    }
);
