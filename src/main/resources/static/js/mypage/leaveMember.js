document.querySelector('.leaveMember-btn').addEventListener('click', function() {
    var memberId = document.getElementById('memberId').value;
    console.log('탈퇴할 회원: ' + memberId);
    var password = document.getElementById('password').value;
    var quitAgreement = document.getElementById('quit-agreement').checked; //탈퇴 동의 여부 가져오기

    //필요한 유효성 검사 등을 수행
    if (!quitAgreement) {
        alert('탈퇴에 동의해주세요.');
        return;
    }

    if (confirm("정말로 탈퇴하시겠습니까?")) {
        //회원 탈퇴 요청
        api.delete('/member/leave', {
            data: {
                password: password
            }
        }).then(function(response) {
            alert(response.data);
        })
        .catch(function(error) {
            console.error(error);
            alert(error.response.data.message);
        });
    }
});