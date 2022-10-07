let index = {
    init: function(){
        $("#btn-save").on("click", ()=>{
            this.save();
         });
    },

    save: function(){
        let data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        $.ajax({
            type: "POST",
            url: "/api/board",
            data: JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset:utf-8", //body 데이터가 어떤 타입인지(MIME)
            dataType: "json" //요청을 서버로 해서 응답이 왔을 때, 기본적으로 모든 것이 문자열(json)이면 javascript 오브젝트로 변경해 준다
        }).done(function(resp){
        alert("글쓰기가 완료되었습니다.")
            location.href= "/";
        }).fail(function(error){
            alert(JSON.stringify(error));
        }); //ajax 통신을 이용해서 3개의 데이터를 json 으로 변경하여 insert 요청
    }
}

index.init();