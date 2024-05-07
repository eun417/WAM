/*로그인 버튼 클릭*/
document.getElementById('loginBtn').addEventListener('click', function() {
    login();
});

//Enter 하면 로그인 버튼 클릭
function enterLoginBtn(event) {
    if (event.code === 'Enter') {
        event.preventDefault();
        document.getElementById('loginBtn').click();
    }
}

function login() {
    //LoginRequest 객체 생성
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    const loginReq = {
        email: email,
        password: password
    };

    //로그인 요청
    axios.post('/auth/login', loginReq)
        .then(function(response) {
            console.log(response);

            const accessToken = response.data.accessToken;
            const refreshToken = response.data.refreshToken;
            //서버에서 반환한 토큰을 로컬 스토리지에 저장
            localStorage.setItem('accessToken', accessToken);
            localStorage.setItem('refreshToken', refreshToken);

            //payload에서 데이터 가져오기
            const payload = getPayloadData(accessToken);
            //권한에 따라 다른 페이지로 이동
            if (payload.auth == "ROLE_ADMIN") {
                window.location.href = "/admin/member/list";
            } else {
                window.location.href = "/";
            }
        })
        .catch(function(error) {
            console.error('로그인 실패', error);
            alert(error.response.data.message);
        });
}