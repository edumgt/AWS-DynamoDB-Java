from fastapi import FastAPI

from app.api.employee_router import router as employee_router

app = FastAPI(title="AWS DynamoDB FastAPI Example")
app.include_router(employee_router)


@app.get("/health")
def health():
    return {"status": "ok"}
