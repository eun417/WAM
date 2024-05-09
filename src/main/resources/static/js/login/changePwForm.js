/* 비밀번호 재설정 요청 */
document.getElementById('finBtn').addEventListener('click', function() {
    const { authCode } = getQueryParams();
    console.log(authCode);

    //ChangePwRequestDto 객체 생성
    const newPw = document.getElementById('newPw').value.trim();
    const checkPw = document.getElementById('checkPw').value.trim();

    //입력값이 비어 있는지 확인
    if (newPw === '' || checkPw === '') {
        alert('정보를 입력해주세요.');
        return;
    }

    const changePwReq = {
        newPassword: newPw,
        checkPassword: checkPw
    };

    axios.put(`/auth/change-pw/${authCode}`, changePwReq)
        .then(function (response) {
          console.log(response);
          alert(response.data)
          window.location.href = '/auth/login';
        })
        .catch(function (error) {
          console.error('비밀번호 재설정 실패:', error.response.data);
          alert(error.response.data.message);
          window.location.replace('/auth/change-pw');
        });
});

//URL에서 매개변수 추출 함수
function getQueryParams() {
    const urlParams = new URLSearchParams(window.location.search);
    const authCode = urlParams.get('authCode');
    console.log(authCode);
    return { authCode, authCode };
}