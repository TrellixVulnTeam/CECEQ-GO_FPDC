# Por defecto
from django.shortcuts import render

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def mapasA(request):
    if request.user.is_authenticated:
        return render(request, 'mapas/mapasA.html')
    return render(request, 'login/login.html')
def mapasB(request):
    if request.user.is_authenticated:
        return render(request, 'mapas/mapasB.html')
    return render(request, 'login/login.html')