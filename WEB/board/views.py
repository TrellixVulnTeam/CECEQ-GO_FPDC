# Por defecto
from django.shortcuts import render

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def dash(request):
    return render(request,'board/dashboard.html')
