from django.shortcuts import render, redirect
from rest_framework.response import Response
from rest_framework import status
from django.http import HttpResponse
from django.contrib import messages
from rest_framework.decorators import parser_classes
from django.http import JsonResponse
from datetime import date

from rest_framework.decorators import api_view, renderer_classes
from rest_framework.renderers import JSONRenderer, TemplateHTMLRenderer

from django.template import loader
from .models import UserInfo, Order, Product
from .serializers import OrderSerializer, UserInfoSerializer, ProductSerializer


# Create your views here.
@api_view(['GET'])
def get_product(request):
    try:
        product = Product.objects.filter(isActive = True)
        serializer = ProductSerializer(product, many = True)
    except:
        return Response({'error': 'error getting data'})

    return Response(serializer.data, status= status.HTTP_200_OK)

@api_view(['POST'])
def registration(request):
    try:
        temp = UserInfo.objects.get(email = request.data['email'])
        c = {'status': 'customer exception'}
        return Response(c, status= status.HTTP_400_BAD_REQUEST)
    except:
        try:
            print(request.data)
            user = UserInfo()
            serializer = UserInfoSerializer(user, data = request.data)
            if serializer.is_valid():
                serializer.save()
                user = UserInfo.objects.get(phone = request.data['phone'])
                c = UserInfoSerializer(user)
                return Response(c.data, status= status.HTTP_200_OK)
            else:
                c = {'status': 'serializer.save'}
                return Response(c, status= status.HTTP_400_BAD_REQUEST)
        except:
            c = {'status': 'User exception'}
            return Response(c, status= status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def login(request):
    try:
        user = UserInfo.objects.get(email = request.data['email'])
        if user.password == request.data['password']:
            serializer = UserInfoSerializer(user)
            return Response(serializer.data, status= status.HTTP_200_OK)
        else:
            c = {'status': 'email or password do not match'}
            return Response(c, status = status.HTTP_400_BAD_REQUEST)
    except:
        c = {'status': 'email not found'}
        return Response(c, status = status.HTTP_400_BAD_REQUEST)

@api_view(['POST'])
def order(request):
    try:
        user = UserInfo.objects.get(id = request.data['user_Id'])
        product = Product.objects.get(id = request.data['product_Id'])

        print(user)
        print(product)

        order = Order()
        order.product_Id = product
        order.user_Id = user

        serializer = OrderSerializer(order, request.data)

        if serializer.is_valid():
            serializer.save()
        else:
            return Response({'error': 'error 2'}, status= status.HTTP_400_BAD_REQUEST)
        return Response({'status': 'Success'}, status= status.HTTP_201_CREATED)
    except:
        return Response({'error': 'error 1'}, status= status.HTTP_400_BAD_REQUEST)
        

