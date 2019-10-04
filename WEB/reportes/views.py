# Por defecto
from django.shortcuts import render

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def reporte_1(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/reportes_1.html')
    return render(request, 'login/login.html')