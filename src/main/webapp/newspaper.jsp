<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <link rel="stylesheet" href="asset/css/newspaper.css">
  <title>đọc báo</title>
</head>
<style>
  body{
    background-image: url("asset/png/background/background.png");
    background-size: auto;
  }
  @font-face {
    font-family: 'Poppins';
    src: url('${pageContext.request.contextPath}/assets/fonts/Poppins-Regular.ttf') format('ttf');
  }
  body{
    font-family: 'Poppins',sans-serif;
  }
</style>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="container">
  <div class="contentContainer">
    <div class="title">
      <h2>
        하늘 위로 두둥실 날아오르는 ‘플라잉카’, 떴다 떴다 자동차!
      </h2>
    </div>
    <div class="image" >
      <img src="${pageContext.request.contextPath}/asset/png/newspaper.jpg">
    </div>
    <div class="text">
      하늘 위로 두둥실 날아오르는 ‘플라잉카’, 떴다 떴다 자동차!
      도로 위를 달릴 뿐만 아니라 공중에서 날 수도 있는 차를 ‘플라잉카’라고 해요.
      ‘비행 자동차’ 또는 ‘개인용 항공기’라고도 하는데, 최근 미국 스타트업이 개발한 플라잉카의 영상이 큰 주목을 받았어요.
      플라잉카는 미국뿐만 아니라 중국 등 다양한 기업에서 만들고 있는데요.
      가까운 미래에 하늘 위를 날아다닐 플라잉카들을 살펴봐요.

      **도로 위의 장애물? 날아오르면 되지!**
      최근 미국 스타트업 알레프 에어로노틱스(이하 알레프)가 플라잉카 ‘모델 A’의 테스트 영상을 공개해 화제예요.
      영상 속 플라잉카는 도로 위를 달리다가 주차돼 있는 자동차와 가까워지는데요.
      운전 방향을 틀어 장애물을 피하는 대신 속도를 늦추더니 주차된 차 위로 두둥실 날아올라 비행했어요.

      10m 정도 낮게 날던 플라잉카는 다시 땅으로 착륙해 도로 위를 주행했지요.
      전기차인 모델 A는 1회 충전으로, 도로에서는 약 354㎞를, 비행할 때는 177㎞를 이동할 수 있어요.
      주행하는 상태에서 곧바로 이륙할 수 있는 이유는 자동차의 무게가 가벼운 덕분.
      보통 자동차 제조업체들이 튼튼한 자동차를 만들기 위해 무거운 철판을 활용하는 것과 달리
      알레프가 만든 플라잉카는 가벼운 탄소 섬유로 만들어졌어요.

      또 자동차 몸체에는 무거운 장치들이 들어 있는 대신 프로펠러 8개가 바퀴와 수직 방향으로 달렸어요.
      그래서 플라잉카를 정면에서 보면 프로펠러가 보이지 않고, 위에서 보면 자동차 몸체 안에
      프로펠러가 있는 것을 확인할 수 있지요.

      약 30만 달러(약 4억 3000만 원)에 달하는 플라잉카 모델 A는 올해 말에 출시될 예정으로,
      이미 3330건의 예약 주문을 확보한 상태랍니다.

      **트렁크에 짐이 아닌 비행기가?**
      자동차 자체가 비행할 수 있는 것은 아니지만, 자동차에 비행기를 싣고 다니는 차도 있어요.
      ‘중국의 테슬라’라 불리는 전기차 기업 샤오펑에 소속된 샤오펑에어로HT가 개발한
      ‘랜드 에어크래프트 캐리어(Land Aircraft Carrier·LAC)’예요.

      LAC는 5인승 전기 미니밴과 2인승 전기 수직이착륙(eVTOL) 드론으로 구성된 플라잉카예요.
      미니밴 트렁크에 드론을 싣고 다니며, 필요할 때 드론을 차량에서 분리해 비행할 수 있는 구조인 것.

      샤오펑에어로HT는 올 초 미국 라스베이거스에서 열렸던 세계 최대 가전·정보통신(IT) 박람회
      ‘CES 2025’에서 플라잉카 ‘LAC’를 선보여 주목을 받은 바 있어요.
      샤오펑에어로HT는 올해부터 LAC의 대량 생산을 시작해 2026년에 첫 배달을 계획하고 있답니다.

      1월, 미국 정보기술(IT) 전문 매체 테크크런치에 따르면 LAC 가격은 약 3억 원으로 추정돼요.
      LAC 주문은 3000건에 달하는데, 몇몇 주문자들은 LAC를 긴급 구조나 수색에 활용할 계획이랍니다.

      **자동차와 비행기 둘 다 가능**
      LAC는 비행기를 싣고 다니는 플라잉카라면, 중국 체리차가 지난해 공개한 플라잉카
      ‘랜드 앤드 에어 비히클(Land and Air Vehicle)’은 운전자석 아래는 자동차, 위에는 드론을
      끼울 수 있는 형태예요.

      운전석을 바퀴가 달린 자동차에 끼우면 자동차가 되고, 운전석을 드론 날개에 끼우면
      비행기가 되는 식.

      마치 차 위에 헬기를 얹은 모양으로, 운전석을 무엇과 조립하느냐에 따라
      자동차 혹은 비행기로 활용할 수 있지요.

      이 플라잉카는 최대 2명이 탑승할 수 있고, 완전 자율주행 시스템을 탑재해
      운전대와 페달이 없는 것이 특징이에요.
      땅에서 최대 1㎞ 높이까지 날아오를 수 있고, 최대 비행 속도는 시속 120㎞로
      최대 40분간 비행할 수 있답니다.
    </div>
  </div>
  <div class="questionContainer">
    <div class="question">Chủ đề chính của đoạn văn trên là gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Đoạn văn trên đề cập đến nội dung gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Vì sao tác giả lại đưa ra quan điểm này?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn có đồng ý với ý kiến của tác giả không? Vì sao?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn rút ra được bài học gì từ đoạn văn trên?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Nếu có thể thay đổi một ý trong đoạn văn, bạn sẽ thay đổi điều gì?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>

    <div class="question">Bạn có ví dụ thực tế nào liên quan đến nội dung của đoạn văn không?</div>
    <input type="text" name="Answer" placeholder="Nhập câu trả lời" required>
  </div>
</div>
</body>
<jsp:include page="footer.jsp"></jsp:include>
</html>
