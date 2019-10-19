# Por defecto
from django.shortcuts import render
from django.shortcuts import render, redirect
# LLamamos a los metodos por HHTPS
from django.http import HttpResponse
import datetime
#URLS de Reportes

def cursos(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/cursos.html')
    return redirect('login')

def salones(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/salones.html')
    return redirect('login')

def usuarios(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/usuarios.html')
    return redirect('login')

def visitas(request):
    if request.user.is_authenticated:
        return render(request, 'reportes/visitas.html')
    return redirect('login')

def dia_hoy(request):
    hoy = datetime.date.today()
    return render(request,hoy)
