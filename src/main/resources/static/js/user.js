let index = {
    init: function(){
        $("#btn-save").on("click", ()=>{ //function(){}이 아닌 ()=>{} this 를 바인딩 하기 위해 사용
            this.save();
        });
        $("#btn-update").on("click", ()=>{
            this.update();
        });
    },

    save: function(){
        //alert('user의 save함수 호출!')
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };
        //console.log(data);

        //ajax 호출시 default 가 비동기 호출
        $.ajax({
            //회원가입 수행 요청
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data), //http body 데이터
            contentType: "application/json; charset:utf-8", //body 데이터가 어떤 타입인지(MIME)
            dataType: "json" //요청을 서버로 해서 응답이 왔을 때, 기본적으로 모든 것이 문자열(json)이면 javascript 오브젝트로 변경해 준다
        }).done(function(resp){
            if(resp.status === 500){
            alert("회원가입이 실패되었습니다!");
            }else{
            alert("회원가입이 완료되었습니다!");
            // alert(resp);
            location.href= "/";
            }
        }).fail(function(error){
            alert(JSON.stringify(error));
        }); //ajax 통신을 이용해서 3개의 데이터를 json 으로 변경하여 insert 요청
    },

    update: function(){
            let data = {
                id: $("#id").val(),
                username: $("#username").val(),
                password: $("#password").val(),
                email: $("#email").val()
            };

            $.ajax({
                type: "PUT",
                url: "/user",
                data: JSON.stringify(data), //http body 데이터
                contentType: "application/json; charset:utf-8", //body 데이터가 어떤 타입인지(MIME)
                dataType: "json" //요청을 서버로 해서 응답이 왔을 때, 기본적으로 모든 것이 문자열(json)이면 javascript 오브젝트로 변경해 준다
            }).done(function(resp){
                alert("회원수정이 완료되었습니다!");
                // alert(resp);
                location.href= "/";
            }).fail(function(error){
                alert(JSON.stringify(error));
            }); //ajax 통신을 이용해서 3개의 데이터를 json 으로 변경하여 insert 요청
        }
}

index.init();