/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    loadProfileInformation();
});


/*회원 정보 조회*/
function loadProfileInformation() {
    api.get('/member/profile-detail')
        .then(function (response) {
            const memberData = response.data;
            document.getElementById('memberId').value = memberData.memberId;
            document.getElementById('email').value = memberData.email;
            document.getElementById('nickname').value = memberData.nickname;
            document.getElementById('name').value = memberData.name;
            document.getElementById('phoneNumber').value = memberData.phoneNumber;
        })
        .catch(function (error) {
            console.error(error);
        });
}


/*회원 정보 수정*/
document.querySelector('#updateMemberBtn').addEventListener('click', function() {
    const isRegexValidated = window.getValidationStatus();
    console.log("isRegexValidated", isRegexValidated);
    if (!isRegexValidated) {
        alert('정보를 다시 입력해주세요.');
        return;
    }

    const passwordInput = document.getElementById('password').value.trim();
    if (passwordInput !== '') {
        alert('비밀번호는 따로 변경해주세요.');
        return;
    }

    //UpdateMemberRequestDto 객체 생성
    const newNickname = document.getElementById('nickname').value.trim();
    const newName = document.getElementById('name').value.trim();
    const inputPhoneNumber = document.getElementById('phoneNumber').value.trim();
    const newPhoneNumber = inputPhoneNumber !== '' ? inputPhoneNumber : null;

    const updateMemberReq = {
        nickname: newNickname,
        name: newName,
        phoneNumber: newPhoneNumber
    };

    api.put('/member/profile-detail', updateMemberReq)
        .then(function (response) {
            alert(response.data);
            window.location.href = '/member/profile';
        })
        .catch(function (error) {
            console.error(error);
            //서버가 반환한 에러 메시지 표시
            if (error.response && error.response.data && error.response.data.errors) {
                alert(error.response.data.errors[0].message);
            } else {
                alert(error.response.data.message);
            }
        });
});


/*비밀번호 변경*/
document.getElementById('updatePwBtn').addEventListener('click', function() {
    const isPwValidated = window.getPwValidationStatus();
    if (!isPwValidated) {
        alert('비밀번호를 다시 입력해주세요.');
        return;
    }

    //UpdatePwRequestDto 객체 생성
    const nowPw = document.getElementById('password').value;
    const newPw = document.getElementById('newPw').value;
    const checkPw = document.getElementById('checkPw').value;

    const updatePwReq = {
        nowPassword: nowPw,
        newPassword: newPw,
        checkPassword: checkPw
    };

    api.put(`/member/profile-detail/pw`, updatePwReq)
        .then(function (response) {
          alert(response.data)
          window.location.reload(); //새로고침
        })
        .catch(function (error) {
            console.error('비밀번호 변경 실패', error);
            //서버가 반환한 에러 메시지 표시
            if (error.response && error.response.data && error.response.data.errors) {
                alert(error.response.data.errors[0].message);
            } else {
                alert(error.response.data.message);
            }
        });
});