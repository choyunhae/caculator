# caculator
# calculator
1. 과제 개요
  - 주제에 대한 전반적인 내용을 요약해서 기술
 
-영수증을 인식하는 가계부 안드로이드 어플리케이션 구현을 계획하고 있습니다. 스캔된 이미지를 선택하고 cloud vision API 라이브러리를 사용하여 영수증 이미지의 텍스트를 인식합니다. 인식한 글자에서 금액 정보를 추출하여 사용자의 지출내역에 추가합니다. 수입/지출 내역은 날짜, 금액, 카테고리 메모 등으로 분류해 데이터베이스에 저장합니다.


2. 과제 수행 내용 및 결과
  - 최종 과제 수행 내용 및 결과를 다음 네 단계 과정으로 구분해서 구체적으로 기술
- 딥러닝에 기반한 API를 실제 사용해본다. Android Studio를 이용 하여 안드로이드 어플리케이션에서 사용    하는 기능들을 구현하고 학교에서 배우지 않는 언어인 Java를 스스로 익히고 새로운 언어를 사용해봅니다.
- 기존 어플리케이션 분석
1. “Recpic” : 영수증정보를 어플 내에 저장합니다. 사용자가 스스로 카테고리를 선택하고 정보를 수정할 수 있습니다. 또, 영수증을 카메라로 찍어 스캔된 형태로 보관할 수 있습니다.
2. “WepleMoney” :　월간, 주간, 일간 수입/지출 내역을 제공합니다. 수입/지출 내역을 원형 그래프, 꺾은선 그래프 등으로 시각화합니다. 필터를 적용하여 개별 카테고리에 해당하는 정보를 볼 수 있습니다.

  2.1 요구 사항
  - 요구사항 정의 및 분석
1) 사용자의 수입을 입력한다.
2) 갤러리에서 영수증 이미지를 불러온다.
3) 영수증에서 총 지출 금액을 추출한다.
4) 사용자가 지출 내역을 직접 입력하고 거래 일자 및 지출 금액을 함께 저장한다.
5) 달력에서 수입/지출내역을 리스트로 확인한다.
6) 수입에서 카테고리별 소비패턴(비율)을 알려준다.


 
2.2 시스템 설계 (합성 및 분석)
    - 전체 구조, 주요 자료구조 및 알고리즘, 그리고 사용자 인터페이스와 데이터 관리 시스템 등의 설계 
 
<사용자>
    -수입인지 지출인지 선택한다.
    -‘수입’을 선택하면 ‘원’단위로 금액/날짜를 입력받는다.
    -‘지출’을 선택하면
    -영수증 이미지를 선택한다.
    -저장된 영수증 이미지를 불러온다.
    -지불 방식 카테고리*를 선택한다.
    -지출내역 카테고리*를 선택한다.
    -영수증에서 읽어온 지출정보*를 입력/수정한다.
    -지출내역에 기반한 소비패턴을 월간/주간 그래프로 확인한다.
    -지출내역을 달력의 날짜를 선택해 확인한다.
    -지출내역을 삭제한다.
    -잔액을 일 별로 확인한다.
  
<시스템>
   -영수증 이미지를 불러온다.
           ▶스캔된 영수증 이미지 사용
    -영수증 이미지에서 텍스트를 추출한다. 
           ▶google cloud vision 사용
    -추출된 텍스트에서 총 사용 금액을 추출/저장한다  	 
           ▶총 사용 금액만을 추출하는 알고리즘
           ▶firebase를 이용해 json트리로 데이터를 구조화한다.
           ▶추출된 금액은 원 단위로 환산해서 사용한다.
    -총수입과 총지출 내역을 그래프로 도식화한다.
            ▶날짜, 지출내역 카테고리를 키 값으로 사용
    -그래프는 월간/주간으로 보여준다.
    -사용자가 입력한 내역을 달력의 해당 날짜에 저장한다.
    -잔액은 일 별로 출력한다.
           ▶일 별 잔액 계산하는 알고리즘

   

		*지불방식 카테고리: 카드, 현금
		*지출내역 카테고리 : 식비, 쇼핑/외식, 세금, 주거, 통신, 교통
		*지출정보 : 지출금액


  2.3 구현
  - 구체적인 구현 환경(OS, 개발 언어 및 tool 등) 기술
    - 개발언어 : java
    - tool : Android Studio, firebase, google cloud vision API

  
2.4 시험평가 
  - 테스팅 계획 및 결과를 구체적으로 기술

<테스팅 계획 및 결과>

- 수입 : 1.‘수입’버튼을 클릭한다.
         2. 현금/카드 중 하나를 선택한다.
         3. 카테고리를 선택한다.
         4. 금액을 입력하고 코멘트를 입력한다.
         5. 저장버튼을 누르면 데이터 베이스에 정보가 저장된다.
         6. 달력화면에서 리스트로 확인한다.

-지출  : 1. '지출 버튼을 클릭한다.
         2. 영수증 이미지 접근 권한을 얻는다.
         3. 현금/카드 중 하나를 선택한다.
         4. 카테고리를 선택한다.
         5. 금액을 입력하고 코멘트를 입력한다.
        5-1. +버튼을 클릭해 저장되있는 영수증 이미지 중 하나를 선택한다.
        5-2. 금액을 자동으로 입력받는다.
         6. 저장버튼을 누르면 데이터 베이스에 정보가 저장된다.
         7. 달력화면에서 리스트로 확인한다.
         8. 그래프로 소비패턴을 확인한다.
 
<테스트 화면>
![갤러리_영수증저장](https://user-images.githubusercontent.com/54641007/78698929-09f1db80-793e-11ea-84c2-0a7367867d0e.png)
![달력](https://user-images.githubusercontent.com/54641007/78698931-0bbb9f00-793e-11ea-8914-a66db566114a.png)
![달력에서 내역확인](https://user-images.githubusercontent.com/54641007/78698933-0bbb9f00-793e-11ea-91d9-fb720a58b3e8.png)
![달력에서 지출내역확인](https://user-images.githubusercontent.com/54641007/78698934-0c543580-793e-11ea-8aa7-d119af522b63.png)
![수입 및 지출 버튼](https://user-images.githubusercontent.com/54641007/78698938-0ceccc00-793e-11ea-8981-e387a7d2a758.png)
![수입내역 입력](https://user-images.githubusercontent.com/54641007/78698944-0d856280-793e-11ea-9d1a-219bad69d1c0.png)
![영수증_불러오기](https://user-images.githubusercontent.com/54641007/78698946-0d856280-793e-11ea-92d7-9cf4c08579fc.png)
![지출내역 입력](https://user-images.githubusercontent.com/54641007/78698949-0e1df900-793e-11ea-92d4-1768ab1563b8.png)