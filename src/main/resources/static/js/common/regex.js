/* 회원가입 유효성 검사 */
document.addEventListener('DOMContentLoaded', function() {
    let isValidated = false;    //유효성 검사 완료 확인

    const nameInput = document.getElementById('name');
    const nicknameInput = document.getElementById('nickname');
    const phoneNumberInput = document.getElementById('phoneNumber');
    const passwordInput = document.getElementById('password');
    const checkPasswordInput = document.getElementById('checkPassword');
    const regMsgs = document.querySelectorAll('.reg-msg');
    const checkCircles = document.querySelectorAll('.check-circle');

    const nameMaxLength = 15;
    const nicknameMaxLength = 40;
    const phoneNumberPattern = /^01(?:0|1|[6-9])(?:\d{3}|\d{4})\d{4}$/;
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$/;

    function validateInput(input, index) {
        if (!input) return; //input 이 null 이면 함수 종료

        input.addEventListener('input', function() {
            let errorMessage = '';

            switch(index) {
                case 0: //이름
                    if (nameInput.value.trim().length === 0) {
                        errorMessage = '이름을 입력하세요.';
                    } else if (nameInput.value.trim().length > nameMaxLength) {
                        errorMessage = '이름은 최대 15자까지 입력 가능합니다.';
                    }
                    break;
                case 1: //닉네임
                    if (nicknameInput.value.trim().length === 0) {
                        errorMessage = '닉네임을 입력하세요.';
                    } else if (nicknameInput.value.trim().length > nicknameMaxLength) {
                        errorMessage = '닉네임은 최대 40자까지 입력 가능합니다.';
                    }
                    break;
                case 2: //휴대폰 번호
                    if (phoneNumberInput.value.trim().length === 0) {
                        errorMessage = '휴대폰 번호를 입력하세요.';
                    } else if (!phoneNumberPattern.test(phoneNumberInput.value.trim())) {
                        errorMessage = '올바른 휴대폰 번호를 입력하세요.';
                    }
                    break;
                case 3: //비밀번호
                    if (passwordInput.value.trim().length === 0) {
                        errorMessage = '비밀번호를 입력하세요.';
                    } else if (!passwordPattern.test(passwordInput.value.trim())) {
                        errorMessage = '영문, 숫자, 특수문자를 포함하여 8~15자로 입력하세요.';
                    }
                    break;
                case 4: //비밀번호 재입력
                    if (checkPasswordInput.value.trim().length === 0) {
                        errorMessage = '비밀번호를 다시 입력하세요.';
                    } else if (passwordInput.value.trim() !== checkPasswordInput.value.trim()) {
                        errorMessage = '비밀번호가 일치하지 않습니다.';
                    }
                    break;
            }

            //에러 메시지 표시
            if (errorMessage) {
                input.classList.add('active-reg-input');
                regMsgs[index].classList.add('active-reg-msg');
                regMsgs[index].textContent = errorMessage;
                checkCircles[index].style.display = "none";
                isValidated = false;
            } else {
                input.classList.remove('active-reg-input');
                regMsgs[index].classList.remove('active-reg-msg');
                regMsgs[index].textContent = '';
                checkCircles[index].style.display = "inline";
                isValidated = true;
            }
        });
    }

    validateInput(nameInput, 0);
    validateInput(nicknameInput, 1);
    validateInput(phoneNumberInput, 2);
    validateInput(passwordInput, 3);
    validateInput(checkPasswordInput, 4);

    //isValidated(유효성 결과) 리턴하는 함수
    window.getValidationStatus = function() {
        return isValidated;
    };
});