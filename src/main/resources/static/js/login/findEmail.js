/* 이메일 찾기 */
document.getElementById('finBtn').addEventListener('click', function() {
    //FindEmailRequestDto 객체 생성
    const phoneNumber = document.getElementById('phoneNumber').value.trim();
    const name = document.getElementById('name').value.trim();

    //입력값이 비어 있는지 확인
    if (phoneNumber === '' || name === '') {
        alert('정보를 입력해주세요.');
        return;
    }

    const findEmailReq = {
      phoneNumber: phoneNumber,
        name: name
    };

    axios.post('/auth/find-account', findEmailReq)
        .then(function (response) {
            console.log(response.data);

            //입력창 숨기기
            const findEmailInputDiv = document.getElementById('findEmailInput');
            findEmailInputDiv.style.display = 'none';

            //결과 출력
            const emailResultDiv = document.getElementById('emailResult');
            const email = response.data.findEmail;    //찾은 이메일
            emailResultDiv.innerHTML = `<div>${name}님의 이메일은<br><span style="color: #445D48;">${email}</span>입니다.</div>`;
        })
        .catch(function (error) {
            console.error('이메일 찾기 실패', error);
            alert(error.response.data.message);
        });
});