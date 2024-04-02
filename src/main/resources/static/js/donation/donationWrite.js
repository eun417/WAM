/*후원 생성*/
document.getElementById('createDonationBtn').addEventListener('click', function() {
    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');
    if (!token) {
        alert("로그인 후 이용해주세요.");
        window.location.href = "/support/list";
        return;
    }

    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = formatDate(document.getElementById('startDate').value);
    const endDate = formatDate(document.getElementById('endDate').value);
    const firstImg  = document.getElementById('firstImg').files[0];
    console.log('firstImg name:'+firstImg.name);
    const subheading = document.getElementById('subheading').value.trim();
    const content = $('#summernote').summernote('code');
    console.log('content:'+content);
    const commentCheck = checkCommentOption();

    //SupportRequestDto 객체 생성
    const supportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        subheading: subheading,
        content: content,
        commentCheck: commentCheck
    };

    const formData = new FormData();
    const json = JSON.stringify(supportReq);
    const blob = new Blob([json], { type: "application/json" });
    formData.append('supportReq', blob);
    formData.append('firstImg', firstImg);


    //후원 생성 요청을 보냄
    axios.post('/support/', formData, {
        headers: {
            'Authorization': 'Bearer ' + token,
            'Content-Type': 'multipart/form-data'
        }
    }).then(function(response) {
        console.log(response.data);
        alert(response.data);
    })
    .catch(function(error) {
        console.error(error);
        alert(error.response.data.message);
    });
});

/*후원 수정*/
document.getElementById('updateDonationBtn').addEventListener('click', function() {
    const title = document.getElementById('title').value.trim();
    const animalSubjects = document.getElementById('subject').value;
    const goalAmount = document.getElementById('goalAmount').value.trim();
    const startDate = formatDate(document.getElementById('startDate').value);
    const endDate = formatDate(document.getElementById('endDate').value);
    const firstImg  = document.getElementById('firstImg').files[0];
    console.log('firstImg name:'+firstImg.name);
    const subheading = document.getElementById('subheading').value.trim();
    const content = document.querySelector('#summernote').textContent;
    console.log('content:'+content);
    const commentCheck = checkCommentOption();

    const supportId = document.getElementById('supportId').value;

    //기존 대표 이미지 삭제 여부(기본값:false)
    var firstImgDeleted = false;

    //대표 이미지를 새로 설정한 경우
    if (newFirstImg !== null) {
        //기존 대표 이미지 삭제
        firstImgDeleted = true;
    }

    //UpdateSupportRequestDto 객체 생성
    const updateSupportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        firstImgDeleted: firstImgDeleted,
        newFirstImg: newFirstImg,
        subheading: subheading,
        content: content,
        commentCheck: commentCheck
    };

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    //후원 수정 요청을 보냄
    axios.post('/support/' + supportId, supportReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        //서버 응답 처리
        console.log(response);
        alert('후원 수정 성공');
    })
    .catch(function(error) {
        //오류 처리
        console.error(error);
        alert('후원 수정 실패');
    });
});

//날짜 형식 변환 함수
function formatDate(inputDate) {
    const selectedDate = new Date(inputDate);

    const year = selectedDate.getFullYear();
    const month = (selectedDate.getMonth() + 1).toString().padStart(2, '0'); //월은 0부터 시작하므로 +1 해줌
    const day = selectedDate.getDate().toString().padStart(2, '0');

    return `${year}.${month}.${day}`;
}