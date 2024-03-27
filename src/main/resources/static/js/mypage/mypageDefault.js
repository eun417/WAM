//페이지 로드 시 프로필 정보를 가져와서 처리하는 함수
function loadProfileInformation() {
    const token = localStorage.getItem('accessToken');

    axios.get('/member/profile-detail', {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    })
    .then(function (response) {
        const memberData = response.data;

        renderMenu(memberData.authority);

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

//회원 권한에 따라 메뉴 안내 변경
function renderMenu(authority) {
    var noticeElement = document.querySelector(".detail-notice");

    if (authority === "GUEST") {
        noticeElement.textContent = "현재 임시 회원입니다!";
    } else if (authority === "USER") {
        noticeElement.textContent = "현재 일반 회원입니다!";
    } else if (authority === "ADMIN") {
        noticeElement.textContent = "관리자 권한입니다!";
    }
}

//페이지 로드 시 프로필 정보를 가져와서 처리
loadProfileInformation();