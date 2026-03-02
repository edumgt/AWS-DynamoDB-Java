import boto3

from app.config.settings import settings


class DynamoDbClientFactory:
    @staticmethod
    def resource():
        return boto3.resource("dynamodb", region_name=settings.aws_region)

    @staticmethod
    def table():
        return DynamoDbClientFactory.resource().Table(settings.dynamodb_table_name)
