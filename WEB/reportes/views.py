# Por defecto
from django.shortcuts import render, redirect
from reportes.util import *
import datetime
import time
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


def show_users(request):
    if request.user.is_authenticated:
        hoy = time.strftime("%I:%M:%S")
        users_anon = get_anonimus_users()
        args = {'title': 'CECEQ Reportes', 'users_anon': users_anon,'hoy':hoy}
        return render(request, 'reportes/usuarios.html', args)
    return redirect('login')