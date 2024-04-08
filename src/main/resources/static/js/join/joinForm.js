let finVerified = false;    //이메일 인증 완료 확인

/* 전송 요청 버튼 클릭 */
document.getElementById("sendEmailButton").addEventListener("click", function() {
    //이메일 가져옴
    const email = document.getElementById("emailInput").value;

    axios.post('/auth/signup/email/send', { email: email })
        .then(function (response) {
            console.log(response);
            alert(response.data);
            //인증번호 입력창 보이기
            const authBox = document.getElementById('auth-box');
            const sendEmailBtn = document.getElementById('sendEmailButton');
            authBox.style.display = 'block';
            sendEmailBtn.style.display = 'none';
        })
        .catch(function (error) {
            console.error(error);
            alert(error.response.data.message);
        });
});


/* 인증하기 버튼 클릭 */
document.getElementById("verifyButton").addEventListener("click", function() {
    //이메일과 인증번호를 가져옴
    const email = document.getElementById("emailInput").value;
    const authCode = document.getElementById("authCodeInput").value;

    //VerifyEmailRequestDto 객체 생성
    const verifyEmailReq = {
        email: email,
        authCode: authCode
    };

    //이메일 인증 요청을 보냄
    axios.post('/auth/signup/email/verify', verifyEmailReq)
        .then(function (response) {
            console.log(response);
            alert(response.data);
            finVerified = true;
        })
        .catch(function (error) {
            console.error(error);
            alert(error.response.data.message);
        });
});


/* 회원가입 */
document.getElementById('joinBtn').addEventListener('click', function() {
    const emailValue = document.getElementById('email').value.trim();
    const nameValue = document.getElementById('name').value.trim();
    const nicknameValue = document.getElementById('nickname').value.trim();
    const phoneNumberValue = document.getElementById('phoneNumber').value.trim();
    const passwordValue = document.getElementById('password').value.trim();
    const checkPasswordValue = document.getElementById('checkPassword').value.trim();

    //유효성 검사 결과
    const isValidated = getValidationStatus();

    if (!isValidated) {
        alert('입력한 정보를 다시 확인해주세요.');
    } else {
        //SignUpRequestDto 객체 생성
        const signUpReq = {
            email: emailValue,
            password: passwordValue,
            checkPassword: checkPasswordValue,
            nickname: nicknameValue,
            name: nameValue,
            phoneNumber: phoneNumberValue
        };

        //회원가입 요청
        axios.post('/auth/signup', signUpReq)
            .then(function (response) {
                console.log(response.data);
                alert(response.data);
                window.location.href = '/auth/login'; //로그인 페이지로 이동
            })
            .catch(function (error) {
                console.error(error);
                alert('회원가입에 실패했습니다. 다시 시도해주세요.')
            });
    }
});