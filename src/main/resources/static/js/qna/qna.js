/*QnA 수정*/
document.getElementById('updateQnaButton').addEventListener('click', function() {
    var title = document.getElementsByName('title')[0].value;
    var content = document.getElementsByName('content')[0].value;

    const qnaId = document.getElementById('qnaId').value;
    console.log(qnaId);

    //UpdateQnaRequestDto 객체 생성
    const updateQnaReq = {
        title: title,
        content: content,
    };

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    //후원 생성 요청을 보냄
    axios.post('/qna/' + qnaId, updateQnaReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        //서버 응답 처리
        console.log(response);
        alert('QnA 수정 성공');
    })
    .catch(function(error) {
        //오류 처리
        console.error(error);
        alert('QnA 수정 실패');
    });
});


/*QnA 삭제*/
document.getElementById('deleteQnaButton').addEventListener('click', function() {
    const qnaId = document.getElementById('qnaId').value;
    console.log(qnaId);

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    if (confirm("정말로 QnA를 삭제하시겠습니까?")) {
        //사용자가 확인을 누르면 컨트롤러 호출
        axios.delete('/qna/' + qnaId, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }).then(function(response) {
            //요청이 성공한 경우
            console.log(response);
            alert("QnA 삭제가 완료되었습니다.");
        })
        .catch(function(error) {
            //요청이 실패한 경우
            console.error(error);
            alert("QnA 삭제에 실패하였습니다.");
        });
    }
});