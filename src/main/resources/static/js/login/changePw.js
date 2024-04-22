/* 비밀번호 재설정 링크 메일 전송 */
document.getElementById('sendMailBtn').addEventListener('click', function() {
    //ChangePwLinkRequestDto 객체 생성
    const name = document.getElementById('name').value.trim();
    const email = document.getElementById('email').value.trim();

    //입력값이 비어 있는지 확인
    if (name === '' || email === '') {
        alert('정보를 입력해주세요.');
        return;
    }

    const changePwLinkReq = {
        name: name,
        email: email
    };

    axios.post('/auth/change-pw/email/send', changePwLinkReq)
        .then(function (response) {
            console.log(response);
            alert(response.data);
        })
        .catch(function (error) {
            console.error('메일 전송 실패:', error);
            alert(error.response.data.message);
        });
});