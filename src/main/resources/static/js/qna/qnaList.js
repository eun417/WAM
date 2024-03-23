/*qnaDetail 페이지 이동*/
function goToQnaDetail(event, qnaId) {
    event.preventDefault(); //기본 이벤트 실행 방지

    //로컬 스토리지에서 액세스 토큰 가져오기
    const token = localStorage.getItem('accessToken') || null;

    const config = {
        headers: {}
    };

    if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
    }

    //서버의 컨트롤러로 이동
    axios.get(`/qna/detail/${qnaId}`, config)
        .then(function(response) {
            window.location.href = "/qna/detail/" + qnaId;
        })
        .catch(function(error) {
            console.error(error);
            alert('QnA를 불러오지 못했습니다.');
        });
}

/*qnaWrite 페이지 이동*/
function goToQnaWrite(event) {
    event.preventDefault(); //기본 이벤트 실행 방지

    //로컬 스토리지에서 액세스 토큰 가져오기
    const token = localStorage.getItem('accessToken') || null;

    if (!token) {
        alert('로그인 후 이용해주세요.');
        return;
    }

    //토큰이 있는 경우 페이지 이동
    window.location.href = "/qna/write";
}

/*qnaSearch 페이지 이동*/
document.getElementById('search-btn').addEventListener('click', function() {
    var selectValue = document.querySelector('.select').value;
    var searchValue = document.querySelector('.QnAsearch').value;
    window.location.href = "/qna/search?select=" + selectValue + "&search=" + searchValue;
});