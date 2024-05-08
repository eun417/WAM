/*소셜 로그인 성공시 토큰 저장*/
function socialLoginSuccess() {
    // URL에서 쿼리 매개변수 추출
    const urlParams = new URLSearchParams(window.location.search);
    const accessToken = urlParams.get('accessToken');
    const refreshToken = urlParams.get('refreshToken');

    // 추출한 토큰 값을 로컬 스토리지에 저장
    if (accessToken && refreshToken) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      window.location.replace('/');
      return;
    }
}


/*총 후원금 조회 함수*/
function loadTotalAmount() {
    axios.get('/payment/total-amount')
        .then(function(response) {
            const totalAmount = response.data;
            document.querySelector('.donation-total-amount>p>span').textContent = totalAmount;
        })
        .catch(function(error) {
            console.error(error);
        });
}


/*종료 임박 후원 조회 함수*/
function loadEndingSoonSupport() {
    axios.get('/support/ending-soon')
        .then(function(response) {
            const endingSoonSupportList = response.data;
            let hasData = false; // 데이터 조회 여부를 나타내는 변수

            if (endingSoonSupportList && endingSoonSupportList.length > 0) {
                hasData = true; // 데이터가 있을 경우 true 로 설정

                //랜덤한 인덱스 선택
                const randomIndex = Math.floor(Math.random() * endingSoonSupportList.length);
                const randomEndingSoonSupport = endingSoonSupportList[randomIndex];

                //graph의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                const progress = (randomEndingSoonSupport.supportAmount / randomEndingSoonSupport.goalAmount) * 100;
                const graphWidth = progress > 100 ? '100%' : progress + '%';
                const graph = `<div class="graph" style="width: ${graphWidth};"></div>`;

                let box = `<div class="ending-soon-box">
                               <div class="ending-soon-img-box">
                                   <div class="remaining-time"><span class="material-symbols-outlined">schedule</span><span></span></div>
                                   <div class="donation-status ending-soon">
                                       ${randomEndingSoonSupport.supportStatus === 'ENDING_SOON' ? '종료 임박' : ''}
                                   </div>
                                   <div class="ending-soon-img"><img src="${randomEndingSoonSupport.firstImg}" class="first-pic" alt="animal"></div>
                               </div>
                               <div class="ending-soon-detail">
                                   <div class="ending-soon-title">${randomEndingSoonSupport.title}</div>
                                   <div class="donation-nickname gray-text">${randomEndingSoonSupport.nickname}</div>
                                   <div class="ending-soon-content">
                                       <div class="ending-soon-amount">
                                           <div class="percent">
                                               ${graph}
                                               <div class="graph-bottom"></div>
                                           </div>
                                           <div class="amount">
                                               <p><span>${randomEndingSoonSupport.supportAmount}</span>원</p>
                                               <p><span>${randomEndingSoonSupport.goalAmount}</span>원 목표</p>
                                           </div>
                                       </div>
                                   </div>
                               </div>
                           </div>`;

                document.querySelector('.deadline-donation-box .content-box').innerHTML = box;

                //종료 임박 후원이 없는 경우 보이게 함
                document.querySelector('.deadline-donation-box').style.display = 'block';

                //종료 임박 후원이 있을 경우 카운트
                if (hasData) {
                    setInterval(updateTime, 1000);  //남은 후원 시간
                }
            } else {
                console.log('종료 임박 후원 없음');
                //종료 임박 후원이 없는 경우 숨김
                document.querySelector('.deadline-donation-box').style.display = 'none';
            }
        })
        .catch(function(error) {
            console.error(error);
        });
}

//남은 후원 시간 카운트하는 함수
function updateTime() {
    let now = new Date();
    let midnight = new Date();
    midnight.setHours(24, 0, 0, 0); //자정

    let timeUntilMidnight = midnight - now; //자정까지의 시간(밀리초 단위)

    //시, 분, 초로 변환
    let hours = Math.floor(timeUntilMidnight / (1000 * 60 * 60));
    let minutes = Math.floor((timeUntilMidnight % (1000 * 60 * 60)) / (1000 * 60));
    let seconds = Math.floor((timeUntilMidnight % (1000 * 60)) / 1000);

    //두 자리 숫자로 포맷팅
    hours = formatTwoDigits(hours);
    minutes = formatTwoDigits(minutes);
    seconds = formatTwoDigits(seconds);

    //시간을 화면에 표시
    const secondSpan = document.querySelector('.remaining-time span:nth-child(2)');
    secondSpan.textContent = hours + ':' + minutes + ':' + seconds;
}

//두 자리 숫자로 포맷팅하는 함수
function formatTwoDigits(num) {
    return num < 10 ? "0" + num : num;
}


/*태그별 후원 조회 함수*/
document.querySelectorAll('.tag').forEach(function(btn) {
    btn.addEventListener('click', function() {
        //모든 버튼에서 .tag-active 클래스 제거
        document.querySelectorAll('.tag').forEach(function(element) {
            element.classList.remove('tag-active');
        });

        //클릭된 버튼에만 .tag-active 클래스 추가
        btn.classList.add('tag-active');

        const keyword = btn.id; //클릭된 버튼의 id 값을 keyword로 설정

        axios.get('/support/search-tag', {
            params: {
                keyword: keyword,
                pageNo: 0
            }
        }).then(function(response) {
            const supportList = response.data.content;
            document.querySelector('.donation-line').innerHTML = '';

            for (let i = 0; i < 8; i++) {
                const support = supportList[i];

                if(support) {
                    //graph 의 width 설정(목표금액의 몇 퍼센트 모였는지 보여줌)
                    const progress = (support.supportAmount / support.goalAmount) * 100;
                    const graphWidth = progress > 100 ? '100%' : progress + '%';
                    const graph = '<div class="graph" style="width: ' + graphWidth + ';"></div>';

                    var row = `<div class="donation-box">
                                    <div class="donation-img-box">
                                        <div class="donation-status ${support.supportStatus === 'START' ? 'start' : support.supportStatus === 'ENDING_SOON' ? 'ending-soon' : support.supportStatus === 'END' ? 'end' : ''}">
                                            ${support.supportStatus === 'START' ? '후원 시작' :
                                            support.supportStatus === 'ENDING_SOON' ? '종료 임박' :
                                            support.supportStatus === 'END' ? '후원 종료' : ''}
                                        </div>
                                        <div class="donation-img"><img src="${support.firstImg}" class="first-pic" alt="animal"></div>
                                    </div>
                                    <a href="/support/detail/${support.supportId}" class="donation-title title-hover">${support.title}</a>
                                    <div class="donation-nickname gray-text">${support.nickname}</div>
                                    <div class="donation-amount">
                                        <div class="percent">
                                            ${graph}
                                            <div class="graph-bottom"></div>
                                        </div>
                                        <p class="support-amount"><span>${support.supportAmount}</span>원</p>
                                    </div>
                                </div>`;

                    document.querySelector('.donation-line').innerHTML += row;
                } else {
                    console.log('조회된 후원 없음');
                    return;
                }
            }
        })
        .catch(function(error) {
            console.error(error);
        });
    });
});


/*QnA 조회 함수*/
function loadQna(pageNo) {
    axios.get('/qna', {
        params: {
            pageNo: pageNo
        }
    })
    .then(function(response) {
        const qnaList = response.data.content;

        for (let i = 0; i < 3; i++) {
            const qna = qnaList[i];
            if (qna) {
                let row = `<div class="qna-detail-box">
                                <a href="/qna/detail/${qna.qnaId}" class="qna-detail-title title-hover">${qna.title}</a>
                                <p class="gray-text">${qna.createDate}</p>
                                <p class="qna-content">${qna.content}</p>
                            </div>`;
                document.querySelector('.qna-list').innerHTML += row;
            } else {
                 console.log('QnA 없음');
             }
        }
    })
    .catch(function(error) {
        console.error(error);
    });
}

/*페이지 로드 시 함수 실행*/
document.addEventListener('DOMContentLoaded', function() {
    socialLoginSuccess();
    loadEndingSoonSupport();
    loadTotalAmount();
    document.getElementById('mml').click(); //첫 번째 버튼 자동 클릭
    loadQna(0);
});