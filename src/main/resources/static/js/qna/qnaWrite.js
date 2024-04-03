/*QnA 생성*/
document.getElementById('createQnaBtn').addEventListener('click', function() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    const title = document.getElementById('title').value.trim();
    const content = $('#summernote').summernote('code');

    //QnaRequestDto 객체 생성
    const qnaReq = {
        title: title,
        content: content,
    };

    //QnA 생성 요청
    axios.post('/qna/', qnaReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        alert(response.data);
    })
    .catch(function(error) {
        console.error(error);

        if (error.response && error.response.data && error.response.data.errors) {
            alert(error.response.data.errors[0].message);
        } else {
            alert('서버 요청 중 에러가 발생했습니다.');
        }
    });
});

/*QnA 수정*/
document.getElementById('updateQnaBtn').addEventListener('click', function() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/qna/list";
        return;
    }

    const title = document.getElementById('title').value.trim();
    const content = $('#summernote').summernote('code');

    const qnaId = document.getElementById('qnaId').value;
    console.log('수정할 QnA: ' + qnaId);

    //UpdateQnaRequestDto 객체 생성
    const updateQnaReq = {
        title: title,
        content: content,
    };

    //QnA 수정 요청
    axios.put('/qna/' + qnaId, updateQnaReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        console.log(response.data);
        alert(response.data);
        window.location.href = "/qna/detail/" + qnaId;
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
});


//qnaDetail 값을 채우는 함수
function fillQnaDetail(qnaDetail) {
    document.getElementById('title').value = qnaDetail.title;
    $('#summernote').summernote('code', qnaDetail.content);
}