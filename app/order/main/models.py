from django.db import models
from django.utils import timezone

# Create your models here.

class UserInfo (models.Model):
    id = models.AutoField(auto_created= True, primary_key= True, unique= True, editable= False)
    fullName = models.CharField (max_length= 250, default= '')
    email = models.CharField (max_length= 250, default= '')
    phone = models.CharField (max_length= 250, default= '')
    password = models.CharField (max_length= 250, default= '')
    isActive = models.BooleanField(default = True)

    def __str__ (self):
        return self.fullName

class Product (models.Model):
    id = models.AutoField(auto_created= True, primary_key= True, unique= True, editable= False)
    name = models.CharField (max_length= 250, default= '')
    isActive = models.BooleanField(default = True)

    def __str__ (self):
        return self.name

class Order (models.Model):
    id = models.AutoField(auto_created= True, primary_key= True, unique= True, editable= False)
    quantity = models.CharField (max_length= 250, default= '')
    address = models.CharField (max_length= 250, default= '')
    latitude = models.CharField (max_length= 250, default= '')
    longitude = models.CharField (max_length= 250, default= '')
    order_date = models.DateTimeField('date', default = timezone.now())

    product_Id = models.ForeignKey(Product, default= -1, on_delete= models.SET_DEFAULT)
    user_Id = models.ForeignKey(UserInfo, default= -1, on_delete= models.SET_DEFAULT)

    def __str__ (self):
        return self.address
