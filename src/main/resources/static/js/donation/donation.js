/*supportWrite 페이지 이동(create)*/
function goToSupportWrite(event) {
    event.preventDefault(); //기본 이벤트 실행 방지

    //로컬 스토리지에서 액세스 토큰 가져오기
    const token = localStorage.getItem('accessToken');

    if (!token) {
        alert('로그인 후 이용해주세요.');
        return;
    }

    //토큰이 있는 경우 페이지 이동
    window.location.href = "/support/write";
}


/*donationSearch 페이지 이동*/
document.getElementById('searchBtn').addEventListener('click', function() {
    var searchValue = document.querySelector('.search').value;
    window.location.href = "/support/search?q=" + searchValue;
});