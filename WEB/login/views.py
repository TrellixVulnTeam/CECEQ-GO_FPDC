# Por defecto
from django.shortcuts import render

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def login(request):
    return render(request,'login/login.html',{'title': 'Login - Home'})
