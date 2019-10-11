# Por defecto
from django.shortcuts import render
# Llamamos a los metodos por HHTPS
from django.http import HttpResponse
from django.contrib.auth import logout as do_logout
from django.shortcuts import render, redirect
# Llamamos las funciones de util
from usuarios.util import *

def perfil(request):
    if request.user.is_authenticated:
        return render(request, 'usuarios/perfil.html')
    return redirect('login')

def show_users(request):
    if request.user.is_authenticated:
        users = get_registered_users()
        users_noR= get_non_registered_users()
        args = {'title':'CECEQ USUARIOS', 'users':users,'users_noR':users_noR}
        return render(request,'usuarios/usuarios.html', args)
    return redirect('login')
def show_modal_user(request):
    return 0;

