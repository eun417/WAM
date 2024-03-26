/* 비밀번호 유효성 검사 */
document.addEventListener('DOMContentLoaded', function() {
    const newPwInput = document.getElementById('newPw');
    const checkPwInput = document.getElementById('checkPw');
    const regMsgs = document.querySelectorAll('.reg-msg');
    const finBtn = document.getElementById('finBtn');
    
    //비밀번호 정규식 확인
    function validatePw(newPw) {
        const regex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;
        return regex.test(newPw);
    }

    //비밀번호 일치 확인
    function checkPwsMatch(newPw, checkPw) {
        return newPwInput.value == checkPwInput.value;
    }

    //UI 업데이트
    function updateRegMsg(input, isValid, message) {
        const regMsg = input.nextElementSibling;
        if (isValid) {
          regMsg.textContent = '';
          input.classList.remove('active-reg-input');
          regMsg.classList.remove('active-reg-msg');
        } else {
          regMsg.textContent = message;
          input.classList.add('active-reg-input');
          regMsg.classList.add('active-reg-msg');
        }
    }

    //검증 결과에 따라 확인 버튼 제한
    function updateButtonState() {
        const isValidPw = validatePw(newPwInput.value);
        updateRegMsg(newPwInput, isValidPw, '영문, 숫자, 특수문자 조합으로 이루어진 8~15자로 입력하세요');
        const pwsMatch = checkPwsMatch(newPwInput.value, checkPwInput.value);
        updateRegMsg(checkPwInput, pwsMatch, '비밀번호가 일치하지 않습니다.');
        finBtn.disabled = !isValidPw || !pwsMatch;
    }

    newPwInput.addEventListener('input', function() {
        updateButtonState();
    });

    checkPwInput.addEventListener('input', function() {
        updateButtonState();
    });

});
