/* 유효성 검사 */
document.addEventListener("DOMContentLoaded", function () {
    const nicknameInput = document.getElementById('memberNickname');
    const nameInput = document.getElementById('memberName');
    const phoneNumberInput = document.getElementById('memberPhoneNumber');

    //최대 길이 설정
    const maxNicknameLength = 40;
    const maxNameLength = 15;
    const maxPhoneNumberLength = 15;

    //휴대폰 번호 정규식 패턴
    const numberPattern = /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/;

    //checkCircles
    const checkCircles = document.querySelectorAll('.material-symbols-outlined.check-circle');

    //입력 필드 이벤트 처리
    nicknameInput.addEventListener('input', function () {
        validateAndUpdateUI(nicknameInput, maxNicknameLength, checkCircles[0]);
    });

    nameInput.addEventListener('input', function () {
        validateAndUpdateUI(nameInput, maxNameLength, checkCircles[1]);
    });

    phoneNumberInput.addEventListener('input', function () {
        //숫자만 포함하는지 확인
        const isValidPhoneNumber = numberPattern.test(phoneNumberInput.value);
        //최대 길이 확인
        const isPhoneNumberValidLength = phoneNumberInput.value.length <= maxPhoneNumberLength;

        //유효성 검사 통과 여부에 따라 UI 업데이트
        if (isValidPhoneNumber && isPhoneNumberValidLength) {
            checkCircles[2].style.display = 'inline'; // check_circle 아이콘 보이기
        }
    });

    //유효성 검사, UI 업데이트하는 함수
    function validateAndUpdateUI(inputField, maxLength, checkCircle) {
        //최대 길이 확인
        if (inputField.value.length <= maxLength) {
            checkCircle.style.display = 'inline'; //check_circle 아이콘 보이기
        }
    }
});

/* 회원 정보 수정 */
document.querySelector('.updateMember-btn').addEventListener('click', function() {
    //새로운 닉네임, 이름, 휴대폰 번호 값 가져오기
    const newNickname = document.getElementById('memberNickname').value;
    const newName = document.getElementById('memberName').value;
    const newPhoneNumber = document.getElementById('memberPhoneNumber').value;

    //HTTP 요청을 위한 데이터 객체 생성
    const requestData = {
        nickname: newNickname,
        name: newName,
        phoneNumber: newPhoneNumber
    };

    //로컬 스토리지에서 액세스 토큰 가져오기
    const token = localStorage.getItem('accessToken');

    axios.put('/member/profile-detail', requestData, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(function (response) {
        alert(response.data);
    })
    .catch(function (error) {
        console.error(error);

        //에러 응답 처리
        if (error.response && error.response.data && error.response.data.errors) {
            //서버가 에러 응답을 반환한 경우
            alert(error.response.data.errors[0].message); //서버가 반환한 에러 메시지 표시
        } else {
            alert('서버 요청 중 에러가 발생했습니다.');
        }
    });
});