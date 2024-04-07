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
        if (payload.auth == "ADMIN") {
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
        if (payload.auth == "ADMIN") {
            document.querySelector(btnName).style.display = 'none';
        }
    }
}