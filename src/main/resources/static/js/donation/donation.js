/*후원 생성*/
document.getElementById('createSupportButton').addEventListener('click', function() {
    var title = document.getElementsByName('title')[0].value;
    var animalSubjects = document.getElementsByName('animalSubjects')[0].value;
    var goalAmount = document.getElementsByName('goalAmount')[0].value;
    var startDate = document.getElementsByName('startDate')[0].value;
    var endDate = document.getElementsByName('endDate')[0].value;
    var firstImg = document.getElementsByName('firstImg')[0].value;
    var subheading = document.getElementsByName('subheading')[0].value;
    var content = document.getElementsByName('content')[0].value;
    var commentCheck = document.getElementsByName('commentCheck')[0].value;

    //SupportRequestDto 객체 생성
    const supportReq = {
        title: title,
        animalSubjects: animalSubjects,
        goalAmount: goalAmount,
        startDate: startDate,
        endDate: endDate,
        firstImg: firstImg,
        subheading: subheading,
        content: content,
        commentCheck: commentCheck
    };

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    //후원 생성 요청을 보냄
    axios.post('/support', supportReq, {
        headers: {
            'Authorization': 'Bearer ' + token
        }
    }).then(function(response) {
        //서버 응답 처리
        console.log(response);
        alert('후원 생성 성공');
    })
    .catch(function(error) {
        //오류 처리
        console.error(error);
        alert('후원 생성 실패');
    });
});


/*후원 수정*/
document.getElementById('updateSupportButton').addEventListener('click', function() {
    var title = document.getElementsByName('title')[0].value;
    var animalSubjects = document.getElementsByName('animalSubjects')[0].value;
    var goalAmount = document.getElementsByName('goalAmount')[0].value;
    var startDate = document.getElementsByName('startDate')[0].value;
    var endDate = document.getElementsByName('endDate')[0].value;
    var newFirstImg = document.getElementsByName('newFirstImg')[0].value;
    var subheading = document.getElementsByName('subheading')[0].value;
    var content = document.getElementsByName('content')[0].value;
    var commentCheck = document.getElementsByName('commentCheck')[0].value;

    const supportId = document.getElementById('supportId').value;

    var firstImgDeleted = false; //기본값 설정

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


/*후원 삭제*/
document.getElementById('deleteSupportButton').addEventListener('click', function() {
    var supportId = document.getElementById('supportId').value;
    console.log(supportId);

    //토큰을 로컬 스토리지에서 가져오기
    const token = localStorage.getItem('accessToken');

    if (confirm("정말로 후원을 삭제하시겠습니까?")) {
        //사용자가 확인을 누르면 컨트롤러 호출
        axios.delete('/support/' + supportId, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        }).then(function(response) {
            //요청이 성공한 경우
            console.log(response);
            alert("후원 삭제가 완료되었습니다.");
        })
        .catch(function(error) {
            //요청이 실패한 경우
            console.error(error);
            alert("후원 삭제에 실패하였습니다.");
        });
    }
});