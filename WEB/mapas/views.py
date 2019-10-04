# Por defecto
from django.shortcuts import render

# LLamamos a los metodos por HHTPS
from django.http import HttpResponse

def mapasA(request):
    return render(request, 'mapas/mapasA.html')

def mapasB(request):
    return render(request, 'mapas/mapasB.html')
