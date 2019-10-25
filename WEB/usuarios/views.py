# Por defecto
from django.shortcuts import render
# Llamamos a los metodos por HHTPS
from django.http import HttpResponse
from django.contrib.auth import logout as do_logout
from django.shortcuts import render, redirect
# Llamamos las funciones de util
from usuarios.util import *

def show_users(request):
    if request.user.is_authenticated:
        if request.user.is_active:
            if request.user.tiene_permiso("ver usuarios"):
                users = get_registered_users()
                users_noR = get_non_registered_users()
                args = {'title': 'CECEQ USUARIOS', 'users': users, 'users_noR': users_noR}
                return render(request, 'usuarios/usuarios.html', args)
            else:

                return redirect('board')
        else:
            return redirect('logout')
    return redirect('login')

def adduser(request):
    if request.method == "POST":
        id = request.POST.get('id-user-add')
        username = get_username(id);
        add_user_in_database(id, username)
    return redirect('usuarios')

def eliuser(request):
    if request.method == "POST":
        id = request.POST.get('id-user-eli')
        delete_user_in_database(id)
    return redirect('usuarios')

def actiuser(request):
    if request.method == "POST":
        id = request.POST.get('id-user-acti')
        perma_activate_user_in_database(id)
    return redirect('usuarios')

def desuser(request):
    if request.method == "POST":
        id = request.POST.get('id-user-des')
        opcion = request.POST.get('opcion-desactivar')
        perma_deactivate_user_in_database(id, opcion)
    return redirect('usuarios')
