# AWS DynamoDB Java CLI 예제

Java(AWS SDK v2)로 **DynamoDB 직원(Employee) 데이터**를 CRUD하는 콘솔 프로젝트입니다.  
이 문서는 프로젝트 구조/실행 방법/권한 설정/트러블슈팅을 한 번에 볼 수 있도록 재정리했습니다.

---

## 1) 프로젝트 개요

- 목적: Java 애플리케이션에서 DynamoDB 테이블에 데이터 추가/조회/삭제/전체 조회를 수행
- 실행 형태: 콘솔 메뉴 기반 CLI
- 데이터 모델: `id` + `name` 복합 키(Partition Key + Sort Key) 기반 조회/삭제

### 제공 기능

1. 직원 추가 (`PutItem`)
2. 직원 단건 조회 (`GetItem`)
3. 직원 삭제 (`DeleteItem`)
4. 직원 전체 조회 (`Scan`)

---

## 2) 기술 스택

- Java 17
- Maven
- AWS SDK for Java v2 (`dynamodb`, `auth`, `regions`)
- SLF4J(Simple)

---

## 3) 코드 구조

```text
src/main/java/
├── MainApp.java
└── com/cognizant/
    ├── Employee.java
    └── EmployeeManager.java
```

- `MainApp`
  - 콘솔 입력/메뉴 처리
  - 사용자 입력값을 받아 `EmployeeManager` 메서드 호출
- `Employee`
  - 직원 엔티티 (`id`, `name`, `position`)
- `EmployeeManager`
  - DynamoDB 클라이언트 생성
  - `addEmployee`, `viewEmployee`, `deleteEmployee`, `listEmployees` 구현

---

## 4) DynamoDB 테이블 설계

현재 코드 기준으로 테이블은 아래와 같이 맞추는 것을 권장합니다.

- 테이블명: `Employees`
- 파티션 키(Partition Key): `id` (String)
- 정렬 키(Sort Key): `name` (String)

> `viewEmployee`, `deleteEmployee`가 `id + name` 복합 키를 사용하므로 정렬 키까지 포함된 테이블이 안전합니다.

---

## 5) 실행 전 준비

### 5-1. 필수 설치

- JDK 17+
- Maven 3.8+
- AWS CLI

### 5-2. AWS 자격 증명 설정

```bash
aws configure
```

입력값 예시(실제 값 사용):
- AWS Access Key ID
- AWS Secret Access Key
- Default region name (예: `ap-northeast-2`)
- Default output format (`json`)

### 5-3. 권한(IAM) 설정

최소한 아래 DynamoDB 액션은 허용되어야 합니다.

- `dynamodb:GetItem`
- `dynamodb:BatchGetItem`
- `dynamodb:PutItem`
- `dynamodb:UpdateItem`
- `dynamodb:DeleteItem`
- `dynamodb:Scan`
- (선택) `dynamodb:DescribeTable`

정책 예시(마스킹 템플릿):

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "AllowCrudForEmployeesTable",
      "Effect": "Allow",
      "Action": [
        "dynamodb:GetItem",
        "dynamodb:BatchGetItem",
        "dynamodb:PutItem",
        "dynamodb:UpdateItem",
        "dynamodb:DeleteItem",
        "dynamodb:Scan",
        "dynamodb:DescribeTable"
      ],
      "Resource": "arn:aws:dynamodb:<REGION>:<ACCOUNT_ID>:table/Employees"
    }
  ]
}
```

---

## 6) 빌드 및 실행

### 빌드

```bash
mvn clean package
```

### 실행(권장)

```bash
mvn -q exec:java -Dexec.mainClass=MainApp
```

---

## 7) 사용 흐름 예시

1. 프로그램 실행
2. `1` 선택 후 `ID`, `Name`, `Position` 입력
3. `2` 선택 후 `ID`, `Name`으로 단건 조회
4. `3` 선택 후 `ID`, `Name`으로 삭제
5. `4` 선택 후 전체 목록 확인
6. `5` 선택 후 종료

---

## 8) 자주 발생하는 오류 & 해결

### 8-1. AccessDeniedException

증상 예시:
- `not authorized to perform dynamodb:GetItem`
- `not authorized to perform dynamodb:DescribeTable`

해결:
- IAM 사용자/역할에 필요한 DynamoDB 액션이 포함되어 있는지 확인
- 리소스 ARN이 실제 리전/계정/테이블명과 일치하는지 확인
- AWS CLI의 현재 계정/프로파일이 실행 대상과 일치하는지 확인

### 8-2. 키 타입 불일치(Type mismatch)

증상 예시:
- `Type mismatch for key id expected: S actual: N`

원인:
- DynamoDB 테이블 키 타입(String)과 Java 코드 입력 타입이 불일치

해결:
- 테이블 키 정의와 코드의 AttributeValue 타입(`fromS`, `fromN`)을 일치시킴

---

## 9) DynamoDB vs RDBMS 간단 비교

| 항목 | DynamoDB (NoSQL) | RDBMS |
|---|---|---|
| 데이터 모델 | Key-Value / Document | Table / Row / Column |
| 스키마 | 유연(스키마리스) | 엄격한 스키마 |
| 확장성 | 수평 확장(Scale-out) | 주로 수직 확장(Scale-up) |
| 조인 | 직접 조인 없음 | SQL 조인 가능 |
| 운영 | 완전관리형(Managed) | 운영/튜닝 관리 필요 |

---

## 10) 민감정보(보안) 처리 내역

본 README는 아래 정보를 모두 **마스킹/일반화**하여 정리했습니다.

- AWS 계정 ID
- IAM 사용자명
- 구체적인 ARN 원문
- 요청 ID 등 추적 식별자

권장 사항:
- 계정 ID, Access Key, Secret Key, 내부 사용자명은 문서/스크린샷/코드에 직접 노출하지 않기
- 예시가 필요할 때는 `<ACCOUNT_ID>`, `<IAM_USER>`, `<REGION>` 같은 플레이스홀더 사용

---

## 11) 향후 개선 제안

- 환경변수 기반 테이블명/리전 분리 (`Employees` 하드코딩 제거)
- `Query` 기반 조회(조건 확장)
- 입력값 검증(공백, 길이 제한)
- 예외 처리 및 사용자 친화적 에러 메시지 표준화
- JUnit 테스트 추가 및 CI 파이프라인 연동


---

## 12) Python FastAPI 모듈 추가

Spring Boot의 계층형 구조(Controller/Service/Repository)와 유사한 방식으로 `python-fastapi` 모듈을 추가했습니다.

```text
python-fastapi/
├── app/
│   ├── api/employee_router.py
│   ├── services/employee_service.py
│   ├── repositories/employee_repository.py
│   ├── config/settings.py
│   ├── config/database.py
│   ├── models/employee.py
│   └── main.py
└── requirements.txt
```

실행:

```bash
cd python-fastapi
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

환경변수:

- `APP_AWS_REGION`
- `APP_DYNAMODB_TABLE_NAME`
