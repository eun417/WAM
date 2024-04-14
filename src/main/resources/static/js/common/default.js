const api = axios.create({
  baseURL: "http://localhost:8081",
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