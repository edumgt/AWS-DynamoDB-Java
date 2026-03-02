# FastAPI DynamoDB 모듈

Spring Boot에서 `Controller -> Service -> Repository`로 구성하듯이 FastAPI도 아래 구조로 분리했습니다.

```text
python-fastapi/
├── app/
│   ├── api/employee_router.py               # Controller 역할
│   ├── services/employee_service.py         # Service 역할
│   ├── repositories/employee_repository.py  # Repository 역할
│   ├── config/
│   │   ├── settings.py                      # 환경설정
│   │   └── database.py                      # DynamoDB 연결 팩토리
│   ├── models/employee.py                   # DTO/Schema
│   └── main.py                              # 앱 진입점
└── requirements.txt
```

## 실행

```bash
cd python-fastapi
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload --port 8000
```

## 환경변수

`APP_` prefix를 사용합니다.

- `APP_AWS_REGION` (기본값: `ap-northeast-2`)
- `APP_DYNAMODB_TABLE_NAME` (기본값: `Employees`)

## API

- `POST /employees`
- `GET /employees/{employee_id}/{name}`
- `DELETE /employees/{employee_id}/{name}`
- `GET /employees`
- `GET /health`
