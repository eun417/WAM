//로컬 스토리지에서 토큰을 가져오는 함수
function getToken() {
    return localStorage.getItem('accessToken');
}

//payload에서 데이터 가져오기
function getPayloadData() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    //토큰이 없는 경우 0 반환
    if (!token) {
        return 0;
    }
    //토큰의 payload 부분 추출
    var base64Payload = token.split('.')[1];    //value 0: header, 1: payload, 2: VERIFY SIGNATURE
    //base64 디코딩 및 JSON 객체로 변환
    const payload = JSON.parse(atob(base64Payload.replace(/-/g, '+').replace(/_/g, '/')));
    return payload;
}

//토큰 만료 시간 확인
function getTokenExpirationDate(exp) {
    //UNIX 시간 스탬프를 밀리초 단위로 변환
    const milliseconds = exp * 1000;

    //밀리초를 기반으로 Date 객체 생성
    const date = new Date(milliseconds);

    //Date 객체를 사용하여 날짜 및 시간 표시
    console.log("토큰 만료 시간:"+date);
}

//토큰 만료 여부를 검사, 로그아웃 처리 함수
function checkTokenExpiration() {
    const token = getToken();
    const payload = getPayloadData();
    getTokenExpirationDate(payload.exp);

    if (token) {
        //토큰 있는 경우: 로그인 상태
        const tokenExpiration = payload.exp * 1000; //밀리초 단위로 변환
        const currentTime = Date.now();
        if (tokenExpiration && tokenExpiration < currentTime) {
            console.log("토큰 만료")
            //토큰 만료된 경우: 로그아웃 처리
            logout();
        }
    }
}

//로그인 상태 확인, 화면 표시 함수
function checkLoginStatus() {
    const token = getToken();

    if (token) {
        //토큰이 있는 경우 (로그인)
        document.querySelector('.button-login').style.display = 'none';
        document.querySelector('.button-join').style.display = 'none';
        document.querySelector('.button-mypage').style.display = 'block';
        document.querySelector('.button-logout').style.display = 'block';
    } else {
        //토큰이 없는 경우 (로그아웃)
        document.querySelector('.button-mypage').style.display = 'none';
        document.querySelector('.button-logout').style.display = 'none';
        document.querySelector('.button-login').style.display = 'block';
        document.querySelector('.button-join').style.display = 'block';
    }
}

/*페이지 로드 시 토큰 만료 검사 실행*/
document.addEventListener('DOMContentLoaded', function() {
    checkTokenExpiration();
    checkLoginStatus();
});