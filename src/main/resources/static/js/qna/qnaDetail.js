/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadQnaDetail();
});


//로컬 스토리지에서 액세스 토큰 가져오기
const token = localStorage.getItem('accessToken');


/*QnA 상세 조회*/
function loadQnaDetail() {
    const qnaId = document.getElementById('qnaId').value;
    console.log('조회된 QnA:' + qnaId);

    axios.get(`/qna/${qnaId}`)
        .then(function(response) {
            var qnaDetail = response.data;

            document.title = qnaDetail.title;   //페이지 제목 설정

            var titleElement = document.querySelector('.D-title');
            var nicknameElement = document.querySelector('.D-top');
            var createDateElement = document.querySelector('.D-date');
            var viewCountElement = document.querySelector('.viewCount-num');
            var contentElement = document.querySelector('.D-textarea');

            //데이터 넣기
            document.getElementById('qnaId').value = qnaDetail.qnaId;
            nicknameElement.textContent = qnaDetail.nickname;
            createDateElement.textContent = qnaDetail.createDate;
            viewCountElement.textContent = qnaDetail.viewCount;
            titleElement.textContent = qnaDetail.title;
            contentElement.innerHTML = qnaDetail.content;   //HTML로 렌더링

            //답변한 QnA인 경우
            if (qnaDetail.answerDate !== null && qnaDetail.answer !== null) {
                var answerDateElement = document.querySelector('.answer-date');
                var answerContentElement = document.querySelector('.answer-content');
                var answerTextarea = document.getElementById('answerTextarea');
                answerDateElement.textContent = qnaDetail.answerDate;
                answerContentElement.textContent = qnaDetail.answer;
                answerTextarea.value = qnaDetail.answer;

                hideAnswerBtnForAdmin(token, '.btn-answer');   //답변 작성 버튼 보이기
                showHideElement('#answerContent');  //답변 내용 보이기

                //답변 작성 버튼 텍스트 변경
                var answerButton = document.querySelector('.btn-answer');
                if (answerButton) {
                    answerButton.textContent = '답변 수정하기';
                }
            }

            //답변 상태에 따라 버튼 스타일 설정
            var answerButton = document.querySelector('.answer-comp');
            answerButton.classList.add(qnaDetail.qnaCheck === 'CHECKING' ? 'bc3' : 'bc7');
            answerButton.textContent = qnaDetail.qnaCheck === 'CHECKING' ? '확인 중' : '답변 완료';

            //로그인 사용자에 따라 다른 버튼 보이기
            showUpDelBtnForWriter(token, qnaDetail.memberId);  //수정&삭제 버튼
            showAnswerBtnForAdmin(token, '.btn-answer');   //답변 작성 버튼
        })
        .catch(function(error) {
            console.error(error);
            alert('QnA를 불러오지 못했습니다.');
            window.location.href = "/qna/list";  //목록 페이지로 이동
        });
}

/*답변 등록 버튼 클릭 이벤트 핸들러*/
document.querySelector('.btn-answer').addEventListener('click', function() {
    const answerTextarea = document.querySelector('#answerTextarea');
    //textarea 가 보이는지 확인
    if (answerTextarea.style.display === 'block') {
        //QnaAnswerRequestDto 객체 생성
        const qnaAnswerReq = {
            answer: answerTextarea.value
        };

        const qnaId = document.getElementById('qnaId').value;

        //답변 등록 요청
        axios.put(`/qna/${qnaId}/answer`, qnaAnswerReq, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }).then(function(response) {
            console.log(response.data);
            alert(response.data);
            window.location.href = `/qna/detail/${qnaId}`;  //페이지 로드
        })
        .catch(function(error) {
            console.error('답변 등록 실패', error);
            alert(error.response.data.message);
        });
    } else {
        //textarea 가 보이지 않는 경우 보이게 하기
        showHideElement('#answerTextarea');

        //기존 답변이 있는 경우
        if (answerTextarea.value) {
            //기존 답변 숨기기
            showHideElement('#answerContent');
        }
    }
});

/*qnaWrite 페이지 이동(update)*/
document.getElementById('updateQnaBtn').addEventListener('click', function() {
    var qnaId = document.getElementById('qnaId').value;
    console.log('수정할 후원: ' + qnaId);

    if (!token) {
        alert('로그인 후 이용해주세요.');
        return;
    }

    //토큰이 있는 경우 페이지 이동
    window.location.href = "/qna/update/" + qnaId;
});

/*QnA 삭제*/
document.getElementById('deleteQnaBtn').addEventListener('click', function() {
    const qnaId = document.getElementById('qnaId').value;
    console.log('삭제할 QnA: ' + qnaId);

    if (confirm("정말로 QnA를 삭제하시겠습니까?")) {
        //사용자가 확인을 누르면 QnA 삭제 요청
        axios.delete('/qna/' + qnaId, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }).then(function(response) {
            console.log(response.data);
            alert(response.data);
            window.location.href = "/qna/list";  //목록 페이지로 이동
        })
        .catch(function(error) {
            console.error('QnA 삭제 실패', error);
            alert(error.response.data.message);
        });
    }
});


//요소를 보이게 하거나 숨기는 함수
function showHideElement(element) {
    var element = document.querySelector(element);
    if (element.style.display === 'none') {
        element.style.display = 'block';
    } else {
        element.style.display = 'none';
    }
}

//로그인 사용자가 작성자인지 확인하는 함수
function showUpDelBtnForWriter(token, memberId) {
    if (token) {
        //payload 에서 데이터 가져오기
        const payload = getPayloadData(token);
        console.log('login memberId:'+payload.sub);

        //memberId가 같으면 버튼 보이기
        if (payload.sub == memberId) {
            document.querySelector('.D-writer').style.removeProperty('display'); //display 속성 제거
        }
    }
}

//로그인 사용자가 관리자면 버튼 보이게 하는 함수
function showAnswerBtnForAdmin(token, btnName) {
    if (token) {
        //payload에서 데이터 가져오기
        const payload = getPayloadData(token);

        //권한이 관리자이면 답변 작성 버튼 보이기
        if (payload.auth == "ROLE_ADMIN") {
            showHideElement(btnName);
        }
    }
}

//로그인 사용자가 관리자면 버튼 안 보이게 하는 함수
function hideAnswerBtnForAdmin(token, btnName) {
    if (token) {
        //payload에서 데이터 가져오기
        const payload = getPayloadData(token);

        //권한이 관리자이면 답변 작성 버튼 보이기
        if (payload.auth == "ROLE_ADMIN") {
            document.querySelector(btnName).style.display = 'none';
        }
    }
}