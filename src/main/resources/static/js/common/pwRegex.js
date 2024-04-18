/* 비밀번호 유효성 검사 */
document.addEventListener('DOMContentLoaded', function() {
    let isPwValidated = false;    //유효성 검사 완료 확인

    const newPwInput = document.getElementById('newPw');
    const checkPwInput = document.getElementById('checkPw');
    const regMsgs = document.querySelectorAll('.reg-msg');
    const checkCircles = document.querySelectorAll('.check-circle');

    //비밀번호 정규식 확인
    function validatePw(newPw) {
        const pwPattern = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
        return pwPattern.test(newPw);
    }

    //비밀번호 일치 확인
    function checkPwsMatch(newPw, checkPw) {
        return newPw == checkPw;
    }

    //UI 업데이트
    function updateRegMsg(input, index, isValid, message) {
        const regMsg = input.nextElementSibling;
        if (isValid) {
            regMsg.textContent = '';
            input.classList.remove('active-reg-input');
            regMsg.classList.remove('active-reg-msg');
            checkCircles[index].style.display = "inline";
            isPwValidated = true;
        } else {
            regMsg.textContent = message;
            input.classList.add('active-reg-input');
            regMsg.classList.add('active-reg-msg');
            checkCircles[index].style.display = "none";
            isPwValidated = false;
        }
    }

    //검증 결과에 따라 확인 버튼 제한
    function updateButtonState() {
        const isPwValidated = validatePw(newPwInput.value);
        const pwsMatch = checkPwsMatch(newPwInput.value, checkPwInput.value);
        updateRegMsg(newPwInput, 4, isPwValidated, '영문, 숫자, 특수문자 조합으로 이루어진 8~15자로 입력하세요');
        updateRegMsg(checkPwInput, 5, pwsMatch, '비밀번호가 일치하지 않습니다.');
    }

    newPwInput.addEventListener('input', function() {
        updateButtonState();
    });

    checkPwInput.addEventListener('input', function() {
        updateButtonState();
    });

    //isValidated(유효성 결과) 리턴하는 함수
    window.getPwValidationStatus = function() {
        return isPwValidated;
    };
});
