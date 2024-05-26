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