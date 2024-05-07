const api = axios.create({
  baseURL: "http://cea6439.com",
  headers: { "Content-type": "application/json" },
  withCredentials: true,
});

/*헤더에 토큰 등록*/
api.interceptors.request.use(
  function (config) {
    //요청을 보내기 전에 수행할 일
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken && config.headers) {
      //access token을 header에 담아 요청
      config.headers.Authorization = `Bearer ${accessToken}`;
    } else {
        alert("로그인 후 이용해주세요.");
        return;
    }
    return config;
  },
  function (error) {
    console.log(error);
    return Promise.reject(error);
  }
);

/*Access Token 재발급*/
api.interceptors.response.use(
  function (response) {
    console.log(response);
    return response;
  },
  async function (error) {
    //오류 요청을 보내기전 수행할 일
    const msg = error.response.data.message;
    const status = error.response.status;
    console.log('msg', msg);
    console.log('status', status);

    if (status === 401) {
        if (msg === "토큰이 만료되었습니다.") {
            const refreshToken = localStorage.getItem('refreshToken');
            if (!refreshToken) {
                return Promise.reject('Refresh token을 찾을 수 없습니다.');
            }

            //TokenRequestDto 객체 생성
            const tokenReq = {
                accessToken: localStorage.getItem('accessToken'),
                refreshToken: refreshToken
            };

            await api.post('/auth/refresh', tokenReq)
                .then(function(response) {
                    console.log("토큰 재발급 성공")
                    //새로운 access token 발급
                    const accessToken = response.data.accessToken;
                    localStorage.setItem('accessToken', accessToken);
                    error.config.headers["Authorization"] = `Bearer ${accessToken}`;
                    return api(error.config);
                })
                .catch(function(refreshError) {
                    console.error("토큰 재발급 오류:", refreshError);
                    return Promise.reject(refreshError);
                });
        } else if (msg === "리프레시 토큰이 만료되었습니다.") {
            localStorage.clear();
            window.location.href = "/auth/login";
            alert("토큰이 만료되어 자동으로 로그아웃 되었습니다.");
        }
    } else if (status == 400) {
        //alert(msg);
    }
    return Promise.reject(error);
  }
);


/* 빈 화면 */
//table
function noResult(colspan, fragment) {
    const noResult = `<td colspan="${colspan}">
                            <div class="no-result">
                                <span class="info-icon material-symbols-outlined">info</span>
                                <p class="info-memo">조회된 결과가 없습니다.</p>
                            </div>
                       </td>`;
    const tr = document.createElement('tr');
    tr.innerHTML = noResult;
    fragment.appendChild(tr);
}

//div
function noResultDiv(fragment) {
    const noResult = `<div class="no-result">
                        <span class="info-icon material-symbols-outlined">info</span>
                        <p class="info-memo">조회된 결과가 없습니다.</p>
                    </div>`;
    let div = document.createElement('div');
    div.innerHTML = noResult;
    fragment.appendChild(div.firstChild);
}

//삭제 후 요소가 하나도 없을 때 빈 화면 출력하는 함수
function deleteAfterNoResult(tableBody) {
    //남은 목록 수 확인
    const remainingRows = tableBody.querySelectorAll('tr').length;
    if (remainingRows === 0) {
        noResult(6, tableBody);
    }
}