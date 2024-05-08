document.addEventListener('DOMContentLoaded', function() {
    checkLoginStatus(); //로그인 여부 따라 헤더 버튼 수정
    moveMenuItem(); //현재 있는 탭 표시
});

//로그아웃
document.querySelector('.button-logout').addEventListener('click', function() {
    logout();
});


//로그인 상태 확인, 헤더 버튼 표시 함수
function checkLoginStatus() {
    const token = localStorage.getItem('accessToken');

    if (token) {
        //payload에서 데이터 가져오기
        const payload = getPayloadData(token);

        //권한이 관리자이면 답변 작성 버튼 보이기
        if (payload.auth == "ROLE_ADMIN") {
            document.querySelector('.button-admin').style.display = 'block';
        } else {
            //토큰이 있는 경우 (로그인)
            document.querySelector('.button-mypage').style.display = 'block';
            document.querySelector('.button-logout').style.display = 'block';
        }
    } else {
        //토큰이 없는 경우 (로그아웃)
        document.querySelector('.button-login').style.display = 'block';
        document.querySelector('.button-join').style.display = 'block';
    }
}

//현재 있는 탭에 menu-acitve 클래스 추가 함수
function moveMenuItem() {
    //현재 URL에 따라 클래스 추가
    var currentPath = window.location.pathname;

    var menuItems = document.querySelectorAll('.menu a');
    menuItems.forEach(function(item) {
        item.classList.remove('menu-active'); // 모든 메뉴에서 클래스 제거
    });

    var qnaMenuItem = document.querySelector('.menu a[href^="/qna"]');
    var supportMenuItem = document.querySelector('.menu a[href^="/support"]');
    var animalMapMenuItem = document.querySelector('.menu a[href^="/animal-map"]');

    if (qnaMenuItem && currentPath.startsWith("/qna")) {
        qnaMenuItem.classList.add('menu-active');
    } else if (supportMenuItem && currentPath.startsWith("/support")) {
        supportMenuItem.classList.add('menu-active');
    } else if (animalMapMenuItem && currentPath.startsWith("/animal-map")) {
        animalMapMenuItem.classList.add('menu-active');
    }
}