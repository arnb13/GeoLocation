from django.contrib import admin
from .models import Order, UserInfo, Product

# Register your models here.

admin.site.register(UserInfo)
admin.site.register(Product)
admin.site.register(Order)

