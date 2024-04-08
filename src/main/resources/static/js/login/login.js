/*로그인 버튼 클릭*/
document.getElementById('loginBtn').addEventListener('click', function() {
    //LoginRequest 객체 생성
    var email = document.getElementsByName('email')[0].value;
    var password = document.getElementsByName('password')[0].value;

    const loginReq = {
        email: email,
        password: password
    };

    //로그인 요청
    axios.post('/auth/login', loginReq)
        .then(function(response) {
            console.log(response);
            alert('로그인 성공!');

            const accessToken = response.data.accessToken;
            const refreshToken = response.data.refreshToken;
            saveTokenToLocalStorage(accessToken, refreshToken); //서버에서 반환한 토큰을 로컬 스토리지에 저장
        })
        .catch(function(error) {
            console.error('로그인 실패', error);
            alert(error.response.data.message);
        });
});