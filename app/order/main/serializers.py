from rest_framework import serializers
from .models import (Order, UserInfo, Product)

class UserInfoSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserInfo
        fields = ['id', 'fullName', 'phone', 'email', 'password']

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['id', 'name']

class OrderSerializer(serializers.ModelSerializer):
    class Meta:
        model = Order
        fields = ['id', 'quantity', 'address', 'latitude', 'longitude', 'product_Id', 'user_Id']