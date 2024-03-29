/*qnaDetail 페이지 이동*/
function goToQnaDetail(event, qnaId) {
    event.preventDefault(); //기본 이벤트 실행 방지
    window.location.href = "/qna/detail/" + qnaId;
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