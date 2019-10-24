from django.shortcuts import render
# Por defecto
from django.shortcuts import render
# Llamamos a los metodos por HHTPS
from django.http import HttpResponse
from django.contrib.auth import logout as do_logout
from django.shortcuts import render, redirect
# Llamamos las funciones de util
from usuarios.util import *


# Create your views here.
def instalaciones(request):
    if request.user.is_authenticated:
        return render(request, 'instalaciones/index.html')
    return redirect('login')
